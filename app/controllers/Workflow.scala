package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import models.Searchable

object Workflow extends Controller {

  def list(filter: String, pageNum: Int, pageSize: Int, orderBy: Int) = Action {
	Ok(workflow.list(Activiti.processList(new Searchable(filter, pageNum, pageSize, orderBy)).withLink(
	    (newPageNum: Int, newPageSize: Int, newOrderBy: Int) => 
          routes.Workflow.list(filter, newPageNum, newPageSize, newOrderBy).toString
	)))
  }
  
}