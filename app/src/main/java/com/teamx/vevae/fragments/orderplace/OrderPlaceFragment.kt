package com.teamx.vevae.fragments.orderplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.vevae.App.App
import com.teamx.vevae.R
import com.teamx.vevae.Utils.localization.LocaleManager
import com.teamx.vevae.databinding.FragmentOrderPlaceBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment

class OrderPlaceFragment : BaseFragment() {

    lateinit var orderPlaceBinding: FragmentOrderPlaceBinding
    private lateinit var options: NavOptions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_order_place, container, false)

        orderPlaceBinding = FragmentOrderPlaceBinding.inflate(inflater,container,false)


        options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        orderPlaceBinding.btnBack.setOnClickListener{
            popUpStack()
        }

         if (!App.localeManager.getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)) {
            orderPlaceBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_screen_back,
                    requireActivity().theme
                )
            )
            orderPlaceBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.arabic_button_back,
                    requireActivity().theme
                )
            )
        } else {
            orderPlaceBinding.btnBack.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.back,
                    requireActivity().theme
                )
            )
            orderPlaceBinding.buttonBackImage.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.icon_arrow,
                    requireActivity().theme
                )
            )
        }


        orderPlaceBinding.btnPlaceOrder.setOnClickListener{
            popUpStack()
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_Fragmnet)
            navController.navigate(R.id.  profileFragment, null,options)
        }





        return orderPlaceBinding.root

    }


}