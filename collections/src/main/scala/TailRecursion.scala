import scala.annotation.tailrec

object TailRecursion {
  def main(args: Array[String]): Unit = {
    val demoCollection = "line 1" :: "line 2" :: "line 3" :: Nil

    val f = tailRecursiveLength(demoCollection, 0)
    println(s"collection size ${f}")
  }

  @tailrec
  def tailRecursiveLength(list: List[String], accumulator: Long): Long = list match {
    case Nil => accumulator
    case head :: tail => tailRecursiveLength(tail, accumulator + 1)
  }
}
