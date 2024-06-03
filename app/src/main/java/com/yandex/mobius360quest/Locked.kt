package com.yandex.mobius360quest

import android.os.Bundle
import android.view.View
import com.google.android.material.slider.LabelFormatter
import com.yandex.mobius360quest.core.BaseViewBindingFragment
import com.yandex.mobius360quest.core.databinding.LockpuzzleBinding

/**
 * Пользователь должен пройти проверку на робота
 * Для этого нужно передвинуть слайдером картинку в правильное положение
 */
class Locked : BaseViewBindingFragment<LockpuzzleBinding>(LockpuzzleBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.post {
            binding.slider.values = listOf(binding.grid.x)
            binding.slider.valueFrom = binding.grid.x
            binding.slider.valueTo = binding.grid.width.toFloat()
        }
        binding.slider.labelBehavior = LabelFormatter.LABEL_GONE
        binding.slider.addOnChangeListener { _, value, fromUser ->
            binding.target.x = value * 10
            checkHit(false)
        }
        binding.button.setOnClickListener {
            navigateToNext()
        }
    }

    fun checkHit(fromUser: Boolean) {
        val centerX = binding.target.x + binding.target.width / 2
        val hitX = centerX in binding.placeholder.x..(binding.placeholder.y + binding.placeholder.width)
        binding.button.isEnabled = fromUser && hitX
    }

}
