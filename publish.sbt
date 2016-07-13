publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := {
  <scm>
    <url>git@github.com:lifeway/play-redis.git</url>
    <connection>scm:git:git@github.com:lifeway/play-redis.git</connection>
  </scm>
    <developers>
      <developer>
        <id>typesafe</id>
        <name>Typesafe</name>
        <url>https://typesafe.com</url>
      </developer>
      <developer>
        <id>lifeway</id>
        <name>LifeWay Christian Resources</name>
        <url>https://www.lifeway.com</url>
      </developer>
    </developers>
}
pomIncludeRepository := { _ => false }
homepage := Some(url(s"https://github.com/lifeway/play-redis"))
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

sonatypeProfileName := "com.lifeway"
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseTagName := s"${(version in ThisBuild).value}"
releaseCrossBuild := true

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
