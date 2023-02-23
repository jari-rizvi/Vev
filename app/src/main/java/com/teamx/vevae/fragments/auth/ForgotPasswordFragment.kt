package com.teamx.vevae.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.SignUp.ValidateUser
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.databinding.FragmentForgotPasswordBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordFragment : BaseFragment() {

    private  lateinit var  fragmentForgotPasswordBinding: FragmentForgotPasswordBinding
    private   lateinit  var emailPhone : String
    private lateinit var options: NavOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_forgot_password, container, false)

        fragmentForgotPasswordBinding = FragmentForgotPasswordBinding.inflate(inflater,container,false)

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
            fragmentForgotPasswordBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_screen_back,
                    requireActivity().theme
                )
            )
            fragmentForgotPasswordBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_button_back,
                    requireActivity().theme
                )
            )

            lang = LocaleManager.LANGUAGE_ARABIC
        } else {
            fragmentForgotPasswordBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.back,
                    requireActivity().theme
                )
            )
            fragmentForgotPasswordBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.icon_arrow,
                    requireActivity().theme
                )
            )

            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        fragmentForgotPasswordBinding.btnSendCode.setOnClickListener {

            if (fragmentForgotPasswordBinding.editPhoneMail.text.toString().isNotEmpty()){
                if (InternetConnection.checkConnection(requireContext())){
                    validateUserData()
                }else{
                    showInternetToast()
                }
            }else{
                showToast(getString(R.string.invalid_number))
            }


        }


        fragmentForgotPasswordBinding.btnBack.setOnClickListener {


            popUpStack()
        }



        return fragmentForgotPasswordBinding.root
    }

    private fun validateUserData() {

        progressbar("Check user","Please wait...")

       emailPhone = fragmentForgotPasswordBinding.editPhoneMail.text.toString()

        if (emailPhone.contains("@")){
            callApi();
        }else{
            if (emailPhone.startsWith("+")){
                callApi();
            }else{
                fragmentForgotPasswordBinding.rootLayout.snackbar(getString(R.string.country_code_971))
            }

        }


    }

    private fun callApi() {

        WebServiceFactory.getInstance().validateUserForgotPassword(emailPhone,lang).enqueue(object : Callback<ValidateUser>{

            override fun onResponse(call: Call<ValidateUser>, response: Response<ValidateUser>) {

                if (response.body()!!.getFlag()==1){

                    sendOTP()


                }else{
                    showToast(response.body()!!.message)
                    mProgressBar.dismiss()
                }
            }

            override fun onFailure(call: Call<ValidateUser>, t: Throwable) {
                mProgressBar.dismiss()
            }
        })
    }

    private fun sendOTP() {

        val bundle = Bundle()

        bundle.putString("phone",emailPhone)
        bundle.putString("forgot","forgot")
        Prefs.putString("emailPhone",emailPhone)
        mProgressBar.dismiss()

        if (emailPhone.contains("@")){
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.emailOTPFragment,bundle,options)
        }else{
            //email verification screen
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.OTPFragment,bundle,options)
        }
    }

}