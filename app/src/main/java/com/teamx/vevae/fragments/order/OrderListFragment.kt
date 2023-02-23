package com.teamx.vevae.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamx.vevae.App.App
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.OrderListAdapter
import com.teamx.vevae.databinding.FragmentOrderListBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnOrderClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderListFragment : BaseFragment(), OnOrderClickListener {


    private lateinit var fragmentOrderListBinding: FragmentOrderListBinding
    private lateinit var  orderListAdapter: OrderListAdapter
    private lateinit var orderList : com.teamx.vevae.Models.orderList.OrderList


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_order_list, container, false)

        fragmentOrderListBinding = FragmentOrderListBinding.inflate(inflater,container,false)




        if (InternetConnection.checkConnection(requireContext())){
            getOrderList();
        }else{
            fragmentOrderListBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
            fragmentOrderListBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))

        }else{
            fragmentOrderListBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }



        fragmentOrderListBinding.btnBack.setOnClickListener{

            popUpStack()

        }


        return fragmentOrderListBinding.root
    }

    private fun getOrderList() {

        progressbar("Orders","Please wait")
        WebServiceFactory.getInstance().getOrderList(lang).enqueue(object :
            Callback<com.teamx.vevae.Models.orderList.OrderList> {
            override fun onResponse(
                call: Call<com.teamx.vevae.Models.orderList.OrderList>,
                response: Response<com.teamx.vevae.Models.orderList.OrderList>
            ) {
                if (response.body()!!.flag==1){
                    mProgressBar.dismiss()
                    orderList = response.body()!!
                    setData(response.body()!!)
                }else{
                    fragmentOrderListBinding.rootLayout.snackbar(response.body()!!.message)
                    mProgressBar.dismiss()
                }
            }

            override fun onFailure(
                call: Call<com.teamx.vevae.Models.orderList.OrderList>,
                t: Throwable
            ) {
                mProgressBar.dismiss()
            }

        })
    }

    private fun setData(body: com.teamx.vevae.Models.orderList.OrderList) {

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentOrderListBinding.orderRecyclerview.layoutManager = layoutManager

        orderListAdapter = OrderListAdapter(requireContext(),body.data,this)
        fragmentOrderListBinding.orderRecyclerview.adapter = orderListAdapter
    }

    override fun onItemClickListener(position: Int) {

        showToast(""+ orderList.data[position].orderId)

        val bundle = Bundle()
        bundle.putInt("orderId",orderList.data.get(position).orderId)

        navBuilder.setEnterAnim(R.anim.slide_in)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
        navController.navigate(R.id.orderDetailFragment, bundle,navBuilder.build())


    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).hideNavBar()
    }


}