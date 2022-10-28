package module4

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

package object http4s_homework {

  case class CounterEntity(counter: Long)

  implicit val decoderCounterEntity: Decoder[CounterEntity] =
    deriveDecoder

  implicit val encoderCounterEntity: Encoder[CounterEntity] =
    deriveEncoder

  case class SlowResponseEntity(chunk: Int, total: Int, time: Int)

  case class SlowResponseStrEntity(chunk: String, total: String, time: String) {

    private def validateIntPositive(value: String, name: String): List[String] = {
      val intValue = value.toIntOption
      if (intValue.nonEmpty && intValue.get > 0)
        Nil
      else List(s"Parameter $name: $value failed validation")
    }

    def validate(): Either[String, SlowResponseEntity] = {
      val lstErr = validateIntPositive(chunk, "chunk") ++
      validateIntPositive(total, "total") ++
      validateIntPositive(time, "time")
      if (lstErr.isEmpty)
        Right(SlowResponseEntity(chunk.toInt, total.toInt, time.toInt))
      else
        Left(lstErr.mkString(", "))
    }
  }
}
