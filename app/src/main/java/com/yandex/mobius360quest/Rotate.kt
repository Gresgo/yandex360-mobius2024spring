package com.yandex.mobius360quest

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.forEach
import com.yandex.mobius360quest.core.BaseViewBindingFragment
import com.yandex.mobius360quest.core.Randomizer
import com.yandex.mobius360quest.core.databinding.RotateLayoutBinding

/**
 * Финальная проверка на робота
 * Пользователь должен с помощью экранной клавиатуры ввести проверочный код
 * Вся клавиатура не влезла на экран, поэтому было решено половину перенести в landscape
 */
class Rotate : BaseViewBindingFragment<RotateLayoutBinding>(RotateLayoutBinding::inflate) {

    private val value = Randomizer.getNewValue()
    private var entry = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entry = binding.passcode.text.toString()
        makeCheck()
        binding.text.text = getString(R.string.rotate_passcode_template, value)
        binding.grid.forEach { itemView ->
            when {
                itemView.id == R.id.button_clear -> itemView.setOnClickListener {
                    binding.passcode.text = entry
                    entry = ""
                }
                itemView.id == R.id.button_next -> itemView.setOnClickListener {
                    navigateToNext()
                }
                itemView is Button -> itemView.setOnClickListener {
                    entry += itemView.text.toString()
                    binding.passcode.text = entry
                    makeCheck()
                }
            }
        }
    }

    private fun makeCheck() {
        binding.buttonNext?.isEnabled = Randomizer.compare(entry, value)
    }
}