package module4.http4s_homework

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref, Resource}
import org.http4s.Status.BadRequest
import org.http4s.{Method, Request, Response}
import org.scalatest.flatspec.AnyFlatSpec
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder

class SlowResponseServiceTest extends AnyFlatSpec {

  "check bad request" should "ok" in {

    val expected = "Parameter chunk: as failed validation, Parameter time: -1 failed validation"

    val result = for {
      response <- Ref.of[IO, Long](0).flatMap { cnt =>
        Restful
          .router(cnt)
          .run(Request(Method.GET, uri"/slow/as/50/-1"))
          .value
          .flatMap(r => IO.pure(r))
      }
      body <- response.get.as[String]
      status <- IO.pure(response.get.status)
    } yield (body, status)

    val resBodyAndStatus = result.unsafeRunSync()

    assert(resBodyAndStatus._2 === BadRequest)
    assert(resBodyAndStatus._1 === expected)
  }

  "check processed time for response body" should "ok" in {

    val result = for {
      start <- IO.realTime
      _ <- Ref.of[IO, Long](0).flatMap { cnt =>
        Restful
          .router(cnt)
          .run(Request(Method.GET, uri"/slow/10/50/1"))
          .value
          .flatMap(r => IO.pure(r))
      }
      end <- IO.realTime
      procTime <- IO.pure((end - start).toSeconds)
    } yield procTime

    assert( result.unsafeRunSync() === 5)
  }

}
