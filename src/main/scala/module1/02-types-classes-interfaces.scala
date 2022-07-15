package module1

import sun.security.util.Password

import java.io.{Closeable, File}
import scala.io.{BufferedSource, Source}


object type_system {

  /**
   * Scala type system
   *
   */

  // AnyVal

  //


  // Unit



  // Null


  // Nothing

  def absurd(v: Nothing) = ???


  // Generics

  // работа с ресурсом

  lazy val file: File = ???

  lazy val source = ???

  lazy val lines = ???



  // ограничения связанные с дженериками


  /**
   *
   * class
   *
   * конструкторы / поля / методы / компаньоны
   */



  /**
   * Задание 1: Создать класс "Прямоугольник"(Rectangle), мы должны иметь возможность создавать прямоугольник с заданной
   * длиной(length) и шириной(width), а также вычислять его периметр и площадь
   *
   */


  /**
   * object
   *
   * 1. Паттерн одиночка
   * 2. Линивая инициализация
   * 3. Могут быть компаньоны
   */




  /**
   * case class
   *
   */


  // создать case класс кредитная карта с двумя полями номер и cvc






  /**
   * case object
   *
   * Используются для создания перечислений или же в качестве сообщений для Акторов
   */



  /**
   * trait
   *
   */


  class A {
    def foo() = "A"
  }

  trait B extends A {
    override def foo() = "B" + super.foo()
  }

  trait C extends B {
    override def foo() = "C" + super.foo()
  }

  trait D extends A {
    override def foo() = "D" + super.foo()
  }

  trait E extends C {
    override def foo(): String = "E" + super.foo()
  }

  // A -> D -> B -> C
  val v = new A with D with C with B // CBDA

  // A -> B -> C -> E -> D
  val v1 = new A with E with D with C with B // DECBA







  /**
   * Value classes и Universal traits
   */


}