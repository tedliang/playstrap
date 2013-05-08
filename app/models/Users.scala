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
  
  def list(criteria: Searchable) = DB.withSession { implicit session => 
    new Page(criteria, 
        Query(this)
        .filter(_.username.toUpperCase like s"%${criteria.filter.toUpperCase}%")
        .sortBy(u => criteria.sort match {
          case 1 => u.id.asc
          case -1 => u.id.desc
          case 2 => u.username.asc
          case -2 => u.username.desc
          case 3 => u.email.asc
          case -3 => u.email.desc
        })
        .drop(criteria.offset)
        .take(criteria.size)
        .list,
        Query(this.length).first)
  }
  
}

object UserRole extends ManyToManyTable[User, Role]("UserRole", Users, Roles)