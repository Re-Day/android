package com.project.reday.UI.onboarding

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.project.reday.UI.MainActivity
import com.project.reday.R
import com.project.reday.Utils.UserManager
import com.project.reday.databinding.FragmentOnboardingNicknameBinding

class OnboardingNicknameFragment : Fragment() {

    lateinit var binding: FragmentOnboardingNicknameBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnboardingNicknameBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        observeKeyboardState()

        binding.run {
            editTextNickname.addTextChangedListener {
                buttonNext.isEnabled = editTextNickname.text.length in 2..10
            }

            buttonNext.setOnClickListener {
                val dateManager = UserManager(mainActivity)

                dateManager.saveNickname(editTextNickname.text.toString())

                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView_main, OnboardingTypeFragment())
                    .addToBackStack(null)
                    .commit()
            }

        }

        return binding.root
    }

    private fun observeKeyboardState() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            var originHeight = -1
            if ( binding.root.height > originHeight) {
                originHeight =  binding.root.height
            }

            val visibleFrameSize = Rect()
            binding.root.getWindowVisibleDisplayFrame(visibleFrameSize)

            val visibleFrameHeight = visibleFrameSize.bottom - visibleFrameSize.top
            val keyboardHeight = originHeight - visibleFrameHeight

            if (keyboardHeight > visibleFrameHeight * 0.15) {
                // 키보드가 올라옴
                binding.buttonNext.translationY = - keyboardHeight.toFloat() // 버튼을 키보드 위로 이동
            } else {
                // 키보드가 내려감
                binding.buttonNext.translationY = + keyboardHeight.toFloat() // 버튼을 키보드 위로 이동
            }
        }
    }
}