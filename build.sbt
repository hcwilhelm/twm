name := "TWM"

version := "0.1"

scalaVersion := "2.12.7"

scalacOptions := Seq("-feature", "-language:higherKinds", "-Xlog-implicits", "-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.4.0",
  "org.typelevel" %% "cats-mtl-core" % "0.4.0",
  "org.typelevel" %% "cats-effect" % "1.1.0"
)

libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC2"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-optics"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.18",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test
)