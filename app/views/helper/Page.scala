package views.helper

abstract class Page[C](number: Int, pageSize: Long, _filter: String, _orderBy: Int, _content: Seq[C], _total: Long) {

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

  lazy val hasContent = _content.nonEmpty
  lazy val content = _content
  
  lazy val total = _total
  lazy val offset = pageSize*number
  
  lazy val from = offset+1
  lazy val to = offset+_content.length
  
  lazy val hasPrev = prev >= 0
  lazy val hasNext = total > to
  lazy val prev = number - 1
  lazy val next = number + 1
  lazy val prevLink = pageLink(prev)
  lazy val nextLink = pageLink(next)
  
  lazy val first = 0
  lazy val last = (_total/pageSize-1).toInt
  lazy val firstLink = pageLink(first)
  lazy val lastLink = pageLink(last)
  
}
