package controllers

import play.api._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import views.html.user
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

  def create = Action { implicit request =>
    Ok(user.create(form))
  }
  
  def save = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(user.create(formWithErrors)),
      model => 
        Redirect(routes.User.view(Users.insert(model))).flashing(
        "success" -> s"User ${model.username} has been created.")
    )
  }
  
  def edit(id: Long) = Action { implicit request =>
    Users.findById(id).map { model => 
      Ok(user.edit(id, form.fill(model)))
    } getOrElse NotFound
  }

  def update(id: Long) = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(user.edit(id, formWithErrors)),
      model => Users.update(model) match {
          case 0 => Redirect(routes.User.edit(id)).flashing(
              "failure" -> s"Could not update user ${model.username}.")
          case _ => Redirect(routes.User.view(id)).flashing(
              "success" -> s"User ${model.username} has been updated.")
      }
    )
  }
}