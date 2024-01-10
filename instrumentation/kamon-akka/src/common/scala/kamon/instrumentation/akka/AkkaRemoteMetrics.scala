package kamon.instrumentation.akka

import kamon.Kamon
import kamon.metric.InstrumentGroup
import kamon.metric.MeasurementUnit.information
import kamon.tag.TagSet

import scala.collection.concurrent.TrieMap

object AkkaRemoteMetrics {

  val InboundMessageSize = Kamon.histogram (
    name = "akka.remote.messages.inbound.size",
    description = "Tracks the distribution of inbound message sizes",
    unit = information.bytes
  )

  val OutboundMessageSize = Kamon.histogram (
    name = "akka.remote.messages.outbound.size",
    description = "Tracks the distribution of outbound message sizes",
    unit = information.bytes
  )

  val SerializationTime = Kamon.timer (
    name = "akka.remote.serialization-time",
    description = "Tracks the time taken to serialize outgoing messages"
  )

  val DeserializationTime = Kamon.timer (
    name = "akka.remote.deserialization-time",
    description = "Tracks the time taken to deserialize incoming messages"
  )

  private val _serializationInstrumentsCache = TrieMap.empty[String, SerializationInstruments]

  class SerializationInstruments(systemName: String) extends InstrumentGroup(TagSet.of("system", systemName)) {
    val inboundMessageSize = register(InboundMessageSize)
    val outboundMessageSize = register(OutboundMessageSize)
    val serializationTime = register(SerializationTime)
    val deserializationTime = register(DeserializationTime)
  }

  def serializationInstruments(system: String): SerializationInstruments =
    _serializationInstrumentsCache.getOrElseUpdate(system, new SerializationInstruments(system))
}
