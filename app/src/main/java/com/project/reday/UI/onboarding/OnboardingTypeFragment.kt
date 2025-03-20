package com.project.reday.UI.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.reday.UI.MainActivity
import com.project.reday.R
import com.project.reday.Utils.DateManager
import com.project.reday.databinding.FragmentOnboardingTypeBinding

class OnboardingTypeFragment : Fragment() {

    lateinit var binding: FragmentOnboardingTypeBinding
    lateinit var mainActivity: MainActivity

    var selectedType = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnboardingTypeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        binding.run {
            layoutType1.setOnClickListener {
                selectedType = 1
                layoutType1.setBackgroundResource(R.drawable.background_type_image)
                layoutType2.setBackgroundResource(0)
                buttonNext.isEnabled = true
            }

            layoutType2.setOnClickListener {
                selectedType = 2
                layoutType1.setBackgroundResource(0)
                layoutType2.setBackgroundResource(R.drawable.background_type_image)
                buttonNext.isEnabled = true
            }

            buttonNext.setOnClickListener {
                val dateManager = DateManager(mainActivity)

                dateManager.saveType(selectedType)

                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView_main, OnboardingFirstDayFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun initView() {
        binding.run {
            buttonNext.isEnabled = false

            toolbar.buttonBack.setOnClickListener {
                fragmentManager?.popBackStack()
            }
        }
    }

}