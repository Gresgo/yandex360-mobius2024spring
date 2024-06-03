package com.yandex.mobius360quest

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.yandex.mobius360quest.core.AuthResponse
import com.yandex.mobius360quest.core.AuthServer
import com.yandex.mobius360quest.core.BaseViewBindingFragment
import com.yandex.mobius360quest.core.databinding.AuthFragmentBinding

class Auth : BaseViewBindingFragment<AuthFragmentBinding>(AuthFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.auth.setOnClickListener {
            login(binding.inputLogin.text.toString(), binding.inputPassword.text.toString())
        }
        val removeError = { _: CharSequence?, _: Int, _: Int, _: Int ->
            binding.inputLoginLayout.error = null
            binding.inputPasswordLayout.error = null
        }
        binding.inputLogin.doOnTextChanged(removeError)
        binding.inputPassword.doOnTextChanged(removeError)
        binding.resetPassword.setOnClickListener {
            navigateToLastSaved()
        }
    }

    private fun login(login: String, password: String) {
        when (AuthServer.authorize(login, password)) {
            AuthResponse.WRONG -> {
                binding.inputLoginLayout.error = getString(R.string.text_error)
                binding.inputPasswordLayout.error = getString(R.string.text_creds)
            }
            AuthResponse.WEAK_PASS -> {
                binding.inputLoginLayout.error = getString(R.string.text_error)
                binding.inputPasswordLayout.error = getString(R.string.text_weak_pass)
            }
            AuthResponse.SUCCESS -> {
                findNavController().navigate(R.id.win)
            }
        }
    }
}