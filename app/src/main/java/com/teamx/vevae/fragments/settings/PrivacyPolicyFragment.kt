package com.teamx.vevae.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentPrivacyPolicyBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment


class PrivacyPolicyFragment : BaseFragment() {

    lateinit var privacyPolicyBinding: FragmentPrivacyPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
        privacyPolicyBinding = FragmentPrivacyPolicyBinding.inflate(layoutInflater,container,false)

        privacyPolicyBinding.btnBack.setOnClickListener {

            popUpStack()
        }

        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            privacyPolicyBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC
        }else{
            privacyPolicyBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        return privacyPolicyBinding.root;
    }
}