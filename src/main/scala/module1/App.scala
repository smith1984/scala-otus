package module1

object App {
  def main(args: Array[String]): Unit = {
    println(s"Hello from " +
      s"${Thread.currentThread().getName}")

//    val t1 = new Thread{
//      override def run(): Unit = {
//        Thread.sleep(1000)
//        println(s"Hello from " +
//          s"${Thread.currentThread().getName}")
//      }
//    }
//    val t2 = new threads.Thread1
//    t1.start()
//    t1.join()
//    t2.start()

    def rates = {
      val t1 = threads.getRatesLocation5
      val t2 = threads.getRatesLocation6
      t1.onComplete{ i1 =>
        t2.onComplete{ i2 =>
          println(s"Result: ${i1 + i2}")
        }
      }

      val r: threads.ToyFuture[Int] = for{
        i1 <- t1
        i2 <- t2
      } yield  i1 + i2

      r.onComplete(println(_))
    }

    threads.printRunningTime(rates)

  }
}
