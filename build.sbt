
lazy val `inventory` = (project in file("."))
  .aggregate(
    `user-api`, `user-impl`,
    `setting-api`, `setting-impl`,
    `materials-api`, `materials-impl`,
    `web-gateway`
  )
  .settings(commonSettings: _*)


organization in ThisBuild := "com.dream"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

autoCompilerPlugins := true

val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "4.0.0"
val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
val mockito = "org.mockito" % "mockito-core" % "2.22.0" % Test
val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
val mysqlConnectJava = "mysql" % "mysql-connector-java" % "5.1.42"
val cats = "org.typelevel" %% "cats-core" % "1.4.0"
val enumeratum = "com.beachape" %% "enumeratum" % "1.5.13"
val enumeratumPlay = "com.beachape" %% "enumeratum-play-json" % "1.5.14"
val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.14.0"

def commonSettings: Seq[Setting[_]] = Seq(
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    simulacrum
  )
)

lazy val security = (project in file("security"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      scalaTest,
      playJsonDerivedCodecs
    )
  )


lazy val common = (project in file("common"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      cats,
      enumeratum,
      enumeratumPlay,
      scalaTest,
      playJsonDerivedCodecs
    )
  )


lazy val `common-dao` = (project in file("common-dao"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      lagomScaladslPersistenceJdbc,
      playJsonDerivedCodecs,
      scalaTest
    )
  )

lazy val `user-api` = (project in file("user-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)

lazy val `user-impl` = (project in file("user-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`user-api`)


lazy val `setting-api` = (project in file("setting-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)


lazy val `setting-impl` = (project in file("setting-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`setting-api`, common, `common-dao`)

/*
lazy val `billing-api` = (project in file("billing-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)


lazy val `billing-impl` = (project in file("billing-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      cats,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`billing-api`, `common-dao`)


lazy val `accounting-api` = (project in file("accounting-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)


lazy val `accounting-impl` = (project in file("accounting-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      cats,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`accounting-api`, `common-dao`)


lazy val `purchasing-api` = (project in file("purchasing-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)


lazy val `purchasing-impl` = (project in file("purchasing-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      cats,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`purchasing-api`, `common-dao`)


lazy val `manufacturing-api` = (project in file("manufacturing-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)


lazy val `manufacturing-impl` = (project in file("manufacturing-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      cats,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`manufacturing-api`, `common-dao`)

*/

lazy val `materials-api` = (project in file("materials-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(security, common)



lazy val `materials-impl` = (project in file("materials-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      mysqlConnectJava,
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslPersistenceJdbc,
      cats,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`materials-api`, `common-dao`)


lazy val `web-gateway` = (project in file("web-gateway"))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala, LagomPlay, SbtReactiveAppPlugin)
  .dependsOn(`user-api`, `materials-api`, `setting-api`, security)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
      scalaTestPlusPlay,
      scalaTest,
      mockito,

      "org.ocpsoft.prettytime" % "prettytime" % "3.2.7.Final",

      "org.webjars" % "foundation" % "6.2.3",
      "org.webjars" % "foundation-icon-fonts" % "d596a3cfb3"
    ),
    EclipseKeys.preTasks := Seq(compile in Compile),
    httpIngressPaths := Seq("/")
  )

scalacOptions += "-Ypartial-unification"

lagomCassandraCleanOnStart in ThisBuild := true

// ------------------------------------------------------------------------------------------------

// register 'elastic-search' as an unmanaged service on the service locator so that at 'runAll' our code
// will resolve 'elastic-search' and use it. See also com.example.com.ElasticSearch
lagomUnmanagedServices in ThisBuild += ("elastic-search" -> "http://127.0.0.1:9200")