package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import models.{Searchable, Page}

object Workflow extends Controller {

  def list(pageNum: Int, pageSize: Int, orderBy: Int, filter: String) = Action {
	Ok(workflow.list(Activiti.processList(new Searchable(pageNum, pageSize, orderBy, filter)).enableLink(
	    (newPageNum: Int, newPageSize: Int, newOrderBy: Int) => 
          routes.Workflow.list(newPageNum, newPageSize, newOrderBy, filter).toString
	)))
  }
  
}