package views.helper

import controllers.routes
import play.api.mvc.Call
import scala.xml.NodeSeq

case class Pagination(t: (Int,Int,String) => Call, filter: String, currentOrderBy: Int) {

  def link(p: Int, newOrderBy: Option[Int] = None) = t(p,newOrderBy.map { orderBy =>
        if(orderBy == scala.math.abs(currentOrderBy)) -currentOrderBy else orderBy
    }.getOrElse(currentOrderBy), filter)
    
  def header(orderBy: Int, title: String) = {
    <th class={s"col$orderBy header ${sortClass(orderBy)}"}>
    <a href={link(0, Some(orderBy)).toString}>{title}</a>
	</th>
  }
  
  def sortClass(orderBy: Int) = 
    if(scala.math.abs(currentOrderBy) == orderBy) 
      if(currentOrderBy < 0) "headerSortDown" 
      else "headerSortUp" 
    else ""
  
}
