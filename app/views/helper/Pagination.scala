package views.helper

abstract class Pagination(page: Int, pageSize: Long, _filter: String, _orderBy: Int, rows: Int, _total: Long) {

  def link(newPage: Int, newOrderBy: Int): play.api.mvc.Call

  def pageLink(newPage: Int) = link(newPage, _orderBy)
  
  def sortLink(newOrderBy: Int) = link(0, 
      if(newOrderBy == scala.math.abs(orderBy)) -_orderBy 
      else newOrderBy)
  
  def sortClass(newOrderBy: Int, upClass: String = "headerSortUp", downClass: String = "headerSortDown") = 
    if(scala.math.abs(_orderBy) == newOrderBy) 
      if(_orderBy < 0) downClass else upClass
    else ""
  
  lazy val filter = _filter
  lazy val orderBy = _orderBy
  
  lazy val total = _total
  lazy val offset = pageSize*page
  
  lazy val from = offset+1
  lazy val to = offset+rows
  
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + rows) < _total)

}
