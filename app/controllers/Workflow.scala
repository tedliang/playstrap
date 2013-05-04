package controllers

import play.api._
import play.api.mvc._
import activiti.Activiti
import play.api.Play.current
import views._

object Workflow extends Controller {

  val pageSize = 2

  def list(page: Int, orderBy: Int, filter: String = "%") = Action {
    Ok(html.workflow.list(Activiti.processList(page, pageSize, filter, orderBy), views.helper.Pagination(routes.Workflow.list _, filter, orderBy)))
  }
  
}