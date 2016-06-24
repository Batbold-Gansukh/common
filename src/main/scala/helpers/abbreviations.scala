package helpers

import cats.data.Xor
import monix.eval.Task

import scala.concurrent.Future

/**
  * Created by batbold on 5/20/16.
  */
object abbreviations {

  // For scala future

  def FXLT(error: String): Future[Xor.Left[Throwable]] = Future.successful(Xor.Left(new Throwable(error)))

  def XLT(error: String): Xor.Left[Throwable] = Xor.Left(new Throwable(error))

  def FXT[R](error: String): Future[Throwable Xor R] = Future.successful(Xor.left[Throwable, R](new Throwable(error)))

  def XT[R](error: String): Throwable Xor R = Xor.left[Throwable, R](new Throwable(error))

  def FXL[T](value: T): Future[Xor.Left[T]] = Future.successful(Xor.Left(value))

  def XL[T](value: T): Xor.Left[T] = Xor.Left(value)

  def FX[L, R](value: L): Future[L Xor R] = Future.successful(Xor.left[L, R](value))

  def X[L, R](value: L): L Xor R = Xor.left[L, R](value)

  def FS[V](value: V): Future[V] = Future.successful(value)

  // For monix task

  def TXLT(error: String): Task[Xor.Left[Throwable]] = Task.now(Xor.Left(new Throwable(error)))

  def TXT[R](error: String): Task[Throwable Xor R] = Task.now(Xor.left[Throwable, R](new Throwable(error)))

  def TXL[T](value: T): Task[Xor.Left[T]] = Task.now(Xor.Left(value))

  def TX[L, R](value: L): Task[L Xor R] = Task.now(Xor.left[L, R](value))

  def FT[T](future: ⇒ Future[T]): Task[T] = Task.defer(Task.fromFuture(future))

  def TT[T](str: String): Task[T] = Task.raiseError[T](new Throwable(str))

}
