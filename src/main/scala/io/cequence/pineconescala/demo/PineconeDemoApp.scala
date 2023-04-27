package io.cequence.pineconescala.demo

import akka.actor.ActorSystem
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}
import io.cequence.pineconescala.service.{PineconeIndexService, PineconeIndexServiceFactory, PineconeVectorService, PineconeVectorServiceFactory}

trait PineconeDemoApp extends App {

  protected implicit val ec: ExecutionContext = ExecutionContext.global

  private val actorSystem: ActorSystem = ActorSystem()
  private implicit val materializer: Materializer = Materializer(actorSystem)

  protected def execWithIndexService: PineconeIndexService => Future[_]
  protected def execWithVectorService: (String => Future[PineconeVectorService]) => Future[_]

  {
    for {
      pineconeIndexService <- Future(
        PineconeIndexServiceFactory() // we wrap it in a Future just because of the recover block
      )

      _ <- execWithIndexService(pineconeIndexService)

      _ <- execWithVectorService((indexName: String) =>
        PineconeVectorServiceFactory(indexName).map(
          _.getOrElse(
            throw new Exception(s"Could not find index '${indexName}'")
          )
        )
      )

      _ <- actorSystem.terminate()
    } yield
      System.exit(0)
  } recover {
    case e: Exception =>
      e.printStackTrace()
      System.exit(1)
  }
}
