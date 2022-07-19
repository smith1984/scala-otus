object Collect {
  def main(args: Array[String]): Unit = {
    case class Car(marke: String, model: String, year: Int)
    val cars = Car("VW", "id3", 2020) :: "xdfg" :: Car("Lexus", "NX", 2010) :: Car("BMW", "3", 2001) :: "sad" :: Nil

    cars.collect {
      case x: Car => x.model
      case y:String => "111"
    }.foreach(x=> println(x.toString))
  }
}
