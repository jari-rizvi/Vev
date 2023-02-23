package com.teamx.vevae.fragments.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.SignUp.VerifyUser
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentSignUpBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : BaseFragment() {

    private lateinit var options: NavOptions
    private lateinit var fragmentSignUpBinding : FragmentSignUpBinding

    private lateinit var email: String
    private lateinit var  username: String
    private lateinit var  phone: String
    private lateinit var  password: String
    lateinit var token : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sign_up, container, false)

        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater,container,false)



        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {

            fragmentSignUpBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_button_back,
                    requireActivity().theme
                )
            )
            lang = LocaleManager.LANGUAGE_ARABIC
        } else {
            fragmentSignUpBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.icon_arrow,
                    requireActivity().theme
                )
            )

            lang = LocaleManager.LANGUAGE_ENGLISH
        }



        fragmentSignUpBinding.btnSignUp.setOnClickListener{

            if (Patterns.EMAIL_ADDRESS.matcher(fragmentSignUpBinding.editEmail.text.toString()).matches()){
                if (validateFields()){
                    if (InternetConnection.checkConnection(context)){
                        verifyUser()
                    }else{
                        showInternetToast()
                    }
                }
            }else{
                showToast(getString(R.string.invalid_email))
            }
        }

        fragmentSignUpBinding.btnSignUpheading.setOnClickListener {
            findNavController().popBackStack()
        }



        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                token = task.result

                Prefs.putString("deviceToken", token)

            })

        return fragmentSignUpBinding.root

    }

    fun onNewToken(deviceToken: String) {
        Log.d("TAG", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        token = deviceToken;
    }

    private fun verifyUser() {

        progressbar("Verify User","Please wait...")
        WebServiceFactory.getInstance().verifyUser(email,phone,lang).enqueue(object : Callback<VerifyUser>{

            override fun onResponse(call: Call<VerifyUser>, response: Response<VerifyUser>) {
                if (response.body()!!.flag ==1){
                    mProgressBar.dismiss()

                    val bundle = Bundle()
                    bundle.putString("name",username)
                    bundle.putString("phone",phone)
                    bundle.putString("email",email)
                    bundle.putString("password",password)

                    findNavController().navigate(R.id.OTPFragment,bundle,options)
                } else{
                    val error = response.body()!!.message.split(".")
                    showToast(error[0])
                    mProgressBar.dismiss()
                }
            }
            override fun onFailure(call: Call<VerifyUser>, t: Throwable) {
                mProgressBar.dismiss()

            }
        })
    }

    private fun validateFields(): Boolean {

       email = fragmentSignUpBinding.editEmail.text.toString()
         username = fragmentSignUpBinding.editUsername.text.toString()
         phone = fragmentSignUpBinding.editPhone.text.toString()
         password = fragmentSignUpBinding.editPassword.text.toString()


       /* if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            showToast("invalid email")
            return false
        }*/
         if (email.isEmpty()) {

         showToast(getString(R.string.enter_email))
         return false
     }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast(getString(R.string.invalid_email));
            return  false
        }


        if (username.length <= 4) {
            showToast(getString(R.string.name_must_hav_5))
            return false
        }

        if (username.length > 16) {
            showToast(getString(R.string.not_exceeded_to_15_character))
            return false
        }



        if (phone.length < 7 ) {
            showToast(getString(R.string.phone_7_characters))
            return false
        }

        if (phone.length > 16 ) {
            showToast(getString(R.string.number_not_exceeded_15))
            return false
        }



        if (password.length < 8 ) {
            showToast(getString(R.string.atleast_8_character))
            return false
        }

        return true
    }


}