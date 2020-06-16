package com.kotlindiscord.site.serialisers

import com.kotlindiscord.site.emuns.InfractionType
import com.kotlindiscord.site.emuns.toInfractionType
import kotlinx.serialization.*
import java.time.Instant

@Serializer(forClass = Instant::class)
class InfractionTypeSerialiser : KSerializer<InfractionType> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: InfractionType) {
        encoder.encodeString(value.type)
    }

    override fun deserialize(decoder: Decoder): InfractionType = decoder.decodeString().toInfractionType()
}
