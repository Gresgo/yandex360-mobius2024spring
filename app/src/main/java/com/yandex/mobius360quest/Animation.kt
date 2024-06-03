package com.yandex.mobius360quest

import android.os.Bundle
import android.view.View
import com.yandex.mobius360quest.databinding.AnimationFragmentBinding
import com.yandex.mobius360quest.core.BaseViewBindingFragment

class Animation : BaseViewBindingFragment<AnimationFragmentBinding>(AnimationFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yaAnimationView.loadAnimation("anim_1.txt")
    }
}