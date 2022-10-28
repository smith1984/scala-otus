package module4.http4s_homework

import cats.effect.{IO, Ref, Resource}
import com.comcast.ip4s.{Host, Port}
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._

import scala.concurrent.duration.DurationInt

object Restful {

  type Counter[F[_]] = Ref[F, Long]

  def counterService(counter: Counter[IO]): HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "counter" => for {
      _ <- counter.update(_ + 1)
      cnt <- counter.get
    response <- Ok(CounterEntity(cnt))
    } yield response
  }

  def slowResponseService(): HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / chunk / total / time  => {
      val resValidate = SlowResponseStrEntity(chunk = chunk, total = total, time = time).validate()
      resValidate match {
        case Left(errMsg) => BadRequest(errMsg)
        case Right(sre) =>
          val stream: Stream[IO, String] = Stream.
          range(0, sre.total).
          as(1).
          chunkN(sre.chunk).
          evalMapChunk(c =>
            IO.sleep(sre.time.second) *>
              IO.pure(c.toArray.mkString)
          )
          Ok(stream)
      }
    }
  }

  def router(counter: Counter[IO]) = Router(
    "/" -> counterService(counter),
    "/slow" -> slowResponseService()
  )

  val server = for {
    counter <- Resource.eval(Ref.of[IO, Long] (0L))
    s <- EmberServerBuilder
      .default[IO]
      .withPort(Port.fromInt(8080).get)
      .withHost(Host.fromString("localhost").get)
      .withHttpApp(router(counter).orNotFound)
      .build
  } yield s
}
