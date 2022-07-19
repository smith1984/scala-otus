object  MonadaRules {

  def squareFunction(x: Int): Option[Int] = Some(x * x)

  def incrementFunction(x: Int): Option[Int] = Some(x + 1)


  def leftUnitLaw() {
    val x = 5
    val monad: Option[Int] = Some(x)
    val result = monad.flatMap(squareFunction) == squareFunction(x)
    println(result)
  }

  def rightUnitLaw() {
    val x = 5
    val monad: Option[Int] = Some(x)
    val result = monad.flatMap(x => Some(x)) == monad
    println(result)
  }

  def associativityLaw() {
    val x = 5
    val monad: Option[Int] = Some(x)
    val left = monad flatMap squareFunction flatMap incrementFunction
    val right = monad flatMap (x => squareFunction(x) flatMap incrementFunction)
    assert(left == right)
  }

  def main(args: Array[String]): Unit = {
    leftUnitLaw
    rightUnitLaw
    associativityLaw
  }

}