@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package com.yandex.mobius360quest

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BusResponse::class)
object ServerSerializer: KSerializer<BusResponse> {
    override val descriptor = buildClassSerialDescriptor("BusResponse") {
        element<String>("answer_to_check")
    }

    override fun deserialize(decoder: Decoder): BusResponse {

        return decoder.decodeStructure(descriptor) {
            var answer: BusCheckResult = BusCheckResult.ACCESS_DENIED
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop

                    0 -> answer = try {
                        if (decodeStringElement(descriptor, 1) === "ACCESS_GRANTED") BusCheckResult.ACCESS_GRANTED else BusCheckResult.ACCESS_DENIED
                    } catch (_: Exception) {
                        BusCheckResult.ACCESS_DENIED
                    }
                }
            }

            BusResponse(answer)
        }
    }

    override fun serialize(encoder: Encoder, value: BusResponse) {
        TODO("Not yet implemented")
    }
}