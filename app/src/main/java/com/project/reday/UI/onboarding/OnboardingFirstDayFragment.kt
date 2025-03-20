package com.project.reday.UI.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.reday.R
import com.project.reday.UI.MainActivity
import com.project.reday.UI.home.HomeFragment
import com.project.reday.UI.onboarding.bottomSheet.DateBottomSheetFragment
import com.project.reday.UI.onboarding.bottomSheet.DateBottomSheetInterface
import com.project.reday.Utils.DateManager
import com.project.reday.databinding.FragmentOnboardingFirstDayBinding
import java.util.Calendar

class OnboardingFirstDayFragment : Fragment() {

    lateinit var binding: FragmentOnboardingFirstDayBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnboardingFirstDayBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        binding.run {
            editTextDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val currentYear =
                    if(editTextDate.text.isNotEmpty()) {
                        val dateParts = editTextDate.text.split("-")

                        dateParts[0].toInt()
                    } else {
                        calendar.get(Calendar.YEAR)
                    }
                val currentMonth =
                    if(editTextDate.text.isNotEmpty()) {
                        val dateParts = editTextDate.text.split("-")

                        dateParts[1].toInt()
                    } else {
                        calendar.get(Calendar.MONTH) + 1
                    }
                val currentDay =
                    if(editTextDate.text.isNotEmpty()) {
                        val dateParts = editTextDate.text.split("-")

                        dateParts[2].toInt()
                    } else {
                        calendar.get(Calendar.DAY_OF_MONTH)
                    }

                val dateBottomsheet = DateBottomSheetFragment(currentYear, currentMonth, currentDay)

                dateBottomsheet.setDateBottomSheetInterface(object : DateBottomSheetInterface {
                    override fun onTimeClickCompleteButton(date: String) {
                        editTextDate.setText(date)
                        buttonNext.isEnabled = true
                    }
                })

                dateBottomsheet.show(
                    mainActivity.supportFragmentManager,
                    "DateBottomsheet"
                )
            }

            buttonNext.setOnClickListener {
                val dateManager = DateManager(mainActivity)

                dateManager.saveDate("first_day", editTextDate.text.toString())

                if(dateManager.getType() == 1) {
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, OnboardingBrokenDayFragment())
                        .addToBackStack(null)
                        .commit()
                } else {
                }
            }
        }
        return binding.root
    }

}