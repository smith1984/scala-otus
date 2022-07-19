package module1

object App {
  def main(args: Array[String]): Unit = {
//    def sumItUp: Int = {
//      def one(x: Int): Int = { return x; 1 }
//      val two = (x: Int) => { return x; 2 }
//      1 + one(2) + two(11)
//    }
//
//    println(sumItUp)
//
//    println("Hello world")

    functions.filteredEvent(Array(1,2,3,4,5,6)).foreach(println)
    functions.filteredOdd(Array(1,2,3,4,5,6)).foreach(println)

    (0 to 10).toList.foreach( x => println(recursion.fiboTailRec(x)))
  }
}
