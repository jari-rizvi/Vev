package com.teamx.vevae.fragments.StoreProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.storeprofile.StoreProfile
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.ShareViewModel.ShareViewModel
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.ProfilePagerAdapter
import com.teamx.vevae.databinding.FragmentStoreProfileBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Utils.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreProfileFragment : BaseFragment() {

    lateinit var fragmentStoreProfileBinding: FragmentStoreProfileBinding
    lateinit var profilePagerAdapter: ProfilePagerAdapter
    lateinit var viewpager : ViewPager2
    lateinit var shareViewModel :ShareViewModel


    private lateinit var options: NavOptions


    lateinit var storeProfile: StoreProfile
    var tabsName = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_store_profile, container, false)

        fragmentStoreProfileBinding = FragmentStoreProfileBinding.inflate(inflater,container,false)

        (activity as MainActivity).hideNavBar()

        shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)

        val bundle = arguments

        if (bundle != null){
            val storeId = bundle.getInt("id")
            val storeType = bundle.getString("storeType")

            getStoreDetails( storeId,storeType);
        }



        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        fragmentStoreProfileBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        fragmentStoreProfileBinding.btnSearch.setOnClickListener{

            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.seeAllProductsFragment,bundle,options)
        }


        fragmentStoreProfileBinding.btnSearch.setOnClickListener{

            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.searchFragment, bundle)



        }

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentStoreProfileBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_back_white, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentStoreProfileBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_back_white, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }




        return fragmentStoreProfileBinding.root

    }

    private fun tabLayout() {

        tabsName = ArrayList();
        tabsName.add(getString(R.string.home))
        tabsName.add(getString(R.string.review))

        profilePagerAdapter = ProfilePagerAdapter(requireActivity())
        fragmentStoreProfileBinding.viewpager.adapter = profilePagerAdapter

        TabLayoutMediator(
            fragmentStoreProfileBinding.tabLayout,
            fragmentStoreProfileBinding.viewpager
        ) { tab, position ->
            tab.text =tabsName[position]
            fragmentStoreProfileBinding.viewpager.setCurrentItem(tab.position, true)
        }.attach()

    }

    private fun getStoreDetails(storeId: Int, storeType: String?) {


        WebServiceFactory.getInstance().getStoreDetails(storeId,storeType,lang).enqueue(object :
            Callback<StoreProfile> {
            override fun onResponse(call: Call<StoreProfile>, response: Response<StoreProfile>) {
                if (response.body()!!.flag==1){
                    storeProfile = response.body()!!
                    setData(storeProfile)

                    shareViewModel.setStoreProfileData(response.body()!!.data)
                    Log.e("StoreData", storeProfile.data.toString() )


                }else{
                    fragmentStoreProfileBinding.rootLayout.snackbar(response.body()!!.message)
                }



            }

            override fun onFailure(call: Call<StoreProfile>, t: Throwable) {

            }
        })


    }

    private fun setData(storeProfile: StoreProfile) {

        if (storeProfile != null){
            imageLoaderPicasso(storeProfile.data.stores.banner,fragmentStoreProfileBinding.storeProfilePicture)
            fragmentStoreProfileBinding.storeTitle.setText(storeProfile.data.stores.storeName)
            fragmentStoreProfileBinding.storeTagLine.setText(storeProfile.data.stores.storedetail)
        }


        tabLayout();
    }


}