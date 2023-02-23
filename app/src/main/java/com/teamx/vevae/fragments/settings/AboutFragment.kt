package com.teamx.vevae.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentAboutBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment


class AboutFragment : BaseFragment() {


    lateinit var fragmentAboutBinding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_about, container, false)

        fragmentAboutBinding = FragmentAboutBinding.inflate(inflater,container,false)


        fragmentAboutBinding.btnBack.setOnClickListener{

            popUpStack()
        }


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {
            lang = LocaleManager.LANGUAGE_ARABIC
            fragmentAboutBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))

        }else{
            fragmentAboutBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }

        return fragmentAboutBinding.root
    }




}