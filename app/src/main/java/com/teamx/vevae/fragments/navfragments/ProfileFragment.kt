package com.teamx.vevae.fragments.navfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.Logout
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.databinding.FragmentProfileBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.Models.Login.Login
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : BaseFragment() {

    private lateinit var options: NavOptions
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private var deviceToken : String = ""

    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater,container,false)

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        guestUser();

        checkLanguage();

        setUserProfile();

        fragmentProfileBinding.btnSignIn.setOnClickListener{
//         showLoginBottomSheet(R.id.loginFragment,false);
//         popUpStack()
            showLogin(R.id.loginFragment,false);

        }

        fragmentProfileBinding.btnNotification.setOnClickListener{
            findNavController().navigate(R.id.notificationFragment, null,options)
        }

        fragmentProfileBinding.btnNotification.setOnClickListener(View.OnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.notificationFragment, null,options)
        })


        fragmentProfileBinding.btnAllMyOrder.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                findNavController().navigate(R.id.orderListFragment, null,options)
            }else{
                showInternetToast()
            }

        }

        fragmentProfileBinding.btnSettings.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                findNavController().navigate(R.id.settingsFragment, null,options)
            }else{
                showInternetToast()
            }
        }

      /*  fragmentProfileBinding.btnCarBooking.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                //code
            }else{
                showInternetToast()
            }

        }

        fragmentProfileBinding.btnCustomerSupport.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                //code
            }else{
                showInternetToast()
            }
        }*/

        fragmentProfileBinding.btnPaymentMethod.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                findNavController().navigate(R.id.paymentMethodFragment, null,options)
            }else{
                showInternetToast()
            }
        }

        fragmentProfileBinding.btnLogout.setOnClickListener{
            if (InternetConnection.checkConnection(requireContext())){
                logout()
            }else{
                showInternetToast()
            }
        }

        return fragmentProfileBinding.root
    }

    private fun setUserProfile() {

        val  userImage = Prefs.getString("userImage","")
        val  userEmail = Prefs.getString("userEmail","")
        val  userName = Prefs.getString("userName","")

        imageLoaderPicasso(userImage,fragmentProfileBinding.profilePicture)
        fragmentProfileBinding.username.text = userName
        fragmentProfileBinding.userEmail.text = userEmail

    }

    private fun checkLanguage() {

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentProfileBinding.orderBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentProfileBinding.paymentBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentProfileBinding.settingBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))

            fragmentProfileBinding.signInBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
