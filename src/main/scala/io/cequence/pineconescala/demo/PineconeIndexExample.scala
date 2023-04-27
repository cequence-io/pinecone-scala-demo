package io.cequence.pineconescala.demo

import akka.actor.ActorSystem
import akka.stream.Materializer
import io.cequence.pineconescala.domain.PodType
import io.cequence.pineconescala.service.PineconeIndexServiceFactory

import scala.concurrent.ExecutionContext

// run me - env. variables PINECONE_SCALA_CLIENT_API_KEY and PINECONE_SCALA_CLIENT_ENV must be set
object PineconeIndexExample extends App {

  implicit val ec = ExecutionContext.global
  implicit val materializer = Materializer(ActorSystem())

  private val service = PineconeIndexServiceFactory()

  private val indexName = "auto-gpt-test"

  {
    for {
      // create index
      _ <- service.createIndex(
        name = indexName,
        dimension = 1536
      ).map { response =>
        println(s"Create index response: ${response}")
      }

      // list indexes
      _ <- service.listIndexes.map { indexes =>
        println(s"The following indexes exist: ${indexes.mkString(", ")}")
      }

      // describe index (option is returned)
      _ <- service.describeIndex(indexName).map { indexInfo =>
        println(s"Index '${indexName}'info: ${indexInfo}")
      }

      // configure index
      _ <- service.configureIndex(
        "auto-gpt-test",
        replicas = Some(0),
        podType = Some(PodType.p1_x1)
      ).map { response =>
        println(s"Configure index response: ${response}")
      }

      // describe index after re-configuring (update)
      _ <- service.describeIndex(indexName).map { indexInfo =>
        println(s"Index '${indexName}' info after reconfiguring: ${indexInfo}")
      }

      // wait a bit
      _ = {
        println("Waiting 20 seconds for index to be ready")
        Thread.sleep(20000)
      }

      // describe index after waiting
      _ <- service.describeIndex(indexName).map { indexInfo =>
        println(s"Index '${indexName}' info after waiting 20 seconds: ${indexInfo}")
      }

      // delete index
      _ <- service.deleteIndex(indexName).map { response =>
        println(s"Delete index response: ${response}")
      }

      // describe index after deletion
      _ <- service.describeIndex(indexName).map { indexInfo =>
        println(s"Index '${indexName}' info after deletion: ${indexInfo}")
      }

      // re-create index
      _ <- service.createIndex(
        name = indexName,
        dimension = 1536
      ).map { response =>
        println(s"Create index response: ${response}")
      }

      // wait a bit
      _ = {
        println("Waiting 40 seconds for index to be ready before creating a collection")
        Thread.sleep(40000)
      }

      // describe index after waiting
      _ <- service.describeIndex(indexName).map { indexInfo =>
        println(s"Index '${indexName}' info after waiting 40 seconds: ${indexInfo}")
      }

      // create collection
      _ <- service.createCollection("auto-gpt-test-collection", "auto-gpt-test").map { response =>
        println(s"Create collection response: ${response}")
      }

      // list collections (at least one should be available)
      _ <- service.listCollections.map(collectionNames =>
        println(s"Available collections: ${collectionNames.mkString(", ")}")
      )

      // describe collection (option is returned)
      _ <- service.describeCollection("auto-gpt-test-collection").map { collectionInfo =>
        println(s"Collection info: ${collectionInfo}")
      }

      // delete collection
      _ <- service.deleteCollection("auto-gpt-test-collection").map(response =>
        println(s"Delete collection response: ${response}")
      )

      // wait a bit
      _ = {
        println("Waiting 20 seconds for deletion to complete")
        Thread.sleep(20000)
      }

      // list collections after delete
      _ <- service.listCollections.map(collectionNames =>
        println(s"Available collections (after delete): ${collectionNames.mkString(", ")}")
      )
    } yield {
      System.exit(0)
    }
  } recover {
    case e: Throwable =>
      println(e)
      System.exit(1)
  }
}