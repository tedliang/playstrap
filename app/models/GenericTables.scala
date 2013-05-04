package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

abstract class IdTable[T](_schemaName: Option[String], _tableName: String) extends Table[T](_schemaName, _tableName) {
  def this(_tableName: String) = this(None, _tableName)
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def autoInc = * returning id
  def byId = createFinderBy(_.id)
  
  def findById(id: Long): Option[T] = DB.withSession { implicit session =>
    byId(id).firstOption
  }
  def insert(t: T) = DB.withSession { implicit session => 
    autoInc insert t
  }
  
  def insertAll(ts: T*) = DB.withSession { implicit session => 
   	ts map insert
  }

  def count: Int = DB.withSession { implicit session => 
    Query(this.length).first
  }

  def findAll = DB.withSession { implicit session => 
    Query(this).list
  }
}

abstract class ManyToManyTable[L, R](_schemaName: Option[String], _tableName: String, 
    Left: IdTable[L], Right: IdTable[R]) extends Table[(Long,Long)](_schemaName, _tableName) {
  
  def this(_tableName: String, Left: IdTable[L], Right: IdTable[R]) = this(None, _tableName, Left, Right)
  def lId = column[Long](Left.tableName.take(1).toLowerCase + Left.tableName.drop(1)+"Id")
  def rId = column[Long](Right.tableName.take(1).toLowerCase + Right.tableName.drop(1)+"Id")
  
  def lFK = foreignKey(Left.tableName+"_fk", lId, Left)(l => l.id)
  def rFK = foreignKey(Right.tableName+"_fk", rId, Right)(r => r.id)
  
  def pk = primaryKey(_tableName+"_pk", (lId, rId))
  def * = lId ~ rId
  
  def insert(lId: Long, rId: Long) = DB.withSession { implicit session => 
    *.insert(lId, rId)
  }
  
  def left(rId: Option[Long]) = DB.withSession { implicit session => 
    this.filter(_.rId === rId).flatMap(_.lFK).list
  }

  def right(lId: Option[Long]) = DB.withSession { implicit session => 
    this.filter(_.lId === lId).flatMap(_.rFK).list
  }
  
  def count: Int = DB.withSession { implicit session => 
    Query(this.length).first
  }
  
}
