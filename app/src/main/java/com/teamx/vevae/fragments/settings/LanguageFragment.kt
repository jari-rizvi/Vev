package com.teamx.vevae.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ARABIC
import com.teamx.vevae.Utils.localization.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.teamx.vevae.databinding.FragmentLanguageBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment


class LanguageFragment : BaseFragment() {


    lateinit var fragmentLanguageBinding: FragmentLanguageBinding
    var selecteLanguage : Int =0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_language, container, false)
        fragmentLanguageBinding = FragmentLanguageBinding.inflate(inflater,container,false)


        if (!App.localeManager.getLanguage().equals(LocaleManager.Companion.LANGUAGE_ENGLISH)) {

            fragmentLanguageBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.arabic_screen_back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ARABIC

        }else{
            fragmentLanguageBinding.btnBack.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.back, requireActivity().theme))
            lang = LocaleManager.LANGUAGE_ENGLISH
        }


        if (App.localeManager.getLanguage() == LANGUAGE_ENGLISH) {
            fragmentLanguageBinding.radioEnglish.isChecked = true
        } else if (App.localeManager.getLanguage() == LANGUAGE_ARABIC) {
            fragmentLanguageBinding.radioArabic.isChecked = true
        }


        fragmentLanguageBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                com.teamx.vevae.R.id.radioEnglish -> {
                    if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
                        setNewLocale(LANGUAGE_ENGLISH, true)
                    }
                }
                com.teamx.vevae.R.id.radioArabic -> {
                    if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ARABIC)) {
                        setNewLocale(LANGUAGE_ARABIC, true)
                    }
                }
            }
        }



        fragmentLanguageBinding.btnBack.setOnClickListener{
            popUpStack()
        }

        return fragmentLanguageBinding.root
    }
}
