package com.teamx.vevae.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.teamx.vevae.Models.Login.Login
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.databinding.FragmentLoginBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.messaging.FirebaseMessaging




class LoginFragment : BaseFragment() {

    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var token : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_login, container, false)

        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater,container,false)

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        fragmentLoginBinding.continueAsGuest.setOnClickListener {

            clearActivityStack()
        }

         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {

            fragmentLoginBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_button_back,
                    requireActivity().theme
                )
            )
            lang = LocaleManager.LANGUAGE_ARABIC
        }
        else {
            fragmentLoginBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.icon_arrow,
                    requireActivity().theme
                )
            )
            lang = LocaleManager.LANGUAGE_ENGLISH
        }


        fragmentLoginBinding.btnLogin.setOnClickListener{

            if (validate()){
                if (InternetConnection.checkConnection(requireContext())){
                    login()
                }else{
                    showInternetToast()
                }
            }
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                token = task.result

            })


        fragmentLoginBinding.forgotPassowrd.setOnClickListener{
            findNavController().navigate(R.id.forgotPasswordFragment, null,options)
        }

        fragmentLoginBinding.btnSignUp.setOnClickListener{
            findNavController().navigate(R.id.signUpFragment, null,options)
        }
        return fragmentLoginBinding.root
    }

    private fun login() {



        progressbar("Login","Please wait...")
        WebServiceFactory.getInstance().loginUser(fragmentLoginBinding.editEmail.text.toString(),
            fragmentLoginBinding.editPassword.text.toString(),token,lang)
            .enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {


                    if (response.body()!!.flag==1){
                        showToast(getString(R.string.logged_in))
                        mProgressBar.dismiss()



                        Prefs.putString("userToken", response.body()!!.data.token)
                        Prefs.putString("userName", response.body()!!.data.user.name)
                        Prefs.putString("userEmail", response.body()!!.data.user.email)
                        Prefs.putString("userPhone", response.body()!!.data.user.phone)
                        Prefs.putString("userImage", response.body()!!.data.user.image)
                        Prefs.putInt("userId", response.body()!!.data.user.id)

                        clearActivityStack()


                    }else{
                      /*  val error = response.body()!!.message.split(".")
                        showToast(error[0])*/
                            showToast(response.body()!!.message)
                        mProgressBar.dismiss()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                }
            })

    }

    private fun validate(): Boolean {

        if (fragmentLoginBinding.editEmail.text.toString().isEmpty()){
            showToast(getString(R.string.enter_valid_email))
            return false
        }

        if (fragmentLoginBinding.editPassword.text.toString().isEmpty()){
            showToast(getString(R.string.please_enter_password))
            return false
        }

        return true
    }

}