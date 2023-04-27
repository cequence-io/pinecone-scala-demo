package io.cequence.pineconescala.demo

import scala.concurrent.Future

// run me - env. variables PINECONE_SCALA_CLIENT_API_KEY and PINECONE_SCALA_CLIENT_ENV must be set
object ListIndexes extends PineconeDemoApp {
  override protected def execWithIndexService =
    _.listIndexes.map(
      _.foreach(println)
    )

  override protected def execWithVectorService =
    _ => Future(()) // no-op
}