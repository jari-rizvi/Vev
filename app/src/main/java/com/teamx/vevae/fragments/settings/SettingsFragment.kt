package com.teamx.vevae.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.databinding.FragmentSettingsBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.pixplicity.easyprefs.library.Prefs

class SettingsFragment : BaseFragment() {

    private lateinit var options: NavOptions
    lateinit var fragmentSettingsBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false)

        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater,container,false)

        (activity as MainActivity).hideNavBar()

        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        guestUser()

/*
       val userToken = Prefs.getString("userToken", "")
        if (!userToken.isEmpty()) {
            fragmentSettingsBinding.btnNotification.setVisibility(View.VISIBLE)
        } else {
            fragmentSearchBinding.btnNotification.setVisibility(View.GONE)
        }*/

/*edit_arrow*/
        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentSettingsBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))

            fragmentSettingsBinding.profileback.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentSettingsBinding.passwordBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentSettingsBinding.langBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentSettingsBinding.policyBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentSettingsBinding.aboutBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            fragmentSettingsBinding.legalBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_setting_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            fragmentSettingsBinding.profileback.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentSettingsBinding.passwordBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentSettingsBinding.langBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentSettingsBinding.policyBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentSettingsBinding.aboutBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))
            fragmentSettingsBinding.legalBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.edit_arrow, requireActivity().theme))

            fragmentSettingsBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        fragmentSettingsBinding.btnBack.setOnClickListener{
            popUpStack()
        }


        fragmentSettingsBinding.btnEditProfile.setOnClickListener{
            findNavController().navigate(R.id.editProfileFragment, null,options)
        }

        fragmentSettingsBinding.btnChangePassword.setOnClickListener{
            findNavController().navigate(R.id.changePasswordFragment, null,options)
        }

        fragmentSettingsBinding.btnLanguage.setOnClickListener{
            findNavController().navigate(R.id.languageFragment, null,options)
        }

        fragmentSettingsBinding.btnPrivacypolicy.setOnClickListener{
            findNavController().navigate(R.id.privacyPolicyFragment, null,options)
        }

        fragmentSettingsBinding.btnAbout.setOnClickListener{
            findNavController().navigate(R.id.aboutFragment, null,options)
        }

        fragmentSettingsBinding.btnLegalInformation.setOnClickListener{

        }

        return fragmentSettingsBinding.root
    }


    private fun guestUser() {

        val userToken = Prefs.getString("userToken", "")
        if (userToken.isNotEmpty()) {

            fragmentSettingsBinding.btnEditProfile.setVisibility(View.VISIBLE)
            fragmentSettingsBinding.btnChangePassword.setVisibility(View.VISIBLE)
            fragmentSettingsBinding.view1.setVisibility(View.VISIBLE)
            fragmentSettingsBinding.view2.setVisibility(View.VISIBLE)
            fragmentSettingsBinding.view4.setVisibility(View.VISIBLE)


        } else {
            fragmentSettingsBinding.btnEditProfile.setVisibility(View.GONE)
            fragmentSettingsBinding.btnChangePassword.setVisibility(View.GONE)
            fragmentSettingsBinding.view1.setVisibility(View.GONE)
            fragmentSettingsBinding.view2.setVisibility(View.GONE)
            fragmentSettingsBinding.view4.setVisibility(View.VISIBLE)

        }
    }

}