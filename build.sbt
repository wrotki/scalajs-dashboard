import sbt.CrossVersion
//import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin

// https://github.com/scala-js/scala-js/issues/2797
// https://github.com/japgolly/misc/tree/webpack

name := "table-viewer"

version := "1.0"

scalaVersion in ThisBuild := Settings.versions.scala // "2.12.1"
scalacOptions in ThisBuild += "-Ypartial-unification"

//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)


val commonSettings = Seq(
  libraryDependencies ++= Settings.sharedDependencies.value,
  skip in packageJSDependencies := false,
  bintrayRepository := "maven-repo"
)

lazy val root = (project in file("."))

// a special crossProject for configuring a JS/JVM/shared structure
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings: _*)
  .settings(
    scalaVersion := Settings.versions.scala
  )
  .jsConfigure(_.enablePlugins(ScalaJSWeb, WebScalaJSBundlerPlugin))
//.jsConfigure(_.enablePlugins(ScalaJSWeb ,WebScalaJSBundlerPlugin))
// set up settings specific to the JS project
//enablePlugins(WebScalaJSBundlerPlugin)

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

// lazy val client = project.enablePlugins(ScalaJSPlugin, ScalaJSWeb, ScalaJSBundlerPlugin)
lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)
  .settings(commonSettings: _*)
  .settings(
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.CommonJSModule)
    },
    //scalaJSLinkerConfig ~= { _.withRelativizeSourceMapBase(None) },
    scalaJSLinkerConfig ~= {
      _.withSourceMap(false)
    },
    // https://github.com/scalacenter/scalajs-bundler/issues/114
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    scalaJSUseMainModuleInitializer := true,
    name := "client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    jsDependencies ++= Settings.jsDependencies.value,
    useYarn := true,
    npmDependencies in Compile ++= Settings.npmDependencies,
    // Use a different Webpack configuration file for production
    // https://github.com/scalacenter/scalajs-bundler/blob/master/manual/src/ornate/cookbook.md
    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "my.custom.webpack.config.js"),
    version in webpack := "3.12.0"
  )

lazy val server = (project in file("server"))
  .enablePlugins(PlayScala, WebScalaJSBundlerPlugin)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .dependsOn(sharedJVM)
  .settings(commonSettings: _*)
  .settings(
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.jvmDependencies.value,
    // yes, we want to package JS dependencies
    skip in packageJSDependencies := false,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    npmAssets ++= NpmAssets.ofProject(client) { modules => (modules / "bootstrap").allPaths }.value,
    npmAssets ++= NpmAssets.ofProject(client) { modules => (modules / "font-awesome").allPaths }.value,
    npmAssets ++= NpmAssets.ofProject(client) { modules => (modules / "elementalcss-bundle.js").allPaths }.value
    // compress CSS
    // LessKeys.compress in Assets := true
  )

// resolvers in server += Resolver.bintrayRepo("dwhjames", "maven")
resolvers in server += Resolver.bintrayRepo("ticofab", "maven")

libraryDependencies in server += guice

// loads the Play server project at sbt startup
onLoad in Global ~= (_ andThen ("project server" :: _))
