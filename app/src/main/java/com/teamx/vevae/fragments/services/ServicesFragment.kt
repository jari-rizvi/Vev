package com.teamx.vevae.fragments.services

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.vevae.App.App
import com.teamx.vevae.DummyModel.ServiceType
import com.teamx.vevae.Models.PlaceOrder
import com.teamx.vevae.Models.services.BuyServices
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.ServiceDateAdapter
import com.teamx.vevae.adapter.ServicesAdapter
import com.teamx.vevae.adapter.ServicesTypeAdaper
import com.teamx.vevae.databinding.FragmentServicesBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnPaymentMethodClickListener
import com.teamx.vevae.onClick.OnSearchResultClickListener
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.activities.PaymentActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServicesFragment : BaseFragment(), OnPaymentMethodClickListener, OnSearchResultClickListener {

    private  lateinit var fragmentServicesBinding: FragmentServicesBinding
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var serviceDateAdapter: ServiceDateAdapter
    private lateinit var serviceTypeAdapter: ServicesTypeAdaper
    private lateinit var buyServices: BuyServices

    private lateinit var options: NavOptions
    private var serviceId : Int =  1
    private var serviceDate : String = ""
    private var appoimentserviceId : Int =  0
    private var price : Int =  0

    private   var paymentMethodId : Int = -1
    private  lateinit  var transactionId : String
    lateinit var paymentMethodType : String;





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_services, container, false)
        fragmentServicesBinding = FragmentServicesBinding.inflate(inflater,container,false)

        (activity as MainActivity?)!!.hideNavBar()
        paymentMethodType = Prefs.getString("paymentMethod","")

         options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        checkLangauge()


        val  bundle = arguments
        if (bundle != null){
            serviceId = bundle.getInt("id")
            Prefs.putInt("serviceId",serviceId)
            if (InternetConnection.checkConnection(requireContext())){

                getServices(serviceId)
            }else{

                fragmentServicesBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
            }
        }


        paymentMethodType()

        fragmentServicesBinding.btnBack.setOnClickListener{

            popUpStack()
        }

        fragmentServicesBinding.btnSelectPaymentMethod.setOnClickListener {

            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.  paymentMethodFragment, null,options)

        }


        if (InternetConnection.checkConnection(requireContext())){
            getServices(serviceId);
        }else{
            fragmentServicesBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))

        }


        val options  = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        fragmentServicesBinding.btnBuy.setOnClickListener{

            val userToken = Prefs.getString("userToken","");

            if (userToken.isNotEmpty()){

                if (validateFields()){
                    Prefs.putString("storeType","service")
                    if (paymentMethodId == 0){
                        transactionId = "Cash On Delivery"
                        buyService(paymentMethodId,transactionId)
                    }
                    else{
                        Prefs.putInt("amount",price );
                        getPermission();

                    }

                }
            }else{
                showLoginBottomSheet(0,false)
            }
        }


        return fragmentServicesBinding.root

    }


    private fun checkLangauge() {

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentServicesBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentServicesBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            fragmentServicesBinding.paymentMethodBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentServicesBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentServicesBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))
            fragmentServicesBinding.paymentMethodBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ENGLISH

        }

    }

    private fun paymentMethodType() {

        if (paymentMethodType.isNotEmpty()){
            if (paymentMethodType.equals(getString(R.string.debit_credit))){
                paymentMethodId = 1
                fragmentServicesBinding.paymentName.setText(getString(R.string.debit_credit))
                fragmentServicesBinding.paymentImage.setImageResource(R.drawable.icon_credit_card)

            }else{
                paymentMethodId = 0
                fragmentServicesBinding.paymentName.setText(getString(R.string.cash))
                fragmentServicesBinding.paymentImage.setImageResource(R.drawable.icon_cash)
            }
        }
    }

    private fun getPermission() {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
//                        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
//                        navController.navigate(R.id.paymentFragment, null,options)
                        startActivity(Intent(requireActivity(), PaymentActivity::class.java))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                }
            }).check()
    }

    private fun buyService(paymentMethodId : Int,transactionId : String ) {

        val params = JSONObject()
        val productArrayObject = JSONArray()
        val userDetailsArray = JSONArray()

        try {

            val cartItem = JSONObject()
            cartItem.put("appoimentserviceId", appoimentserviceId)
            cartItem.put("date", serviceDate)
            productArrayObject.put(cartItem)


            val orderDetails = JSONObject()
            val userId = Prefs.getInt("userId",0);

            orderDetails.put("userId", userId.toString())
            orderDetails.put("price", price)
            orderDetails.put("type", "services")

            if (paymentMethodId==0){
                orderDetails.put("PaymentMethodId", "0")
                orderDetails.put("transaction_id", transactionId)
            }else{
                orderDetails.put("PaymentMethodId", "1")
                orderDetails.put("transaction_id", transactionId)
            }

            userDetailsArray.put(orderDetails)


            params.put("OrderDetail",userDetailsArray)
            params.put("OrderServices",productArrayObject)
            params.put("lang", lang)


        }catch (e : JSONException){
            fragmentServicesBinding.rootLayout.snackbar(e.localizedMessage)
        }

        proceedOrder(params.toString());


    }

    private fun proceedOrder(params: String) {

        WebServiceFactory.getInstance().buyService(params).enqueue(object : Callback<PlaceOrder>{
            override fun onResponse(call: Call<PlaceOrder>, response: Response<PlaceOrder>) {

                if (response.body()!!.flag==1){
                    Prefs.putString("transactionId", "")
                    fragmentServicesBinding.rootLayout.snackbar(getString(R.string.your_appointment_has_been_fixed_kindly_check_your_email))
                }else{
                    fragmentServicesBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<PlaceOrder>, t: Throwable) {

            }

        })
    }

    private fun validateFields(): Boolean {

        if(serviceDate.isEmpty()){
            fragmentServicesBinding.rootLayout.snackbar(getString(R.string.select_your_service))
            return false
        }
        if (appoimentserviceId==0){
            fragmentServicesBinding.rootLayout.snackbar(getString(R.string.kindly_select_appointment_date))
            return false
        }
        return true
    }

    private fun getServices(serviceId : Int) {

        WebServiceFactory.getInstance().getServices(serviceId,lang).enqueue(object : Callback<BuyServices>{
            override fun onResponse(call: Call<BuyServices>, response: Response<BuyServices>) {
                if (response.body()!!.flag==1){
                    buyServices = response.body()!!
                    setData(response.body()!!)
                }else{
                    fragmentServicesBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<BuyServices>, t: Throwable) {

            }

        })
    }

    private fun setData(buyServices: BuyServices) {


        fragmentServicesBinding.servicename.setText(buyServices.data.service[0].name)
        fragmentServicesBinding.serviceDescription.setText(buyServices.data.service[0].serviceDetail)

        //services option Recyclerview and adapter
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        fragmentServicesBinding.servicesRecyclerview.layoutManager = layoutManager
        serviceTypeAdapter = ServicesTypeAdaper(requireContext(),buyServices.data.servicesData,this)
        fragmentServicesBinding.servicesRecyclerview.adapter = serviceTypeAdapter




        //date Recyclerview and adapter
        val dateLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        fragmentServicesBinding.dateRecyclerview.layoutManager = dateLayoutManager
        serviceDateAdapter = ServiceDateAdapter(requireContext(),buyServices.data.servicesDates,this)
        fragmentServicesBinding.dateRecyclerview.adapter = serviceDateAdapter


        //Date  adapter with viewPager2
        servicesAdapter = ServicesAdapter(requireContext(),buyServices.data.serviceImages)
        fragmentServicesBinding.viewpager.adapter = servicesAdapter

        TabLayoutMediator(fragmentServicesBinding.tabLayout, fragmentServicesBinding.viewpager)
        { tab, position ->
            tab.text = buyServices.data.serviceImages[position].toString()
        }.attach()


    }

    override fun onPaymentClick(position: Int) {

        appoimentserviceId = buyServices.data.servicesData.get(position).id
        price = buyServices.data.servicesData.get(position).price

        Prefs.putInt("durationId",buyServices.data.servicesData.get(position).id)

    }

    override fun OnSearchedItemClick(position: Int) {

        buyServices.getData().servicesDates.get(position).setChecked(true)

        serviceDate = buyServices.data.servicesDates.get(position).servicesDate

        Prefs.putInt("serviceDateId", buyServices.data.servicesDates.get(position).id)

    }


    override fun onResume() {
        super.onResume()

        transactionId = Prefs.getString("transactionId", "")
        if (transactionId.isNotEmpty()){
            paymentMethodId = 1
            buyService(paymentMethodId,transactionId)
        }

    }

}