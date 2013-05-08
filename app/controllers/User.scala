package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import views.html._
import models.{Users, Pageable, Searchable}

object User extends Controller {

  def list(filter: String, p: Pageable) = Action {
	Ok(user.list(Users.list(new Searchable(filter, p)).withLink(
	    routes.User.list(filter, _).url
	)))
  }
  
}