package com.teamx.vevae.fragments.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.DeleteCartItem
import com.teamx.vevae.Models.UpdateQuantity
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.CartAdapter
import com.teamx.vevae.databinding.FragmentCartBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnCartClickLister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : BaseFragment(), OnCartClickLister {

    private lateinit var fragmentCartBinding: FragmentCartBinding
    private lateinit var  cartAdapter: CartAdapter
    private var quantity = 0
    private lateinit var cart: com.teamx.vevae.Models.cart.Cart
    private lateinit var options: NavOptions

    var total = 0f
    var newValue = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cart, container, false)
        fragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false)

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentCartBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentCartBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ENGLISH
        }




        fragmentCartBinding.btnCheckout.setOnClickListener{

            if (cart.data.size > 0 ){

                findNavController().navigate(R.id.checkOutFragment, null,options)
            }else{
                fragmentCartBinding.rootLayout.snackbar(getString(R.string.cart_is_empty))
            }

        }

        fragmentCartBinding.btnNotification.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.notificationFragment, null,options)
        })




        if (InternetConnection.checkConnection(requireContext())){
            getCartData();
        }else{
            fragmentCartBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
        }

        return fragmentCartBinding.root

    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity?)!!.showNavBar()
    }

    private fun getCartData() {

        WebServiceFactory.getInstance().getCartData(lang).enqueue(object : Callback<com.teamx.vevae.Models.cart.Cart> {
            override fun onResponse(
                call: Call<com.teamx.vevae.Models.cart.Cart>,
                response: Response<com.teamx.vevae.Models.cart.Cart>
            ) {
                if (response.code()==401){
                    showToast("Session Expried")
                    clearFragmentsLogout()
                }else{
                    if (response.body()!!.flag==1){

                        fragmentCartBinding.shimmerLayout.visibility = View.GONE
                        fragmentCartBinding.mainLayout.visibility = View.VISIBLE

                        cart = response.body()!!
                        if (cart.data != null && !cart.data.isEmpty()){
                            setData(response.body()!!)
                        }else{
                            fragmentCartBinding.rootLayout.snackbar(getString(R.string.cart_is_empty))
                        }


                    }else{
                        fragmentCartBinding.shimmerLayout.visibility = View.GONE
                        fragmentCartBinding.mainLayout.visibility = View.GONE
                        fragmentCartBinding.rootLayout.snackbar(response.body()!!.message)
                    }
                }

            }

            override fun onFailure(call: Call<com.teamx.vevae.Models.cart.Cart>, t: Throwable) {
                fragmentCartBinding.rootLayout.snackbar(t.toString())
            }

        })
    }

    private fun setData(cart: com.teamx.vevae.Models.cart.Cart) {




        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentCartBinding.cartRecyclerview.layoutManager = layoutManager


        cartAdapter = CartAdapter(requireContext(),cart.data,this)

        fragmentCartBinding.cartRecyclerview.adapter = cartAdapter




        Handler(Looper.getMainLooper()).postDelayed({
            total =0f;
            for (i in 0 until cart.data.size) {
                total += cart.data[i].totalAmount
            }

            fragmentCartBinding.totalprice.setText(total.toString()+" AED")
        }, 200)



    }

    override fun addProductClick(position: Int, itemCount: TextView?, productTotal: TextView?) {



        quantity = cart.data[position].quantity
        quantity++
        itemCount!!.text = quantity.toString()

        increaseItem(quantity,position,productTotal);
    }

    override fun subProductClick(position: Int, itemCount: TextView?, productTotal: TextView?) {
        quantity = cart.data[position].quantity

        if (quantity > 1){
            quantity--
            itemCount!!.text = quantity.toString()
            decreaseItem(quantity,position,productTotal);


        }else{
            fragmentCartBinding.rootLayout.snackbar(getString(R.string.not_allowed))


        }

    }


    private fun increaseItem(quantity: Int?, position: Int, productTotal: TextView?) {

        WebServiceFactory.getInstance().updateQuantity(cart.data[position].cartId, quantity!!).enqueue(object : Callback<UpdateQuantity>{
            override fun onResponse(
                call: Call<UpdateQuantity>,
                response: Response<UpdateQuantity>
            ) {
                if (response.body()!!.flag==1){
                    cart.data.get(position).quantity = quantity
                    fragmentCartBinding.rootLayout.snackbar(getString(R.string.quantity_updated))

                    //set single item total
                    cart.data[position].totalAmount =  cart.data[position].totalAmount + cart.data.get(position).price;

                    //show single item total in textView
                    productTotal!!.setText( cart.data[position].totalAmount.toString())

                    total = 0f
                      for (i in 0 until cart.data.size) {
                          total += cart.data[i].totalAmount
                      }

                    //calculate cart total for updated value
                    newValue =   total - cart.data.get(position).price;

                    //re-set total price
                    fragmentCartBinding.totalprice.setText("$total AED")

                }else{
                    fragmentCartBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<UpdateQuantity>, t: Throwable) {

            }

        })

    }

    private fun decreaseItem( quantity: Int?, position: Int, productTotal: TextView?) {

        WebServiceFactory.getInstance().updateQuantity(cart.data[position].cartId, quantity!!).enqueue(object : Callback<UpdateQuantity>{
            override fun onResponse(
                call: Call<UpdateQuantity>,
                response: Response<UpdateQuantity>
            ) {
                if (response.body()!!.flag==1){
                    cart.data.get(position).quantity = quantity
                    fragmentCartBinding.rootLayout.snackbar(getString(R.string.quantity_updated))

                    cart.data[position].totalAmount =  cart.data[position].totalAmount - cart.data.get(position).price;
                    productTotal!!.setText( cart.data[position].totalAmount.toString())
                    showToast(cart.data[position].totalAmount.toString())

                    //calculate cart total for updated value
                    newValue =   total - cart.data.get(position).price;
                 /*
                    for (i in 0 until cart.data.size) {
                        total += cart.data[i].totalAmount
                    }*/

                    //re-set total price
                    fragmentCartBinding.totalprice.setText("$total AED")

                }else{
                    fragmentCartBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<UpdateQuantity>, t: Throwable) {

            }

        })



    }

    override fun onProductClick(position: Int) {

    }

    override fun onDeleteProductClick(position: Int) {
        WebServiceFactory.getInstance().deleteCartItem(cart.data.get(position).cartId).enqueue(object : Callback<DeleteCartItem>{
            override fun onResponse(
                call: Call<DeleteCartItem>,
                response: Response<DeleteCartItem>
            ) {
                if (response.body()!!.flag==1){
                    cart.data.removeAt(position)
                    cartAdapter.notifyDataSetChanged()

                    fragmentCartBinding.rootLayout.snackbar(getString(R.string.item_deleted))
                }else{

                }
            }

            override fun onFailure(call: Call<DeleteCartItem>, t: Throwable) {

            }

        })

    }




}