package com.teamx.vevae.fragments.StoreProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.vevae.Models.storeprofile.Data
import com.teamx.vevae.Models.storeprofile.StoreProfile
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.ShareViewModel.ShareViewModel
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.StoreHomeAdapter
import com.teamx.vevae.databinding.FragmentStoreHomeBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnOrderClickListener
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.Utils.localization.LocaleManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreHomeFragment : BaseFragment(), OnOrderClickListener {


    lateinit var binding: FragmentStoreHomeBinding
    lateinit var  storeHomeAdapter: StoreHomeAdapter
    private lateinit var options: NavOptions
    private  var storeId : Int=0;
    private  lateinit var storeType : String
    private lateinit var data: Data


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_store_home, container, false)
        binding = FragmentStoreHomeBinding.inflate(inflater,container,false)

         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
        } else {
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        storeId = Prefs.getInt("id",0)
        storeType = Prefs.getString("storeType","")


        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)


        WebServiceFactory.getInstance().getStoreDetails(storeId,storeType,lang).enqueue(object :
            Callback<StoreProfile> {
            override fun onResponse(call: Call<StoreProfile>, response: Response<StoreProfile>) {
                if (response.body()!!.flag==1){
                    setData(response.body()!!.data)

                }else{

                }
            }

            override fun onFailure(call: Call<StoreProfile>, t: Throwable) {

            }

        })





        return binding.root

    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity?)!!.hideNavBar()
    }


    private fun setData(it: Data?) {

        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.homeRecyclerView.setLayoutManager(linearLayoutManager)

        storeHomeAdapter = StoreHomeAdapter(requireContext(), it!!.category,this)
        binding.homeRecyclerView.adapter = storeHomeAdapter
        data = it

    }

    override fun onItemClickListener(position: Int) {

        val bundle = Bundle()
        bundle.putInt("categoryId", data.category.get(position).categoriesId)
        bundle.putString("type", data.category.get(position).type)
        bundle.putString("categoryName",data.category.get(position).categoriesName)
        bundle.putInt("storeId",storeId)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
        navController.navigate(R.id.seeAllProductsFragment, bundle,options)


    }

}