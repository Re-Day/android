package com.project.reday.UI.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.reday.R
import com.project.reday.UI.MainActivity
import com.project.reday.UI.home.adapter.Anniversary
import com.project.reday.UI.home.adapter.AnniversaryAdapter
import com.project.reday.Utils.DateManager
import com.project.reday.Utils.UserManager
import com.project.reday.Utils.Utils.calculateUpcomingAnniversariesByReday
import com.project.reday.Utils.Utils.calculateUpcomingAnniversariesByTotalDay
import com.project.reday.Utils.Utils.formatDateToShort
import com.project.reday.Utils.Utils.getCurrentDate
import com.project.reday.Utils.Utils.getDaysSinceFristDay
import com.project.reday.Utils.Utils.getDaysSinceReunion
import com.project.reday.Utils.Utils.getReDays
import com.project.reday.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    private lateinit var anniversaryAdapter: AnniversaryAdapter

    var getTotalAnniversaries = listOf<Anniversary>()
    var getReAnniversaries = listOf<Anniversary>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        binding.run {
            switchReday.setOnClickListener {
                if(switchReday.isChecked) {
                    textViewToday.text = DateManager(mainActivity).getDate("first_day")

                    textViewTitle.text = "우리가 함께한지"
                    textViewTotalDay.text = "${getReDays(mainActivity)}일"
                    anniversaryAdapter.updateList(getTotalAnniversaries)

                    setRecentAnniversary(getTotalAnniversaries)
                } else {
                    textViewToday.text = DateManager(mainActivity).getDate("re_day")

                    textViewTitle.text = "다시 만난지"
                    textViewTotalDay.text = "${getDaysSinceReunion(mainActivity)}일"
                    anniversaryAdapter.updateList(getReAnniversaries)

                    setRecentAnniversary(getReAnniversaries)
                }
            }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setRecentAnniversary(anniversary: List<Anniversary>) {
        binding.run {
            if(anniversary.isNotEmpty()) {
                layoutRecentAnniversary.run {
                    textViewAnniversaryDay.text = "${anniversary[0].title}까지"
                    textViewDday.text = "D-${anniversary[0].daysLeft}"
                    textViewAnniversaryDate.text = formatDateToShort(anniversary[0].date.toString())

                    progress.run {
                        post { // ✅ `layoutParams.width`는 UI가 그려진 후 접근해야 정확한 값을 가져올 수 있음
                            val bgWidth = layoutRecentAnniversary.progressBackground.width // background의 실제 width 가져오기

                            println("bgWidth : $bgWidth")
                            if (bgWidth > 0) {
                                val percentage = (100 - anniversary[0].daysLeft) / 100f // ✅ 퍼센트 계산 (0 ~ 1)
                                val calculatedWidth = (bgWidth * percentage).toInt().coerceIn(0, bgWidth) // width 범위 제한

                                val layoutParams = layoutRecentAnniversary.progress.layoutParams
                                layoutParams.width = calculatedWidth
                                layoutRecentAnniversary.progress.layoutParams = layoutParams
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter(anniversaries: List<Anniversary>) {
        binding.recyclerViewAnniversary.run {
            layoutManager = LinearLayoutManager(requireContext())
            anniversaryAdapter = AnniversaryAdapter(requireActivity(), anniversaries)
            adapter = anniversaryAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initView() {
        val dateManager = DateManager(mainActivity)
        binding.run {
            textViewToday.text = dateManager.getDate("first_day")

            if(dateManager.getType() == 1) {
                switchReday.visibility = View.VISIBLE
                switchReday.isChecked = true
                textViewTitle.text = "우리가 함께한지"
                textViewTotalDay.text = "${getReDays(mainActivity)}일"
            } else {
                switchReday.visibility = View.INVISIBLE
                textViewTotalDay.text = "${getDaysSinceFristDay(mainActivity)}일"
            }

            if(DateManager(mainActivity).getType() == 1) {
                // 기념일 계산
                getTotalAnniversaries = calculateUpcomingAnniversariesByTotalDay(dateManager.getDate("first_day") ?: getCurrentDate(), getReDays(mainActivity))
                getReAnniversaries = calculateUpcomingAnniversariesByReday(dateManager.getDate("re_day") ?: getCurrentDate(), getDaysSinceReunion(mainActivity)!!)

                initAdapter(getTotalAnniversaries)
            } else {
                // 기념일 계산
                getTotalAnniversaries = calculateUpcomingAnniversariesByTotalDay(dateManager.getDate("first_day") ?: getCurrentDate(), getDaysSinceFristDay(mainActivity)!!)

                initAdapter(getTotalAnniversaries)
            }

            setRecentAnniversary(getTotalAnniversaries)
        }
    }
}