name := "play-modules-redis"
organization := "com.lifeway"

crossScalaVersions := Seq("2.11.8")
scalaVersion := "2.11.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-encoding", "UTF-8")
scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "com.typesafe.play"         %% "play"               % "2.5.3"     % "provided",
  "com.typesafe.play"         %% "play-test"          % "2.5.3"     % "test",
  "com.typesafe.play"         %% "play-specs2"        % "2.5.3"     % "test",
  "com.typesafe.play"         %% "play-cache"         % "2.5.3",
  "biz.source_code"           %  "base64coder"        % "2010-12-19",
  "org.sedis"                 %% "sedis"              % "1.2.2"
)

resolvers ++= Seq(
  "pk11 repo" at "http://pk11-scratch.googlecode.com/svn/trunk",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)
