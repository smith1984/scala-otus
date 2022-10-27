package module3

import module3.zioConcurrency.{currentTime, printEffectRunningTime}
import module3.zio_homework.config.AppConfig
import zio.{Has, RIO, Task, ULayer, URIO, ZIO, ZLayer}
import zio.clock.{Clock, sleep}
import zio.console._
import zio.duration.durationInt
import zio.macros.accessible
import zio.random._

import java.io.IOException
import java.util.concurrent.TimeUnit
import scala.io.StdIn
import scala.language.postfixOps

package object zio_homework {
  /**
   * 1.
   * Используя сервисы Random и Console, напишите консольную ZIO программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в когнсоль угадал или нет. Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */

  lazy val readLine: ZIO[Console, IOException, String] = getStrLn
  lazy val readInt: RIO[Console, Int] = readLine.flatMap(str => ZIO.effect(str.toInt))

  lazy val readIntOrRetry: RIO[Console, Int] = readInt.orElse(
    ZIO.effect(println("Не корректный ввод, попробуй еще:")) *> readIntOrRetry
  )

  def validatedBound(down: Int, up: Int): RIO[Console, Int] = {
    readIntOrRetry.flatMap(g => if (g >= down && g <= up) ZIO.succeed(g)
    else
      ZIO.fail(new Exception(s"Введённое число выходит за пределы ожидаемых значений [$down, $up]")))
  }

  def validBoundOrRetry(down: Int, up: Int): RIO[Console, Int] = validatedBound(down, up).orElse(
    putStrLn(s"Введённое число выходит за пределы ожидаемых значений [$down, $up]\nПопробуй ещё раз:")
      *> validatedBound(down, up))


  def getRandomInt(down: Int, up: Int): RIO[Random, Int] = nextIntBetween(down, up + 1)


  def guessInRange(down: Int, up: Int): RIO[Console with Random, Unit] = for {
    _ <- putStrLn(s"Введите число от  $down до $up:")
    rInt <- getRandomInt(down, up)
    guess <- validBoundOrRetry(down, up)
    _ <- putStrLn(if (rInt == guess) "Вы угадали" else s"Вы не угадали, загаданное число $rInt")
  } yield ()


  lazy val guessProgram = guessInRange(1, 3)

  /**
   * 2. реализовать функцию doWhile (общего назначения), которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   * 
   */

  def doWhile[R, E, A] (zioEff: ZIO[R, E, A], cond: A => Boolean): ZIO[R, E, A] =
    zioEff.flatMap(value =>
      if(cond(value))
        ZIO.succeed(value)
      else
        doWhile(zioEff, cond))

  /**
   * 3. Реализовать метод, который безопасно прочитает конфиг из файла, а в случае ошибки вернет дефолтный конфиг
   * и выведет его в консоль
   * Используйте эффект "load" из пакета config
   */


  def loadConfigOrDefault = for {
    conf <- module3.zio_homework.config.load.orElse(Task.succeed(AppConfig("www.example.com", "80")))
    _ <- putStrLn(conf.toString)
  } yield (conf)

  /**
   * 4. Следуйте инструкциям ниже для написания 2-х ZIO программ,
   * обратите внимание на сигнатуры эффектов, которые будут у вас получаться,
   * на изменение этих сигнатур
   */


  /**
   * 4.1 Создайте эффект, который будет возвращать случайеым образом выбранное число от 0 до 10 спустя 1 секунду
   * Используйте сервис zio Random
   */
  lazy val eff: URIO[Random with Clock, Int] = ZIO.sleep(1 second) *> zio.random.nextIntBetween(0, 10)

  /**
   * 4.2 Создайте коллукцию из 10 выше описанных эффектов (eff)
   */
  lazy val effects: List[URIO[Random with Clock, Int]] = (1 to 10).map( _ => eff).toList


  /**
   * 4.3 Напишите программу которая вычислит сумму элементов коллекци "effects",
   * напечатает ее в консоль и вернет результат, а также залогирует затраченное время на выполнение,
   * можно использовать ф-цию printEffectRunningTime, которую мы разработали на занятиях
   */

  def showAndReturnEffect[R, E, A](eff: ZIO[R, E, A]) = for{
    r <- eff
    _ <- putStrLn(r.toString)
  } yield r

  lazy val app = printEffectRunningTime(showAndReturnEffect(ZIO.collectAll(effects).map(_.sum)))


  /**
   * 4.4 Усовершенствуйте программу 4.3 так, чтобы минимизировать время ее выполнения
   */

  lazy val appSpeedUp = printEffectRunningTime(showAndReturnEffect(ZIO.collectAllPar(effects).map(_.sum)))


  /**
   * 5. Оформите ф-цию printEffectRunningTime разработанную на занятиях в отдельный сервис, так чтобы ее
   * молжно было использовать аналогично zio.console.putStrLn например
   */
  type PrintEffectService = Has[PrintEffectService.Service]

  @accessible
  object PrintEffectService{
    trait Service{
      def printEffectRunningTime[R, E, A](effect: ZIO[R, E, A]): ZIO[Console with Clock with R, E, A]
    }

    class ServiceImpl extends Service{
      override def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[Console with Clock with R, E, A] = for{
        start <- currentTime
        z <- zio
        finish <- currentTime
        _ <- putStrLn(s"Running time(result from service): ${finish - start}")
      } yield z
    }

    val live: ULayer[PrintEffectService] = ZLayer.succeed(new ServiceImpl)
  }

   /**
     * 6.
     * Воспользуйтесь написанным сервисом, чтобы созадть эффект, который будет логировать время выполнения прогаммы из пункта 4.3
     *
     * 
     */

  lazy val appWithTimeLogg: ZIO[PrintEffectService with Console with Clock with Random, Throwable, Int] =
    PrintEffectService.printEffectRunningTime(app)

  /**
    * 
    * Подготовьте его к запуску и затем запустите воспользовавшись ZioHomeWorkApp
    */

  lazy val runApp = appWithTimeLogg.provideSomeLayer[Console with Random with Clock](PrintEffectService.live)
  
}
