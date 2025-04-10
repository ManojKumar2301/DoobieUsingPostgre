package org.fp.practice1.repository

import org.fp.practice1.domain.User
import cats.effect.IO

trait UserRepository {
  def createUsers(users: List[User]): IO[Int]
  def getUserById(id: Long): IO[Option[User]]
  def getAllUsers: IO[List[User]]
}
