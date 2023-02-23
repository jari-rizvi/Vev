package com.teamx.vevae.fragments.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.DefaultAddress
import com.teamx.vevae.Models.address.ShowAllAddress
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.adapter.AllAddressAdapter
import com.teamx.vevae.databinding.FragmentAllAddressBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnAddressListener
import com.vishalkumar.recyclerviewdummydata.RecyclerViewDummyData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAddressFragment : BaseFragment(), OnAddressListener {

    private lateinit var allAddressBinding: FragmentAllAddressBinding
    private lateinit var showAllAddress: ShowAllAddress
    private lateinit var allAddressAdapter: AllAddressAdapter

    private lateinit var recyclerViewDummyData: RecyclerViewDummyData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_all_address, container, false)
        allAddressBinding = FragmentAllAddressBinding.inflate(inflater,container,false)

      /*  recyclerViewDummyData = RecyclerViewDummyData(requireContext())
        recyclerViewDummyData.setDummyData(allAddressBinding.allAddressRecyclerview, R.layout.item_all_address)*/


        allAddressBinding.btnBack.setOnClickListener {

            popUpStack()
        }



        if (InternetConnection.checkConnection(requireContext())){

            getAllAddress()

        }else{
            allAddressBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
        }



        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            allAddressBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            allAddressBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            allAddressBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            allAddressBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH

        }



        allAddressBinding.btnAddNewAddress.setOnClickListener{
            navBuilder.setEnterAnim(R.anim.slide_in_left)
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.shippingAddressFragment, null,navBuilder.build())
        }


        return  allAddressBinding.root
    }

    private fun getAllAddress() {

        WebServiceFactory.getInstance().getAllAddress(lang).enqueue(object : Callback<ShowAllAddress> {
            override fun onResponse(
                call: Call<ShowAllAddress>,
                response: Response<ShowAllAddress>
            ) {

                if (response.body()!!.flag==1){
                    showAllAddress = response.body()!!
                    setData(showAllAddress)
                }else{
                    allAddressBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<ShowAllAddress>, t: Throwable) {
            }

        })
    }

    private fun setData(showAllAddress: ShowAllAddress) {

        if (showAllAddress.data.address.size > 0){


//            val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//            allAddressBinding.allAddressRecyclerview.layoutManager= linearLayoutManager;

            allAddressAdapter = AllAddressAdapter(requireContext(),showAllAddress.data.address,this)
            allAddressBinding.allAddressRecyclerview.adapter = allAddressAdapter

        }

    }

    override fun onAddressClick(position: Int) {


    }

    override fun onDefaultClick(position: Int) {

        WebServiceFactory.getInstance().setDefaultAddress(showAllAddress.data.address.get(position).addressId,1).enqueue(object :Callback<DefaultAddress>{
            override fun onResponse(
                call: Call<DefaultAddress>,
                response: Response<DefaultAddress>) {
                if (response.body()!!.flag==1){
                    allAddressBinding.rootLayout.snackbar(getString(R.string.default_address_updated))
                }else{
                    allAddressBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<DefaultAddress>, t: Throwable) {
                allAddressBinding.rootLayout.snackbar(t.toString())
            }
        })


    }

}


