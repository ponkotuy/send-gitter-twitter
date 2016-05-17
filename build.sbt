
name := "SendGitterAndTwitter"

scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.github.amatkivskiy" % "gitter.sdk.sync" % "1.5.1",
  "org.twitter4j" % "twitter4j-core" % "[4.0,)"
)
