package models

class Pageable(val pageNum: Int, val pageSize: Int, val order: Int) {
  def offset = pageSize*pageNum
}

class Searchable(val filter: String, pageNum: Int, pageSize: Int, order: Int) 
	extends Pageable(pageNum, pageSize, order) 

class Page[P <:Pageable, C](val criteria: P, val content: Seq[C], val total: Long) {

  var link = (newPageNum: Int, newPageSize: Int, newOrder: Int) => ""

  def withLink(newLink: (Int, Int, Int) => String) = {
      link = newLink
      this
    }
      
  def pageLink(newPage: Int) = link(newPage, criteria.pageSize, criteria.order)
  def sizeLink(newPageSize: Int) = link(0, newPageSize, criteria.order)
  
  def sortLink(newOrderBy: Int) = link(0, criteria.pageSize, 
      if(newOrderBy == scala.math.abs(criteria.order)) -criteria.order 
      else newOrderBy)
  
  def sortClass(newOrderBy: Int, upClass: String = "headerSortUp", downClass: String = "headerSortDown") = 
    if(scala.math.abs(criteria.order) == newOrderBy) 
      if(criteria.order < 0) downClass else upClass
    else ""

  def hasContent = content.nonEmpty
  
  def offset = criteria.offset
  def number = criteria.pageNum
  def size = criteria.pageSize
  
  def from = offset+1
  def to = offset+content.length
  
  def hasPrev = prev >= 0
  def hasNext = total > to
  def prev = number - 1
  def next = number + 1
  def prevLink = pageLink(prev)
  def nextLink = pageLink(next)
  
  def first = 0
  def last = (total/size-(if(total%size==0) 1 else 0)).toInt
  def firstLink = pageLink(first)
  def lastLink = pageLink(last)
  
  def bound(range: Int = 5) = {
    val low = number - range
    val high = number + range
    val lenght = range * 2
    
    if(lenght > last) first to last
    else if(first > low) first to lenght
    else if(high > last) last-lenght to last
    else low to high
  }
  
}
