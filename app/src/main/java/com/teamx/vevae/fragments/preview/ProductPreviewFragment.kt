package com.teamx.vevae.fragments.preview

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
import com.teamx.vevae.Models.addtocart.AddToCart
import com.teamx.vevae.Models.preview.Modifiers1
import com.teamx.vevae.Models.preview.Modifiers2
import com.teamx.vevae.Models.preview.ProductPreview
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.*
import com.teamx.vevae.databinding.FragmentProductPreviewBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnModifierOneClickListener
import com.teamx.vevae.onClick.OnModifierTwoClickListener
import com.google.android.material.tabs.TabLayoutMediator
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Utils.snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductPreviewFragment : BaseFragment(), OnModifierOneClickListener,
    OnModifierTwoClickListener {

    lateinit var fragmentProductPreviewBinding: FragmentProductPreviewBinding
    lateinit var productPreviewAdapter: ProductPreviewAdapter

    private lateinit var modifierOneAdapter: ModifierOneAdapter
    private lateinit var modifierTwoAdapter: ModifierTwoAdapter
    private lateinit var productPreview: ProductPreview
    private lateinit var options: NavOptions


    private  var modifiersName1: String = "";
    private  var modifiersName2: String= "";

    private var  productId : Int = 0
    lateinit var userToken :String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_product_preview, container, false)

        fragmentProductPreviewBinding = FragmentProductPreviewBinding.inflate(layoutInflater,container,false);


        (activity as MainActivity?)!!.hideNavBar()

        userToken = Prefs.getString("userToken", "");

        if (userToken.isNotEmpty()){
            fragmentProductPreviewBinding.btnCart.visibility = View.VISIBLE
            fragmentProductPreviewBinding.cartCount.visibility = View.VISIBLE

        }else{
            fragmentProductPreviewBinding.btnCart.visibility = View.GONE
            fragmentProductPreviewBinding.cartCount.visibility = View.GONE
        }


        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
            fragmentProductPreviewBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentProductPreviewBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))


        }else{
            lang = LocaleManager.LANGUAGE_ENGLISH
            fragmentProductPreviewBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentProductPreviewBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))


        }

        val bundle = arguments
        if (bundle != null){
            productId = bundle.getInt("id")

            if (InternetConnection.checkConnection(requireContext())){
                getProduct(productId);
            }else{
                fragmentProductPreviewBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
            }
        }else{
            if (InternetConnection.checkConnection(requireContext())){
                getProduct(productId);
            }else{
                fragmentProductPreviewBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
            }

        }

        fragmentProductPreviewBinding.btnAddToCart.setOnClickListener{

            userToken = Prefs.getString("userToken", "");

            if (userToken.isNotEmpty()){

                if (InternetConnection.checkConnection(requireContext())){
                    if (validate()){
                        addToCart();
                    }
                }else{
                    fragmentProductPreviewBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
                }
            }else{

                showLoginBottomSheet(R.id.productPreviewFragment,false);
            }

        }

        fragmentProductPreviewBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        fragmentProductPreviewBinding.btnCart.setOnClickListener{

            modifiersName1 = ""
            modifiersName2 = ""
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.cartFragment,null,options)

        }


        return fragmentProductPreviewBinding.root

    }

    private fun addToCart() {

        fragmentProductPreviewBinding.btnCart.visibility = View.VISIBLE
        fragmentProductPreviewBinding.cartCount.visibility = View.VISIBLE

         val productId = productPreview.data.product.id;
        if (productId > 0){
            WebServiceFactory.getInstance().addToCart(modifiersName1,modifiersName2,productId,1, lang).enqueue(object : Callback<AddToCart>{
                override fun onResponse(call: Call<AddToCart>, response: Response<AddToCart>) {
                    if (response.body()!!.flag==1){
                        fragmentProductPreviewBinding.rootLayout.snackbar(getString(R.string.add_to_cart))
                        productPreview.data.cart = response.body()!!.data.cartCount

                        if (productPreview.data.cart < 10){
                            fragmentProductPreviewBinding.cartCount.setText("0"+productPreview.data.cart.toString())
                        }else{
                            fragmentProductPreviewBinding.cartCount.setText(productPreview.data.cart.toString())
                        }

                    }else{
                        fragmentProductPreviewBinding.rootLayout.snackbar(response.body()!!.message)
                    }
                }

                override fun onFailure(call: Call<AddToCart>, t: Throwable) {

                }

            })
        }

    }


    private fun validate(): Boolean{

        if (modifiersName1.isEmpty()){
            fragmentProductPreviewBinding.rootLayout.snackbar("Select "+productPreview.data.modifierName1)
            return false
        }

        if (modifiersName2.isEmpty()){
            fragmentProductPreviewBinding.rootLayout.snackbar("Select "+productPreview.data.modifierName2)
            return false
        }

        return true;
    }

    private fun getProduct( productId : Int) {

        WebServiceFactory.getInstance().getProduct(productId,lang).enqueue(object : Callback<ProductPreview> {
            override fun onResponse(
                call: Call<ProductPreview>,
                response: Response<ProductPreview>
            ) {
                if (response.body()!!.flag == 1) {

                    fragmentProductPreviewBinding.shimmerLayout.visibility = View.GONE
                    fragmentProductPreviewBinding.mainLayout.visibility = View.VISIBLE
                    productPreview = response.body()!!
                    setData(response.body()!!)
                } else {
                    fragmentProductPreviewBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<ProductPreview>, t: Throwable) {}
        })


    }

    private fun setData(productPreview: ProductPreview) {

        fragmentProductPreviewBinding.productname.setText(productPreview.data.product.name)
        fragmentProductPreviewBinding.txtPrice.setText(productPreview.data.product.price.toString()+" AED")
        fragmentProductPreviewBinding.txtDecription.setText(productPreview.data.product.detail)

        if (productPreview.data.cart < 10){
            fragmentProductPreviewBinding.cartCount.setText("0"+productPreview.data.cart.toString())
        }else{
            fragmentProductPreviewBinding.cartCount.setText(productPreview.data.cart.toString())
        }


        if (productPreview.data.modifiers1 != null &&
            productPreview.data.modifiers2 != null){

            fragmentProductPreviewBinding.modifierName1.setText(productPreview.data.modifierName1)
            fragmentProductPreviewBinding.modifierName2.setText(productPreview.data.modifierName2)

            productPreviewAdapter = ProductPreviewAdapter(requireContext(),productPreview.data.productImages)
            fragmentProductPreviewBinding.viewpager.adapter = productPreviewAdapter

            TabLayoutMediator(fragmentProductPreviewBinding.tabLayout, fragmentProductPreviewBinding.viewpager) { tab, position ->
                tab.text = productPreview.data.productImages[position].toString()
            }.attach()


            modifierOne(productPreview.data.modifiers1)
            modifierTwo(productPreview.data.modifiers2)
        }else{
            showToast("error")
        }



    }



    private fun modifierOne(modifiers1: ArrayList<Modifiers1>) {



        modifierOneAdapter = ModifierOneAdapter(requireContext(),modifiers1,this)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)

        fragmentProductPreviewBinding.modifierOneRecyclerView.layoutManager   = layoutManager
        fragmentProductPreviewBinding.modifierOneRecyclerView.adapter = modifierOneAdapter

    }

    private fun modifierTwo(modifiers2: ArrayList<Modifiers2>) {



        modifierTwoAdapter = ModifierTwoAdapter(requireContext(),modifiers2,this)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)

        fragmentProductPreviewBinding.modifierTwoRecyclerView.layoutManager = layoutManager
        fragmentProductPreviewBinding.modifierTwoRecyclerView.adapter = modifierTwoAdapter

    }


    override fun onClickModifierOne(position: Int) {

        productPreview.data.modifiers1.get(position).isChecked = true

        modifiersName1 = productPreview.data.modifiers1.get(position).value

    }


    override fun onClickModifierTwo(position: Int) {

        productPreview.data.modifiers2.get(position).isChecked = true

        modifiersName2 = productPreview.data.modifiers2.get(position).value

    }



}
