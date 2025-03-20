package com.project.reday.UI.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.reday.R
import com.project.reday.UI.MainActivity
import com.project.reday.Utils.DateManager
import com.project.reday.Utils.UserManager
import com.project.reday.Utils.Utils.getCurrentDate
import com.project.reday.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        Log.d("REDAY", "${UserManager(mainActivity).getNickname()}님의 총 reday : ${DateManager(mainActivity).getReDays()}, 다시 만난 날짜 : ${DateManager(mainActivity).getDaysSinceReunion()}, 처음 만난 날짜 : ${DateManager(mainActivity).getDaysSinceFristDay()}")

        binding.run {
            switchReday.setOnClickListener {
                if(switchReday.isChecked) {
                    textViewTitle.text = "우리가 함께한지"
                    textViewTotalDay.text = "${DateManager(mainActivity).getReDays()}일"
                } else {
                    textViewTitle.text = "다시 만난지"
                    textViewTotalDay.text = "${DateManager(mainActivity).getDaysSinceReunion()}일"
                }
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
            textViewToday.text = getCurrentDate().toString()
            if(DateManager(mainActivity).getType() == 1) {
                switchReday.visibility = View.VISIBLE
                switchReday.isChecked = true
                textViewTitle.text = "우리가 함께한지"
                textViewTotalDay.text = "${DateManager(mainActivity).getReDays()}일"
            } else {
                switchReday.visibility = View.INVISIBLE
            }
        }
    }
}