package connectors.default

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date
import java.util.UUID
import com.google.firebase.dataconnect.ConnectorConfig
import com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.generated.GeneratedConnector
import com.google.firebase.dataconnect.generated.GeneratedMutation
import com.google.firebase.dataconnect.generated.GeneratedOperation
import com.google.firebase.dataconnect.generated.GeneratedQuery
import java.util.WeakHashMap

// Custom Serializers for UUID and Date
object DateSerializer : KSerializer<Date> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)

  override fun serialize(encoder: Encoder, value: Date) {
    encoder.encodeLong(value.time)
  }

  override fun deserialize(decoder: Decoder): Date {
    return Date(decoder.decodeLong())
  }
}

object UUIDSerializer : KSerializer<UUID> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: UUID) {
    encoder.encodeString(value.toString())
  }

  override fun deserialize(decoder: Decoder): UUID {
    return UUID.fromString(decoder.decodeString())
  }
}

@Serializable
data class ExampleDataClass(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = DateSerializer::class)
  val created: Date,
  val timestamp: Long
)

// DefaultConnector interface with generics
interface DefaultConnector<T : GeneratedConnector<T>> : GeneratedConnector<T> {
  override val dataConnect: FirebaseDataConnect
}

// DefaultConnector implementation
class DefaultConnectorImpl<T : GeneratedConnector<T>>(
  override val dataConnect: FirebaseDataConnect
) : DefaultConnector<T> {

  companion object {
    private val instances = WeakHashMap<FirebaseDataConnect, DefaultConnector<*>>()

    // Configuration for the connector
    val config: ConnectorConfig = ConnectorConfig(
      connector = "default",
      location = "us-west2",
      serviceId = "OgdensCleanersApp"
    )

    // Retrieve or create a DefaultConnector instance
    fun <T : GeneratedConnector<T>> getInstance(dataConnect: FirebaseDataConnect): DefaultConnector<T> {
      return synchronized(instances) {
        instances.getOrPut(dataConnect) {
          DefaultConnectorImpl(dataConnect)
        } as DefaultConnector<T>
      }
    }
  }

  @ExperimentalFirebaseDataConnect
  fun copy(): T {
    throw NotImplementedError("copy() is not implemented yet")
  }

  @ExperimentalFirebaseDataConnect
  override fun mutations(): List<GeneratedMutation<T, *, *>> {
    throw NotImplementedError("mutations() is not implemented yet")
  }

  @ExperimentalFirebaseDataConnect
  override fun operations(): List<GeneratedOperation<T, *, *>> {
    throw NotImplementedError("operations() is not implemented yet")
  }

  @ExperimentalFirebaseDataConnect
  override fun queries(): List<GeneratedQuery<T, *, *>> {
    throw NotImplementedError("queries() is not implemented yet")
  }

  @ExperimentalFirebaseDataConnect
  override fun copy(dataConnect: FirebaseDataConnect): T {
    TODO("Not yet implemented")
  }

  // Override equals, hashCode, and toString
  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DefaultConnectorImpl(dataConnect=$dataConnect)"
}