//            fragmentProfileBinding.supportBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentProfileBinding.logoutBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }
        else{
            fragmentProfileBinding.orderBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentProfileBinding.paymentBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentProfileBinding.settingBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
//            fragmentProfileBinding.carBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
//            fragmentProfileBinding.supportBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentProfileBinding.logoutBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentProfileBinding.signInBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ENGLISH
        }
    }

    private fun guestUser() {

        val userToken = Prefs.getString("userToken", "")
        if (userToken.isNotEmpty()) {
            fragmentProfileBinding.guestUser.setVisibility(View.GONE)
            fragmentProfileBinding.btnSignIn.setVisibility(View.GONE)

            fragmentProfileBinding.userEmail.setVisibility(View.VISIBLE)
            fragmentProfileBinding.btnAllMyOrder.setVisibility(View.VISIBLE)
            fragmentProfileBinding.btnPaymentMethod.setVisibility(View.VISIBLE)
//            fragmentProfileBinding.btnCarBooking.setVisibility(View.VISIBLE)
            fragmentProfileBinding.btnLogout.setVisibility(View.VISIBLE)
            fragmentProfileBinding.btnNotification.setVisibility(View.VISIBLE)

            fragmentProfileBinding.view1.setVisibility(View.VISIBLE)
            fragmentProfileBinding.view2.setVisibility(View.VISIBLE)
            fragmentProfileBinding.signInBack.setVisibility(View.GONE)
//            fragmentProfileBinding.view4.setVisibility(View.VISIBLE)





        }
        else {
            fragmentProfileBinding.guestUser.setVisibility(View.VISIBLE)
            fragmentProfileBinding.profilePicture.setImageResource(R.mipmap.ic_launcher)
            fragmentProfileBinding.btnSignIn.setVisibility(View.VISIBLE)
            fragmentProfileBinding.signInBack.setVisibility(View.VISIBLE)

            fragmentProfileBinding.userEmail.setVisibility(View.GONE)
            fragmentProfileBinding.btnAllMyOrder.setVisibility(View.GONE)
            fragmentProfileBinding.btnPaymentMethod.setVisibility(View.GONE)

            fragmentProfileBinding.btnLogout.setVisibility(View.GONE)
            fragmentProfileBinding.btnNotification.setVisibility(View.GONE)



            fragmentProfileBinding.view1.setVisibility(View.GONE)
            fragmentProfileBinding.view2.setVisibility(View.GONE)
//            fragmentProfileBinding.view4.setVisibility(View.GONE)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.showNavBar()

    }
    private fun logout() {
        progressbar("Logout","Please wait...")

        WebServiceFactory.getInstance().logout().enqueue(object : Callback<Logout>{

            override fun onResponse(call: Call<Logout>, response: Response<Logout>) {
                if (response.code()==200){
                    if (response.body()!!.flag==1){
                        clearFragmentsLogout()
                        mProgressBar.dismiss()
                    }
                    else{
                        mProgressBar.dismiss()
                        showToast(response.body()!!.message)
                    }
                }else if (response.code()==401){
                    showToast(getString(R.string.session_expired))
                    clearFragmentsLogout()
                }
               
            }

            override fun onFailure(call: Call<Logout>, t: Throwable) {
                mProgressBar.dismiss()
            }
        })
    }


     fun showLogin(fragmentId: Int, isNavigate: Boolean) {
         FirebaseMessaging.getInstance().token
             .addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                     return@OnCompleteListener
                 }

                 // Get new FCM registration token
                 deviceToken = task.result
             })
        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_login)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        val userEmail = bottomSheetDialog.findViewById<EditText>(R.id.edit_email)
        val userPassword = bottomSheetDialog.findViewById<EditText>(R.id.edit_password)
        val btnLogin = bottomSheetDialog.findViewById<CardView>(R.id.btnLogin)
        val btnCross = bottomSheetDialog.findViewById<ImageView>(R.id.btnCross)
        btnCross!!.setOnClickListener { bottomSheetDialog.dismiss() }
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                deviceToken = task.result
            })
        btnLogin!!.setOnClickListener {
            if (validate(userEmail!!.text.toString(),userPassword!!.text.toString())) {
                if (InternetConnection.checkConnection(requireContext())) {
                    loginProfile(
                        userEmail!!.text.toString(),
                        userPassword!!.text.toString(),
                        deviceToken,
                        lang,
                        fragmentId,
                        isNavigate
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Check Your internet conection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Kindly enter valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Kindly enter password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginProfile(
        userEmail: String,
        userPassword: String,
        deviceToken: String,
        lang : String,
        fragmentId: Int,
        isNavigate: Boolean
    ) {
        WebServiceFactory.getInstance().loginUser(userEmail, userPassword, deviceToken,lang)
            .enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.body()!!.flag == 1) {
                        Prefs.putString("userToken", response.body()!!.data.token)
                        Prefs.putString("userName", response.body()!!.data.user.name)
                        Prefs.putString("userEmail", response.body()!!.data.user.email)
                        Prefs.putString("userPhone", response.body()!!.data.user.phone)
                        Prefs.putString("userImage", response.body()!!.data.user.image)
                        Prefs.putInt("userId", response.body()!!.data.user.id)
                        guestUser()
                        setUserProfile()
                        bottomSheetDialog.dismiss()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        bottomSheetDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    bottomSheetDialog.dismiss()
                }
            })
    }


}