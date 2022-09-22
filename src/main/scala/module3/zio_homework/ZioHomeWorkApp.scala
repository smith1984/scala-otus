package module3.zio_homework

import zio.clock.Clock
import zio.console.Console
import zio.random.Random
import zio.{ExitCode, URIO}

object ZioHomeWorkApp extends zio.App {
  override def run(args: List[String]): URIO[Clock with Random with Console, ExitCode] = {
    //1 задание
    //module3.zio_homework.guessProgram.exitCode

    // 2 задание
    /*val cond: Int => Boolean = v => (v >= 1 && v <= 3)

    lazy val zioEff = for {
      value <- zio.random.nextIntBetween(1, 11)
      _ <- zio.console.putStrLn(value.toString)
    } yield value

    module3.zio_homework.doWhile(zioEff, cond).exitCode*/

    // 3 задание
    //module3.zio_homework.loadConfigOrDefault.exitCode
    // 4 задание
    //module3.zio_homework.app.exitCode
    //module3.zio_homework.appSpeedUp.exitCode

    // 5,6 задание
    module3.zio_homework.runApp.exitCode

  }
}
