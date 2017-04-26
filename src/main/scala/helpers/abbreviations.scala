package helpers

import monix.eval.Task

import scala.concurrent.Future

/**
  * Created by batbold on 5/20/16.
  */
object abbreviations {

  // For scala future

  def FXLT(error: String): Future[Left[Throwable,_]] = Future.successful(Left(new Throwable(error)))

  def XLT(error: String): Left[Throwable,_] = Left(new Throwable(error))

  def FXT[R](error: String): Future[Throwable Either R] = Future.successful(Left[Throwable, R](new Throwable(error)))

  def XT[R](error: String): Throwable Either R = Left[Throwable, R](new Throwable(error))

  def FXL[T](value: T): Future[Left[T,_]] = Future.successful(Left(value))

  def XL[T](value: T): Left[T,_] = Left(value)

  def XR[R](value: R): Right[_,R] = Right(value)

  def FX[L, R](value: L): Future[L Either R] = Future.successful(Left[L, R](value))

  def X[L, R](value: L): L Either R = Left[L, R](value)

  def FS[V](value: V): Future[V] = Future.successful(value)

  // For monix task

  def TXLT(error: String): Task[Left[Throwable,_]] = Task.now(Left(new Throwable(error)))

  def TXT[R](error: String): Task[Throwable Either R] = Task.now(Left[Throwable, R](new Throwable(error)))

  def TXL[T](value: T): Task[Left[T,_]] = Task.now(Left(value))

  def TX[L, R](value: L): Task[L Either R] = Task.now(Left[L, R](value))

  def FT[T](future: ⇒ Future[T]): Task[T] = Task.defer(Task.fromFuture(future))

  def TT[T](str: String): Task[T] = Task.raiseError[T](new Throwable(str))

}
