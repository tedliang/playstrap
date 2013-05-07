package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import activiti.Activiti
import views.html._
import models.{Searchable,Pageable}

object Workflow extends Controller {

  def list(filter: String, p: Pageable) = Action {
	Ok(workflow.list(Activiti.processList(new Searchable(filter, p)).withLink(
	    routes.Workflow.list(filter, _).toString
	)))
  }
  
}