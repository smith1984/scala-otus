object Test {
  def main(args: Array[String]): Unit = {
    val col = List(10, 30, 1, 70, 2) :: List(1, 2, 3, 4, 5) :: List(1, 2, 3) :: Nil

    val filteredCollection = col.filter(x => x.sum > 10)
    filteredCollection.foreach(x => println(x.mkString(",")))
  }
}
