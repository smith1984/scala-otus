import scala.collection.MapView

object HomeWork2 extends App {

  //Урна с шариками
  val box = 1 :: 0 :: 1 :: 0 :: 1 :: 0 :: Nil

  //Количество экспериментов
  val cnt = 10000

  //Функция эксперимент возвращает два булева значения
  // 1 true если первый шар черный
  // 2 true если второй шар белый при чёрном первом шаре
  def experiment(box: List[Int]): (Boolean, Boolean) = {
    val r = new scala.util.Random
    //если при первой попытки вытянули чёрный шар, то продолжаем эксперимент, в проитвно случае (false, false)
    if (box(r.nextInt(6)) == 0) {
      //Получаем новое состояние урны, удалив 1 черный шар
      val newStateBox = box.splitAt(1)._1 ++ box.splitAt(1)._2.tail

      //если из оставшихся 5 шаров вытянули белый, то (true, true), в противном случае (true, false)
      if (newStateBox(r.nextInt(5)) == 1)
        (true, true)
      else
        (true, false)
    }
    else (false, false)
  }

  //Создаёмпоследовательность для экспериментов
  val results: MapView[(Boolean, Boolean), Int] = (1 to cnt).
    map(exp => experiment(box)). //проводим эксперименты
    groupBy(identity).
    view.
    mapValues(_.size) //подсчитываем количество успешных и не успешных экспериментов

  //Выводим результат должен стримится к 0.6 (3/5) при увеличении количества проведённых экспериментов
  //Применяем формулу условной вероятности делим количество удачных экспериментов на количество успешно вытянутого черного шара в первый раз
  val p_B_A = results(true, true).toDouble / (results(true, true) + results(true, false))

  println(p_B_A) // 0.5987542696403456

}
