package com.teamx.vevae.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.DummyModel.PaymentMethod
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.adapter.PaymentMethodAdapter
import com.teamx.vevae.databinding.FragmentPaymentMethodBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnPaymentMethodClickListener


class PaymentMethodFragment : BaseFragment(), OnPaymentMethodClickListener {


    lateinit var fragmentPaymentMethodBinding: FragmentPaymentMethodBinding
    private lateinit var paymentMethodArrayList: ArrayList<PaymentMethod>
    private lateinit var paymentMethodAdapter : PaymentMethodAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_payment_method, container, false)
        fragmentPaymentMethodBinding = FragmentPaymentMethodBinding.inflate(inflater,container,false)

//        hideBottomNav()

        fragmentPaymentMethodBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        fragmentPaymentMethodBinding.btnUpdateMethod.setOnClickListener {

            popUpStack()
        }

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentPaymentMethodBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentPaymentMethodBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            lang = LocaleManager.LANGUAGE_ENGLISH
            fragmentPaymentMethodBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentPaymentMethodBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))


        }



        paymentMethodArrayList = ArrayList()

        paymentMethodArrayList.add(PaymentMethod(1,R.drawable.icon_master,getString(R.string.debit_credit)))
        paymentMethodArrayList.add(PaymentMethod(3,R.drawable.icon_cash,getString(R.string.cash)))


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentPaymentMethodBinding.paymentMethodRecyclerview.layoutManager = layoutManager

        paymentMethodAdapter = PaymentMethodAdapter(requireContext(),paymentMethodArrayList,this)
        fragmentPaymentMethodBinding.paymentMethodRecyclerview.adapter = paymentMethodAdapter

        return fragmentPaymentMethodBinding.root
    }

    override fun onPaymentClick(position: Int) {

        showToast(""+ paymentMethodArrayList[position].paymentName+ getString(R.string.selected))

        Prefs.putString("paymentMethod",paymentMethodArrayList[position].paymentName)

    }

}