package com.teamx.vevae.fragments.navfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.vevae.App.App
import com.teamx.vevae.DummyModel.NotificationDummy
import com.teamx.vevae.Models.notification.AllNotification
import com.teamx.vevae.Models.updatednotification.Notifications
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.adapter.NotificationAdapter
import com.teamx.vevae.databinding.FragmentNotificationBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.teamx.vevae.onClick.OnOrderClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationFragment : BaseFragment(), OnOrderClickListener {

    private lateinit var fragmentNotificationBinding: FragmentNotificationBinding
    private  lateinit var notificationAdapter: NotificationAdapter
    private lateinit var arrayList: ArrayList<NotificationDummy>
    private lateinit var boldWordsList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notification, container, false)

        fragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater,container,false)
//        showBottomNav()

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentNotificationBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            fragmentNotificationBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        if (InternetConnection.checkConnection(requireContext())){
            progressbar(getString(R.string.notification),getString(R.string.please_wait))
            WebServiceFactory.getInstance().getAllNotification(lang).enqueue(object : Callback<Notifications> {
                override fun onResponse(call: Call<Notifications>, response: Response<Notifications>) {
                    if (response.body()!!.flag==1){
                        setData(response.body()!!)
                        mProgressBar.dismiss()
                    }else{
//                        fragmentNotificationBinding.rootLayout.snackbar(response.body()!!.message)
                        mProgressBar.dismiss()
                    }
                }

                override fun onFailure(call: Call<Notifications>, t: Throwable) {
                    mProgressBar.dismiss()
                }

            })
        }else{

            showInternetToast()
        }

        fragmentNotificationBinding.btnBack.setOnClickListener {
            popUpStack()
        }



        return fragmentNotificationBinding.root
    }

    private fun setData(body: Notifications) {

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        fragmentNotificationBinding.notificationRecyclerview.layoutManager = layoutManager

        notificationAdapter = NotificationAdapter(requireContext(),body.data,this)
        fragmentNotificationBinding.notificationRecyclerview.adapter  = notificationAdapter

    }

    override fun onItemClickListener(position: Int) {
//        arrayList[position].isOpened = false
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.showNavBar()
    }

}