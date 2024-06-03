package com.yandex.mobius360quest

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.yandex.mobius360quest.core.AuthServer
import com.yandex.mobius360quest.core.BaseViewBindingFragment
import com.yandex.mobius360quest.core.RegexEmailChecker
import com.yandex.mobius360quest.core.databinding.EmailFragmentBinding

/**
 * Пользователь должен ввести почти от существующего на сервере аккаунта
 */
class Mail : BaseViewBindingFragment<EmailFragmentBinding>(EmailFragmentBinding::inflate) {

    private val emailChecker = RegexEmailChecker(regexProvider = { simpleEmailRegex })
    private val simpleEmailRegex = Regex(".+@.+")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resetPassword.setOnClickListener {
            checkEmail(binding.inputEmail.toString())
        }
        binding.inputEmail.doOnTextChanged { _, _, _, _ ->
            binding.inputEmailLayout.error = null
        }
    }

    private fun checkEmail(email: String) {
        val ok = emailChecker.isEmail(email)

        if (ok && AuthServer.checkEmail(email)) {
            navigateToNext()
        } else {
            binding.inputEmailLayout.error = getString(
                if (ok) R.string.text_email_not_found else R.string.text_wrong_email
            )
        }
    }
}