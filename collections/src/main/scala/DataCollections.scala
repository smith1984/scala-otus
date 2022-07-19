object DataCollections {
  def main(args: Array[String]): Unit = {
    println("demo of foreach")
    val demoCollection = ("line 1"::"line 2"::"line 3"::"line 1"::"line 2"::Nil).toSet
    val demo1Collection = (1::2::3::1::2::3::Nil).groupBy(x=>x).map(x=>x._1)

    demoCollection.foreach(x => println(s"collection ${x}"))

    println("demo of iterable");
    val iter = demoCollection.iterator
    while (iter.hasNext){
      println(s"collection ${iter.next()}")
    }

    println("demo of functions");
    println("fold, foldLeft, foldRight");
    println(s"fold result: ${demo1Collection.fold(0)((z,i) => z + i)}")
    println(s"fold left result: ${demo1Collection.foldLeft(5) ((z,i) => z + i)}")
    println(s"reduce result: ${demo1Collection.reduce((z,i) => z + i)}")


//    val test = List(1,2,3,4,5) :: List(1,50,3):: List(1,2) :: Nil
//    println(s"flat map result ${test.flatMap(_.toList).mkString(";")}")

//    test.filter(x=>x.reduce((y,z)=>y+z) > 10).foreach(x => println(x.mkString(",")));
  }
}
