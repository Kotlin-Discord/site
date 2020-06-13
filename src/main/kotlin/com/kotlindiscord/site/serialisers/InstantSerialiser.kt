package com.kotlindiscord.site.serialisers

import kotlinx.serialization.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant

@Serializer(forClass = Instant::class)
class InstantSerialiser : KSerializer<Instant> {
    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss.SSS")

    override val descriptor: SerialDescriptor = PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(df.format(value))
    }

    override fun deserialize(decoder: Decoder): Instant = df.parse(decoder.decodeString()).toInstant()
}
