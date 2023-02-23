package com.teamx.vevae.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.teamx.vevae.R
import com.teamx.vevae.activities.MainActivity
import com.teamx.vevae.databinding.FragmentSplashBinding
import com.teamx.vevae.fragments.baseFragmnet.BaseFragment
import com.pixplicity.easyprefs.library.Prefs


class SplashFragment : BaseFragment() {

    lateinit var  splashBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_splash, container, false)

        splashBinding = FragmentSplashBinding.inflate(inflater,container,false);

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_animation)
        splashBinding.splashIcon.startAnimation(animation)

        val userToken = Prefs.getString("userToken", "");

        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code

            if (userToken.isNotEmpty()) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                findNavController().popBackStack(R.id.splashFragment, true)
                findNavController().navigate(R.id.loginFragment, null)
            }
        }, 3000)



        return splashBinding.root

    }


}