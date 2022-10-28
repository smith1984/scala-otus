package module4.http4s_homework

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref, Resource}
import org.http4s.{Method, Request, Response}
import org.scalatest.flatspec.AnyFlatSpec
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder

class CounterServiceTest extends AnyFlatSpec {

  "check next counter" should "ok" in {
    val result = for {
      response <- Ref.of[IO, Long](10).flatMap { cnt =>
        Restful.router(cnt).run(Request(Method.GET, uri"/counter")).value
      }
      body <- response.get.as[CounterEntity]
    } yield body

    assert(result.unsafeRunSync() ===  CounterEntity(11))
  }

  "check status success" should "ok" in {
    val result = for {
      response <- Ref.of[IO, Long](0).flatMap { cnt =>
        Restful.router(cnt).orNotFound.run(Request(Method.GET, uri"/counter"))
      }
      isSuccess <- IO.pure(response.status.isSuccess)
    } yield isSuccess

    assert(result.unsafeRunSync() === true)
  }

  "check not found page" should "ok" in {
    val result = for {
      response <- Ref.of[IO, Long](0).flatMap { cnt =>
        Restful.router(cnt).orNotFound.run(Request(Method.GET, uri"/counter_"))
      }
      code <- IO.pure(response.status.code)
    } yield code

    assert(result.unsafeRunSync() === 404)
  }
}
