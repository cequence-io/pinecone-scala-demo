organization := "io.cequence"

name := "pinecone-scala-demo"

version := "0.0.1"

scalaVersion := "2.12.15" // "2.13.10" // "2.12.15" // "3.2.2" // "2.12.15"  //  // "2.13.10"

libraryDependencies ++= Seq(
  "io.cequence" %% "pinecone-scala-client" % version.value
)
