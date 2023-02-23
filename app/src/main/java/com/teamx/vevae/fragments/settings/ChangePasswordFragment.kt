package com.teamx.vevae.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.Password.ChangePassword
import com.teamx.vevae.Networking.InternetConnection
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.snackbar
import com.teamx.vevae.databinding.FragmentChnagePasswordBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordFragment : BaseFragment() {


    lateinit var fragmentChangePasswordBinding : FragmentChnagePasswordBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chnage_password, container, false)
        fragmentChangePasswordBinding = FragmentChnagePasswordBinding.inflate(inflater,container,false)

        fragmentChangePasswordBinding.btnChangePassword.setOnClickListener {

            if (isValidate()){
                if (InternetConnection.checkConnection(requireContext())){
                    updatePassword()
                }else{
                    showInternetToast()
                }
            }

        }

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentChangePasswordBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            fragmentChangePasswordBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_button_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentChangePasswordBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            fragmentChangePasswordBinding.buttonBackImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.icon_arrow, requireActivity().theme))

            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        fragmentChangePasswordBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        return fragmentChangePasswordBinding.root

    }

    private fun updatePassword() {

        progressbar("Updating password","Please wait...")
        //update password code
        WebServiceFactory.getInstance().updatePassword(fragmentChangePasswordBinding.editCurrentPassword.text.toString(),
            fragmentChangePasswordBinding.editPassword.text.toString(),
            fragmentChangePasswordBinding.editConfirmPassword.text.toString()
        ).enqueue(object: Callback<ChangePassword>{
            override fun onResponse(
                call: Call<ChangePassword>,
                response: Response<ChangePassword>
            ) {

                if (response.body()?.flag==1){
                    mProgressBar.dismiss()
                    fragmentChangePasswordBinding.rootLayout.snackbar(getString(R.string.password_Updated))
                    popUpStack()

                }else{
                    mProgressBar.dismiss()
                    fragmentChangePasswordBinding.rootLayout.snackbar(response.body()!!.message)

                }
            }

            override fun onFailure(call: Call<ChangePassword>, t: Throwable) {
                mProgressBar.dismiss()
            }

        })
    }

    fun isValidate() : Boolean {

        if (fragmentChangePasswordBinding.editCurrentPassword.text.toString()
                .isEmpty() ){
            showToast(getString(R.string.please_enter_password))
            return false
        }


        if (fragmentChangePasswordBinding.editPassword.text.toString()
                .isEmpty() ){
            showToast(getString(R.string.enter_new_password))
            return false
        }

        if (fragmentChangePasswordBinding.editPassword.text.toString().length < 7 ){
            showToast(getString(R.string.character_password_8))
            return false
        }


        if (fragmentChangePasswordBinding.editConfirmPassword.text.toString()
                .isEmpty() ){
            showToast(getString(R.string.enter_confirm_password))
            return false
        }

        if (fragmentChangePasswordBinding.editConfirmPassword.text.toString().length < 7 ){
            showToast(getString(R.string.passwrd_must_be_8_character_long))
            return false
        }

        if (!fragmentChangePasswordBinding.editPassword.text.toString().equals(fragmentChangePasswordBinding.editConfirmPassword.text.toString())){
            showToast(getString(R.string.password_not_matched))
        return false
    }




    return true
    }

}