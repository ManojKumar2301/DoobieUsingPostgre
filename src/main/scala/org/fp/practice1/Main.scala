package org.fp.practice1

import cats.effect.{IO, IOApp, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import org.fp.practice1.repository.doobie.DoobieUserRepository
import org.fp.practice1.domain.User

object Main extends IOApp.Simple {

  def createTransactor: Resource[IO, HikariTransactor[IO]] =
    HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:5432/doobie_database",
      "manoj",
      "Manoj@2002",
      scala.concurrent.ExecutionContext.parasitic
    )

  val createTable: doobie.ConnectionIO[Int] =
    sql"""
      CREATE TABLE IF NOT EXISTS users (
        id BIGINT PRIMARY KEY,
        name TEXT NOT NULL,
        email TEXT NOT NULL
      )
    """.update.run

  override def run: IO[Unit] = {
    createTransactor.use { xa =>
      val userRepo = new DoobieUserRepository(xa)
      val multipleUsers = List(
        User(2L, "Koti", "koti@example.com"),
        User(3L, "Vinay", "vinay@example.com"),
        User(4L, "Mahesh", "mahesh@example.com")
      )

      for {
        _ <- createTable.transact(xa)
        _ <- userRepo.createUsers(multipleUsers)
        userOpt <- userRepo.getUserById(3L)
        _ <- IO.println(s"Fetched user: $userOpt")
        allUsers <- userRepo.getAllUsers
        _ <- IO.println(s"All users: $allUsers")
      } yield ()

    }
  }
}
