package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import views.helper.Pagination

object Workflow extends Controller {

  val pageSize = 2

  def list(page: Int, orderBy: Int, filter: String = "%") = Action {
	val (items, total) = Activiti.processList(page, pageSize, filter, orderBy); 
    Ok(workflow.list(items,
      new Pagination(page, pageSize, filter, orderBy, items.length, total){
        override def link(newPage: Int, newOrderBy: Int) = 
          routes.Workflow.list(newPage, newOrderBy, filter)
      }))
  }
  
}