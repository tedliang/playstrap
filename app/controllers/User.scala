package controllers

import play.api._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import views.html._
import models.{Users, Pageable, Searchable}
import play.api.data.Form


object User extends Controller {

  val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> nonEmptyText)(models.User.apply)(models.User.unapply))
  
  def list(filter: String, p: Pageable) = Action {
	Ok(user.list(Users.list(new Searchable(filter, p)).withLink(
	    routes.User.list(filter, _).url
	)))
  }
  
  def view(id: Long) = Action { implicit request =>
    Users.findById(id).map { model => 
      Ok(user.view(model))
    } getOrElse NotFound
  }

  def edit(id: Long) = Action { implicit request =>
    Users.findById(id).map { model => 
      Ok(user.edit(id, form.fill(model)))
    } getOrElse NotFound
  }

  def update(id: Long) = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(user.edit(id, formWithErrors)),
      entity => Users.update(entity) match {
          case 0 => Redirect(routes.User.edit(id)).flashing(
              "failure" -> s"Could not update user ${entity.username}.")
          case _ => Redirect(routes.User.view(id)).flashing(
              "success" -> s"User ${entity.username} has been updated.")
      }
    )
  }
}