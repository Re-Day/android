package com.project.reday.UI.onboarding.bottomSheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.project.reday.databinding.FragmentDateBottomSheetBinding
import java.util.*

interface DateBottomSheetInterface {
    fun onTimeClickCompleteButton(date: String)
}

class DateBottomSheetFragment(var currentYear: Int, var currentMonth: Int, var currentDay: Int) : DialogFragment() {

    private lateinit var binding: FragmentDateBottomSheetBinding
    private var listener: DateBottomSheetInterface? = null

    fun setDateBottomSheetInterface(listener: DateBottomSheetInterface) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val params = window.attributes
            val marginHorizontalInPx =
                (20 * requireContext().resources.displayMetrics.density).toInt()
            val marginBottomInPx =
                (11 * requireContext().resources.displayMetrics.density).toInt()

            params.width =
                requireContext().resources.displayMetrics.widthPixels - (marginHorizontalInPx * 2)
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
            params.y = marginBottomInPx

            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateBottomSheetBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding.run {
            // 연도 설정
            spinnerYear.minValue = 1900
            spinnerYear.maxValue = 2030
            spinnerYear.value = currentYear
            spinnerYear.setFormatter { String.format("%04d년", it) }

            // 월 설정
            spinnerMonth.minValue = 1
            spinnerMonth.maxValue = 12
            spinnerMonth.value = currentMonth
            spinnerMonth.setFormatter { String.format("%02d월", it) }

            // 일 설정 (초기화)
            updateDaySpinner(currentYear, currentMonth, currentDay)

            // 월이 변경될 때 일자 업데이트
            spinnerMonth.setOnValueChangedListener { _, _, newMonth ->
                updateDaySpinner(spinnerYear.value, newMonth, 1)
            }

            // 연도가 변경될 때 일자 업데이트
            spinnerYear.setOnValueChangedListener { _, _, newYear ->
                updateDaySpinner(newYear, spinnerMonth.value, 1)
            }

            buttonComplete.setOnClickListener {
                val year = spinnerYear.value
                val month = spinnerMonth.value
                val day = spinnerDay.value
                val selectedDate = String.format("%04d-%02d-%02d", year, month, day)

                listener?.onTimeClickCompleteButton(selectedDate)
                dismiss()
            }
        }

        return binding.root
    }

    private fun updateDaySpinner(year: Int, month: Int, selectedDay: Int) {
        val daysInMonth = getDaysInMonth(year, month)
        binding.spinnerDay.minValue = 1
        binding.spinnerDay.maxValue = daysInMonth
        // 현재 선택된 일자가 유효한 범위를 벗어나지 않도록 조정
        binding.spinnerDay.value = if (selectedDay > daysInMonth) daysInMonth else selectedDay

        binding.spinnerDay.setFormatter { String.format("%02d일", it) }

    }

    private fun getDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}
