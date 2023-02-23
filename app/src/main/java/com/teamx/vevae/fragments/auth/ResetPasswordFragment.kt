package com.teamx.vevae.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import com.pixplicity.easyprefs.library.Prefs
import com.teamx.vevae.App.App
import com.teamx.vevae.Models.Password.ChangePassword
import com.teamx.vevae.Networking.WebServiceFactory
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentResetPasswordBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordFragment : BaseFragment() {

    lateinit var fragmentResetPasswordBinding: FragmentResetPasswordBinding
    private lateinit var options: NavOptions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reset_password, container, false)

        fragmentResetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater,container,false);



         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
            fragmentResetPasswordBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_screen_back,
                    requireActivity().theme
                )
            )
            fragmentResetPasswordBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_button_back,
                    requireActivity().theme
                )
            )
        }
         else {
            fragmentResetPasswordBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.back,
                    requireActivity().theme
                )
            )
            fragmentResetPasswordBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.icon_arrow,
                    requireActivity().theme
                )
            )
        }





        fragmentResetPasswordBinding.btnChangePassword.setOnClickListener {
            if (fragmentResetPasswordBinding.editPassword.text.toString()
                    .isNotEmpty() && fragmentResetPasswordBinding.editPassword.text.toString()
                    .isNotEmpty()) {
                if (fragmentResetPasswordBinding.editPassword.text.toString().equals(fragmentResetPasswordBinding.editConfirmPassword.text.toString())) {
                    changePassword(fragmentResetPasswordBinding.editPassword.text.toString())
                }else{
                    showToast(getString(R.string.password_not_matched))
                }
            } else {
                showToast(getString(R.string.enter_valid_password))
            }

        }
        return fragmentResetPasswordBinding.root

    }

    private fun changePassword(password: String) {
        val emailPhone = Prefs.getString("emailPhone","")
        progressbar("Change Password","Please wait...")
        WebServiceFactory.getInstance().changePassword(password,emailPhone).enqueue(object :
            Callback<ChangePassword> {
            override fun onResponse(
                call: Call<ChangePassword>,
                response: Response<ChangePassword>) {
                if (response.body()!!.flag==1){
                    mProgressBar.dismiss()
                    clearFragmentsLogout()
                    showToast(getString(R.string.password_reset))
                }else{
                    mProgressBar.dismiss()
                }

            }

            override fun onFailure(call: Call<ChangePassword>, t: Throwable) {
                mProgressBar.dismiss()
            }
        })

    }

}