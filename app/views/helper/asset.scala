package views.helper

import controllers.routes

object asset {

  val dojo = module("dojo", "1.8.1")
  val bootstrap = module("bootstrap", "2.3.1")
  val dojostrap = module("dojo-bootstrap", "1.3")

  def apply(file: String) = routes.Assets.at(file)

  case class module(name: String, version: String) {

    override def toString = s"$name/$version"

    def apply(file: String) = routes.WebJarAssets.at(
      controllers.WebJarAssets.locate(s"$name/$version/$file"))
  }

}