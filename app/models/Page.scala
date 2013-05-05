package models

case class Pageable(pageNum: Int, pageSize: Int,  order: Int) {
  lazy val offset = pageSize*pageNum
}

case class Page[C](pageable: Pageable, content: Seq[C], total: Long) {

  var link = (newPageNum: Int, newPageSize: Int, newOrder: Int) => ""

  def autoLink(newLink: (Int, Int, Int)=>String):Page[C] = {
      link = newLink
      this
    }
      
  def pageLink(newPage: Int) = link(newPage, pageable.pageSize, pageable.order)
  
  def sortLink(newOrderBy: Int) = link(0, pageable.pageSize, 
      if(newOrderBy == scala.math.abs(pageable.order)) -pageable.order 
      else newOrderBy)
  
  def sortClass(newOrderBy: Int, upClass: String = "headerSortUp", downClass: String = "headerSortDown") = 
    if(scala.math.abs(pageable.order) == newOrderBy) 
      if(pageable.order < 0) downClass else upClass
    else ""

  lazy val hasContent = content.nonEmpty
  
  lazy val offset = pageable.offset
  lazy val number = pageable.pageNum
  lazy val size = pageable.pageSize
  
  lazy val from = offset+1
  lazy val to = offset+content.length
  
  lazy val hasPrev = prev >= 0
  lazy val hasNext = total > to
  lazy val prev = number - 1
  lazy val next = number + 1
  lazy val prevLink = pageLink(prev)
  lazy val nextLink = pageLink(next)
  
  lazy val first = 0
  lazy val last = (total/size-(if(total%size==0) 1 else 0)).toInt
  lazy val firstLink = pageLink(first)
  lazy val lastLink = pageLink(last)
  
}
