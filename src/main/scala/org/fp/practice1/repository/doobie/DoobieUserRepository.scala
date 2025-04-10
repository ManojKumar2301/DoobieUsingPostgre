package org.fp.practice1.repository.doobie

import org.fp.practice1.domain.User
import org.fp.practice1.repository.UserRepository
import doobie._
import doobie.implicits._
import cats.effect.IO

class DoobieUserRepository(xa: Transactor[IO]) extends UserRepository {

  override def createUsers(users: List[User]): IO[Int] = {
    val insertMany = Update[User]("INSERT INTO users (id, name, email) VALUES (?, ?, ?)")
    insertMany.updateMany(users)
      .transact(xa)
      .handleErrorWith { ex =>
        IO.println(s"Failed to insert multiple users: ${ex.getMessage}") *> IO.pure(0)
      }
  }

  override def getUserById(id: Long): IO[Option[User]] = {
    sql"""SELECT id, name, email FROM users WHERE id = $id"""
      .query[User]
      .option
      .transact(xa)
      .handleErrorWith { ex =>
        IO.println(s"Failed to fetch user: ${ex.getMessage}") *> IO.pure(None)
      }
  }

  override def getAllUsers: IO[List[User]] = {
    sql"""SELECT id, name, email FROM users"""
      .query[User]
      .to[List]
      .transact(xa)
      .attempt.flatMap {
        case Right(users) => IO.pure(users)
        case Left(error) =>
          IO.println(s"Failed to retrieve users: ${error.getMessage}") *> IO.pure(Nil)
      }
  }
}
