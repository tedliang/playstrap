package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

case class User(id: Option[Long], username: String, password: String, email: String){
  def roles = UserRole.right(id)
}

object Users extends IdTable[User]("User") {
  def username = column[String]("username", O.NotNull)
  def password = column[String]("password")
  def email = column[String]("email")
  def * = id.? ~ username ~ password ~ email <> (User.apply _, User.unapply _)
}

object UserRole extends ManyToManyTable[User, Role]("UserRole", Users, Roles)