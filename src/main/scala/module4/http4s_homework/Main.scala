package module4.http4s_homework

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      _ <- Restful.server.use(_ => HttpClient.printResponse *> IO.never)
    } yield ()
  }
}

// curl localhost:8080/counter
// curl localhost:8080/slow/qw/as/-1
// curl localhost:8080/slow/10/50/1