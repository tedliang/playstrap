package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import views.helper.Page

object Workflow extends Controller {

  val pageSize = 2

  def list(pageNum: Int, orderBy: Int, filter: String = "%") = Action {
	val (items, total) = Activiti.processList(pageNum, pageSize, filter, orderBy); 
    Ok(workflow.list(
      new Page(pageNum, pageSize, filter, orderBy, items, total){
        override def link(newPage: Int, newOrderBy: Int) = 
          routes.Workflow.list(newPage, newOrderBy, filter)
      }))
  }
  
}