import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

def fullSequence[A](futures: List[Future[A]])
                   (implicit ex: ExecutionContext): Future[(List[A], List[Throwable])] =

Future.sequence(
  futures.map { future =>
    future.map(Success(_)).recover { case x => Failure(x)}
  }
).map { futures =>
  futures.map {
    case Failure(exception) =>
    case Success(value) =>
  }
  val listA: List[A] = futures.filter(p => p.isSuccess).map(p => p.get)
  val listT: List[Throwable] = futures.filter(p => p.isFailure).map(p => p.failed.get)
  (listA, listT)
}




