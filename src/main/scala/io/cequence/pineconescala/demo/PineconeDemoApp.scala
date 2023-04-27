package io.cequence.pineconescala.demo

import akka.actor.ActorSystem
import akka.stream.Materializer

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

trait PineconeDemoApp extends App {

  protected implicit val ec: ExecutionContext = ExecutionContext.global

  private val actorSystem: ActorSystem = ActorSystem()
  private implicit val materializer: Materializer = Materializer(actorSystem)

  protected val service = PineconeIndexServiceFactory()

  protected def exec: pineconeIndexService => Future[_]

  {
    for {
      _ <- exec(service)

      _ <- actorSystem.terminate()
    } yield
      System.exit(0)
  } recover {
    case e: Exception =>
      e.printStackTrace()
      System.exit(1)
  }
}
