package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import models.{Pageable, Page}

object Workflow extends Controller {

  val pageSize = 2

  def list(pageNum: Int, orderBy: Int, filter: String = "") = Action {
	Ok(workflow.list(Activiti.processList(Pageable(pageNum, pageSize, orderBy), filter).autoLink(
	    (newPageNum: Int, newPageSize: Int, newOrderBy: Int) => 
          routes.Workflow.list(newPageNum, newOrderBy, filter).toString
	), filter))
  }
  
}