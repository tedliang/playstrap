package models

import play.api.Play.current

import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._

case class Role(id: Option[Long], name: String, description: Option[String]) extends IdEntity {
  def users = UserRole.left(id)
}

object Roles extends IdTable[Role]("Role") {
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.Nullable)
  def * = id.? ~ name ~ description.? <> (Role.apply _, Role.unapply _)
  override def forInsert = name ~ description.? <> (
    (n, d) => Role(None, n, d), 
    (r: Role) => Some((r.name, r.description)))

}