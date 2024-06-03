package com.yandex.mobius360quest

import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import com.yandex.mobius360quest.core.Navigation

// DO NOT EDIT =)
fun Fragment.navigateToNext() {
    findNavController().navigate(R.id.step_to_next)
}

fun Fragment.navigateToLastSaved() {
    val entries = Navigation.getCheckPoints(requireContext())
    if (entries.isEmpty()) {
        return navigateToNext()
    }
    NavDeepLinkBuilder(requireContext())
        .setGraph(R.navigation.nav_graph)
        .apply {
            entries.forEach {
                addDestination(it)
            }
        }
        .createPendingIntent()
        .send()
}