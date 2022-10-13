package module4.http4s_homework

import cats.effect.{IO, Ref}
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits._
import org.http4s.{Method, Request}

object HttpClient {
  val builder = EmberClientBuilder.default[IO].build

  val request = Request[IO](uri = uri"http://localhost:8080/counter")

  val result = for {
    client <- builder
    response <- client.run(request)
  } yield response

  val printResponse = result.use(IO.println)

}
