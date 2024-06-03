@file:UseSerializers(ServerSerializer::class)

package com.yandex.mobius360quest

import kotlinx.serialization.UseSerializers
import kotlinx.serialization.Serializable

enum class BusCheckResult { ACCESS_GRANTED, ACCESS_DENIED }

@Serializable
data class BusWrappedResponse(
    val id: Long,
    val innerResponse: BusResponse
)

data class BusResponse(
    val correct: BusCheckResult
)

data class BusRequest(
    val tapCount: Int,
    val time: Int,
    val selectedItems: List<Int>
)
