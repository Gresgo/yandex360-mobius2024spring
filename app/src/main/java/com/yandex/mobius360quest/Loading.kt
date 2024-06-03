package com.yandex.mobius360quest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yandex.mobius360quest.core.ImportantBackgroundWork
import com.yandex.mobius360quest.core.databinding.OrdinaryDaysBinding
import java.util.Calendar

/**
 * Для восстановления нужно получить от пользователя дату рождения
 * Пока данные пользователя обрабатываются нужно показать экран загрузки
 */
class Loading : Fragment() {

    private var _binding: OrdinaryDaysBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrdinaryDaysBinding.inflate(inflater, container, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.picker) {
            processDate(year, month, dayOfMonth)
            setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                processDate(year, monthOfYear, dayOfMonth)
            }
        }
        binding.button.setOnClickListener {
            Thread {
                binding.loader.isVisible = true
                // do not remove, it is very important !!!
                ImportantBackgroundWork.launch {
                    binding.loader.isVisible = false
                }
                Handler(Looper.getMainLooper()) {
                    navigateToNext()
                    false
                }.post{}
            }
        }
    }

    private fun processDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        val minRequired = calendar.time
        calendar.set(year, month, day)
        val selected = calendar.time
        binding.button.isEnabled = minRequired.after(selected)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}