package views.helper

import controllers.routes

object asset {

  val dojo = WebJarAsset("dojo", "1.8.3")
  val bootstrap = WebJarAsset("bootstrap", "2.3.1")
  val dojostrap = WebJarAsset("dojo-bootstrap", "1.3")

  def apply(file: String) = routes.Assets.at(file)

  case class WebJarAsset(name: String, version: String) {

    override def toString = s"$name/$version"

    def apply(file: String) = routes.WebJarAssets.at(
      controllers.WebJarAssets.locate(s"$name/$version/$file"))
  }

}