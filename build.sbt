crossScalaVersions := Seq("2.10.4", "2.11.8")

val autowire = crossProject.settings(
  organization := "com.lihaoyi",

  version := "0.2.5",
  name := "autowire",
  scalaVersion := "2.11.8",
  autoCompilerPlugins := true,
  addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.4"),
  scalacOptions ++=  Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xfatal-warnings", // warning -> error
//    "-Xstrict-inference", // don't infer unsound types
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
    "-Ywarn-dead-code",// Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
//    "-Ywarn-unused", // Warn when local and private vals, vars, defs, and types are are unused
    "-Ywarn-unused-import", // Warn when imports are unused
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
    "-Xlint:_", // linter on all
    "-language:higherKinds", // Enabled higherkinds
    "-language:existentials",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-Xmax-classfile-name", "240", // see https://goo.gl/QABCCL
    "-Xfuture", // behave like the next major version, where possible, to alert you to upcoming breaking changes.
    "-target:jvm-1.8"
  ),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "acyclic" % "0.1.4" % "provided",
    "com.lihaoyi" %%% "utest" % "0.4.3" % "test",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.lihaoyi" %%% "upickle" % "0.4.1" % "test"
  ) ++ (
    if (scalaVersion.value startsWith "2.11.") Nil
    else Seq(
      compilerPlugin("org.scalamacros" % s"paradise" % "2.0.0" cross CrossVersion.full),
      "org.scalamacros" %% "quasiquotes" % "2.0.0"
    )
    ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  // Sonatype
  publishArtifact in Test := false,
  publishTo := Some("releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),

  pomExtra :=
    <url>https://github.com/lihaoyi/ajax</url>
      <licenses>
        <license>
          <name>MIT license</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
        </license>
      </licenses>
      <scm>
        <url>git://github.com/lihaoyi/ajax.git</url>
        <connection>scm:git://github.com/lihaoyi/ajax.git</connection>
      </scm>
      <developers>
        <developer>
          <id>lihaoyi</id>
          <name>Li Haoyi</name>
          <url>https://github.com/lihaoyi</url>
        </developer>
      </developers>
).jsSettings(
    resolvers ++= Seq(
      "bintray-alexander_myltsev" at "http://dl.bintray.com/content/alexander-myltsev/maven"
    ),
    scalaJSStage in Test := FullOptStage
).jvmSettings(
  resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies ++= Seq(
    "org.scala-lang" %% "scala-pickling" % "0.9.1" % "test",
    "com.esotericsoftware.kryo" % "kryo" % "2.24.0" % "test",
    "com.typesafe.play" %% "play-json" % "2.4.8" % "test"
  )
)

lazy val autowireJS = autowire.js
lazy val autowireJVM = autowire.jvm
