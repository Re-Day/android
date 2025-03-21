package com.project.reday.UI.onboarding

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.reday.UI.MainActivity
import com.project.reday.R
import com.project.reday.UI.home.HomeFragment
import com.project.reday.Utils.DateManager
import com.project.reday.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        Handler().postDelayed({
            val dateManager = DateManager(mainActivity)
            when(dateManager.getType()) {
                1 -> {
                    if(dateManager.getDate("re_day") != null) {
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main, HomeFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main, OnboardingNicknameFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
                2 -> {
                    if(dateManager.getDate("first_day") != null) {
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main, HomeFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main, OnboardingNicknameFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
                else -> {
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, OnboardingNicknameFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }, 1000)

        return binding.root
    }
}