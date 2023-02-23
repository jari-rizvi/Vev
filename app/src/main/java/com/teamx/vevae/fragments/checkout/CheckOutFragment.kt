package com.teamx.vevae.fragments.checkout

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.PlaceOrder
import com.teamx.vevae.Models.checkout.CheckOut
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.CheckoutProductAdapter
import com.teamx.vevae.databinding.FragmentCheckOutBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.activities.PaymentActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckOutFragment : BaseFragment() {

    lateinit var fragmentCheckOutBinding: FragmentCheckOutBinding
    private lateinit var  checkoutProductAdapter: CheckoutProductAdapter
    lateinit var checkout: CheckOut
    private lateinit var options: NavOptions
    var totalAmount : Int =0
    private   var paymentMethodId : Int = -1
    private  lateinit  var transactionId : String
    lateinit var paymentMethodType : String;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_check_out, container, false)
        fragmentCheckOutBinding = FragmentCheckOutBinding.inflate(inflater,container,false)

        fragmentCheckOutBinding.totalprice.setText("00.00 AED")

        paymentMethodType = Prefs.getString("paymentMethod","")

        checkLangauge()
        paymentMethodType()

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        fragmentCheckOutBinding.btnSelectPaymentMethod.setOnClickListener {

            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.  paymentMethodFragment, null,options)
        }
        
        fragmentCheckOutBinding.btnBack.setOnClickListener{
            popUpStack()
        }

        if (InternetConnection.checkConnection(requireContext())){
            getCheckOutData()
        }else{
            fragmentCheckOutBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))

        }

        fragmentCheckOutBinding.btnPlaceOrder.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                if (isValidate()){
                    Prefs.putString("storeType","product")
                    if (paymentMethodId == 0){
                        transactionId = "Cash On Delivery"
                        implementChechOut(paymentMethodId,transactionId)
                    }
                    else{
                        Prefs.putInt("amount",totalAmount );
                        getPermission();

                    }
                }
            }else{
                showInternetToast()
            }

        }

        fragmentCheckOutBinding.btnChangeAddress.setOnClickListener{

            navBuilder.setEnterAnim(R.anim.slide_in)
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.allAddressFragment, null,navBuilder.build())
        }


        return fragmentCheckOutBinding.root
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
                         startActivity(Intent(requireActivity(),PaymentActivity::class.java))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                }
            }).check()
    }

    private fun isValidate(): Boolean {

        if (fragmentCheckOutBinding.paymentName.text.toString().equals("select payment method")){
            fragmentCheckOutBinding.rootLayout.snackbar(getString(R.string.select_payment_method))
            return false;
        }

        if (checkout.data.cartItem.isEmpty()){
            fragmentCheckOutBinding.rootLayout.snackbar(getString(R.string.cart_is_empty))
            return false
        }

        return true;
    }

    private fun checkLangauge() {

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
            fragmentCheckOutBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentCheckOutBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            fragmentCheckOutBinding.paymentMethodBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentCheckOutBinding.addressBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))


        }else{
            lang = LocaleManager.LANGUAGE_ENGLISH
            fragmentCheckOutBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentCheckOutBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))
            fragmentCheckOutBinding.paymentMethodBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentCheckOutBinding.addressBackIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))



        }

    }

    private fun paymentMethodType() {

        if (paymentMethodType.isNotEmpty()){
            if (paymentMethodType.equals(getString(R.string.debit_credit))){
                paymentMethodId = 1
                fragmentCheckOutBinding.paymentName.setText(getString(R.string.debit_credit))
                fragmentCheckOutBinding.paymentImage.setImageResource(R.drawable.icon_credit_card)

            }else{
                paymentMethodId = 0
                fragmentCheckOutBinding.paymentName.setText(getString(R.string.cash))
                fragmentCheckOutBinding.paymentImage.setImageResource(R.drawable.icon_cash)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.hideNavBar()



        transactionId = Prefs.getString("transactionId", "")
        if (transactionId.isNotEmpty()){
            paymentMethodId = 1
            implementChechOut(paymentMethodId,transactionId)

        }
    }

    private fun implementChechOut(paymentMethodId : Int,transactionId : String ) {


        // ye logic bhe kam ki hai. lkn is k lia model class variables same name k rakhny hoty hain request k lia
        /*String json= new Gson().toJson(productArrayList)
            Log.e("Json",json.toString() )*/

        // Product Order Object

        val params = JSONObject()

        val productArrayObject = JSONArray()
        val userDetailsArray = JSONArray()



        try {
            if (checkout.data.cartItem != null) {
                for (checkout in checkout.data.cartItem) {
                    val cartItem = JSONObject()
                    cartItem.put("ProductId", checkout.productId)
                    cartItem.put("Modifier1", checkout.modifier1)
                    cartItem.put("Modifier2", checkout.modifier2)
                    cartItem.put("Quantity", checkout.quantity)
                    productArrayObject.put(cartItem)
                }
            }
            val orderDetails = JSONObject()
            val userId = Prefs.getInt("userId",0)

            orderDetails.put("userId", userId.toString())
            orderDetails.put("AddressId", checkout.data.shippingAddress.get(0).addressId.toString())
            orderDetails.put("total", totalAmount)
            orderDetails.put("type", "product")

            if (paymentMethodId==0){
                orderDetails.put("PaymentMethodId", "0")
                orderDetails.put("transaction_id", transactionId)
            }else{
                orderDetails.put("PaymentMethodId", "1")
                orderDetails.put("transaction_id", transactionId)
            }

            userDetailsArray.put(orderDetails)


            params.put("OrderDetail",userDetailsArray)
            params.put("OrderProducts",productArrayObject)



        }catch (e : JSONException){
            fragmentCheckOutBinding.rootLayout.snackbar(e.localizedMessage)
        }


        proceedOrder(params.toString())



    }

    private fun proceedOrder(orderDetails: String) {

        WebServiceFactory.getInstance().proceedOrder(orderDetails).enqueue(object : Callback<PlaceOrder>{
            override fun onResponse(call: Call<PlaceOrder>, response: Response<PlaceOrder>) {

                if (response.body()!!.flag==1){
                    fragmentCheckOutBinding.rootLayout.snackbar(getString(R.string.your_order_has_been_placed))
                    checkout.data.cartItem.clear()
                    checkoutProductAdapter.notifyDataSetChanged()
                    fragmentCheckOutBinding.totalprice.setText("00.00 AED")

                    Prefs.putString("transactionId", "")

                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
                    navController.navigate(R.id.  orderPlaceFragment, null,options)


                }else{
                    fragmentCheckOutBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<PlaceOrder>, t: Throwable) {
                fragmentCheckOutBinding.rootLayout.snackbar(t.toString())
            }

        })

    }

    private fun getCheckOutData() {

        WebServiceFactory.getInstance().getCheckOutData(lang).enqueue(object : Callback<CheckOut> {
            override fun onResponse(call: Call<CheckOut>, response: Response<CheckOut>) {

                if (response.body()!!.flag==1){
                    checkout = response.body()!!
                    setData(response.body()!!)
                }else{
                    fragmentCheckOutBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<CheckOut>, t: Throwable) {
                fragmentCheckOutBinding.rootLayout.snackbar(t.toString())
            }

        })

    }

    private fun setData(checkout: CheckOut) {

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentCheckOutBinding.itemRecyclerview.setLayoutManager(layoutManager)

        checkoutProductAdapter = CheckoutProductAdapter(requireContext(),checkout.data.cartItem)
        fragmentCheckOutBinding.itemRecyclerview.adapter = checkoutProductAdapter


        if (checkout.data.shippingAddress.size>0){
//            fragmentCheckOutBinding.btnChangeAddress.visibility = View.VISIBLE

            fragmentCheckOutBinding.txtdeliveryAddressHouse.setText(checkout.data.shippingAddress.get(0).houseNo+", "+checkout.data.shippingAddress.get(0).streetNo)
            fragmentCheckOutBinding.txtdeliveryAddressCityProvince.setText(checkout.data.shippingAddress.get(0).city+", "+checkout.data.shippingAddress.get(0).state)
            fragmentCheckOutBinding.txtdeliveryAddressCityCountry.setText(checkout.data.shippingAddress.get(0).country)
        }else{
//            fragmentCheckOutBinding.btnChangeAddress.visibility = View.GONE
        }




        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
            totalAmount = 0
            for (i in 0 until checkout.data.cartItem.size) {
                totalAmount += checkout.data.cartItem[i].singleItemTotalAmount
            }

            fragmentCheckOutBinding.totalprice.setText(totalAmount.toString()+" AED")

        }, 500)

    }


}