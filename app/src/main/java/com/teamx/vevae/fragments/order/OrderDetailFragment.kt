package com.teamx.vevae.fragments.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamx.vevae.App.App
import com.teamx.vevae.DummyModel.OrderDetails
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.adapter.OrderDetailAdapter
import com.teamx.vevae.databinding.FragmentOrderDetailBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailFragment : BaseFragment() {

    private lateinit var fragmentOrderDetailBinding: FragmentOrderDetailBinding
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private var orderId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_order_detail, container, false)

        fragmentOrderDetailBinding = FragmentOrderDetailBinding.inflate(inflater,container,false)

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentOrderDetailBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            fragmentOrderDetailBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        val bundle =  arguments
        if (bundle != null){

            orderId = bundle.getInt("orderId")
        }

        if (InternetConnection.checkConnection(requireContext())){
            getOrderDetails();
        }else{
            fragmentOrderDetailBinding.rootLayout.snackbar(getString(R.string.please_check_your_internet_connection))
        }


        fragmentOrderDetailBinding.btnBack.setOnClickListener{

            popUpStack()
        }



        return fragmentOrderDetailBinding.root
    }

    private fun getOrderDetails() {
        WebServiceFactory.getInstance().getOrderDetails(orderId,lang).enqueue(object :
            Callback<com.teamx.vevae.Models.orderDetails.OrderDetails> {
            override fun onResponse(call: Call<com.teamx.vevae.Models.orderDetails.OrderDetails>, response: Response<com.teamx.vevae.Models.orderDetails.OrderDetails>) {
                if (response.body()!!.flag==1){
                    setData(response.body()!!);
                }else{
                    fragmentOrderDetailBinding.rootLayout.snackbar(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<com.teamx.vevae.Models.orderDetails.OrderDetails>, t: Throwable) {


            }
        })
    }


    private fun setData(body: com.teamx.vevae.Models.orderDetails.OrderDetails) {

        fragmentOrderDetailBinding.orderId.setText("id #"+body.data.orderDetail.get(0).orderCode.toString())
        if (body.data.address != null && !body.data.address.isEmpty()  ){

            fragmentOrderDetailBinding.txtdeliveryAddress.setText(body.data.address.get(0).houseNo+", "+body.data.address.get(0).streetNo+"\n"+body.data.address.get(0).city+", "+body.data.address.get(0).state+"\n"+body.data.address.get(0).country)
            fragmentOrderDetailBinding.txtdeliveryAddress.visibility =View.VISIBLE
            fragmentOrderDetailBinding.dileverTo.visibility = View.VISIBLE

        }else{
            fragmentOrderDetailBinding.txtdeliveryAddress.visibility = View.GONE
            fragmentOrderDetailBinding.txtdeliveryAddress.visibility =View.GONE
            fragmentOrderDetailBinding.dileverTo.visibility = View.GONE

        }

        if (body.data.orderDetail.get(0).orderDate != null){

            fragmentOrderDetailBinding.orderDate.setText(body.data.orderDetail.get(0).orderDate)

        }else{

            fragmentOrderDetailBinding.orderDate.setText("")

        }

        if (body.data.orderDetail[0].paymentMethodId.equals("0")){
            fragmentOrderDetailBinding.payemntMethodHeading.setText(getString(R.string.payment_methhod))
            fragmentOrderDetailBinding.payemntMethodType.setText(getString(R.string.cash_on_delivery))

        }else{
            fragmentOrderDetailBinding.payemntMethodHeading.setText(getString(R.string.transactionId))
            fragmentOrderDetailBinding.payemntMethodType.setText(body.data.orderDetail[0].transaction_id)

        }



        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentOrderDetailBinding.itemRecyclerview.layoutManager = layoutManager

        orderDetailAdapter = OrderDetailAdapter(requireContext(),body.data.orderitem)

        fragmentOrderDetailBinding.itemRecyclerview.adapter = orderDetailAdapter

/*

        var totalAmount =0

        for (i in 0 until body.data.orderitem.size) {
            totalAmount += body.data.orderitem[i].price
        }

        fragmentOrderDetailBinding.totalprice.setText(totalAmount.toString()+" AED")
*/

        fragmentOrderDetailBinding.totalprice.setText(body.data.orderitem.get(0).total.toString()+" AED")
    }

}