
ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "DoobiePostgresExample",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core"      % "1.0.0-RC8",
      "org.tpolecat" %% "doobie-postgres"  % "1.0.0-RC8",
      "org.tpolecat" %% "doobie-hikari"    % "1.0.0-RC8",
      "org.typelevel" %% "cats-effect"     % "3.6.1"  ,
      "org.postgresql" % "postgresql"      % "42.7.5",
      "org.slf4j" % "slf4j-simple"         % "2.0.17",


    )
  )
