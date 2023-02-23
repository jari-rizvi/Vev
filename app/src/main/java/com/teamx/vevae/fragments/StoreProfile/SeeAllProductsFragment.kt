package com.teamx.vevae.fragments.StoreProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.AllCategories.AllCategories
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ARABIC
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.adapter.AllCategoriesAdapter
import com.teamx.vevae.databinding.FragmentSeeAllProductsBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnOrderClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SeeAllProductsFragment : BaseFragment(), OnOrderClickListener {


    lateinit var fragmentSeeAllProductsBinding: FragmentSeeAllProductsBinding
    lateinit var allCategoryAdapter: AllCategoriesAdapter
    lateinit var allCategories: AllCategories
    private lateinit var options: NavOptions
    private var  categoryId = 0
    private var storeId = 0
    private var type : String? = null
    private var categoryname : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_see_all_products, container, false)
        fragmentSeeAllProductsBinding = FragmentSeeAllProductsBinding.inflate(inflater,container,false)

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }



        if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {

            lang = LANGUAGE_ARABIC
            fragmentSeeAllProductsBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_screen_back,
                    requireActivity().theme
                )
            )

        } else {
            lang = LANGUAGE_ENGLISH
            fragmentSeeAllProductsBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.back,
                    requireActivity().theme
                )
            )
        }

        val  bundle = arguments
        if (bundle != null){
            categoryId   = bundle.getInt("categoryId")
            storeId   = bundle.getInt("storeId")
            type = bundle.getString("type")
            categoryname = bundle.getString("categoryName")

            fragmentSeeAllProductsBinding.categoryName.setText(categoryname)
            if (InternetConnection.checkConnection(requireContext())){
                getAllData()
            }else{
                showInternetToast()
            }
        }

        fragmentSeeAllProductsBinding.btnBack.setOnClickListener {

            popUpStack()
        }




        return fragmentSeeAllProductsBinding.root

    }

    private fun getAllData() {

        WebServiceFactory.getInstance().getAllItemsByCategory(categoryId,storeId, type,lang).enqueue(object :
            Callback<AllCategories> {
            override fun onResponse(call: Call<AllCategories>, response: Response<AllCategories>) {
                if (response.body()!!.flag==1){
                    allCategories = response.body()!!
                    setData(response.body()!!)
                }else{
//                    fragmentSeeAllProductsBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<AllCategories>, t: Throwable) {
                fragmentSeeAllProductsBinding.rootLayout.snackbar(t.toString())
            }

        })
    }

    private fun setData(body: AllCategories) {

        val  gridLayout = GridLayoutManager(requireContext(),3,RecyclerView.VERTICAL,false)
        allCategoryAdapter = AllCategoriesAdapter(context,body.data.category.listbycategory,this)

        fragmentSeeAllProductsBinding.itemRecyclerview.layoutManager = gridLayout
        fragmentSeeAllProductsBinding.itemRecyclerview.adapter = allCategoryAdapter
    }

    override fun onItemClickListener(position: Int) {

        val bundle = Bundle()
        bundle.putInt("id",allCategories.data.category.listbycategory.get(position).id)
       if (allCategories.data.category.type.contains("product")){

           navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
           navController.navigate(R.id.  productPreviewFragment, bundle,options)

       }else{
           navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
           navController.navigate(R.id.  servicesFragment, bundle,options)
       }


    }


}