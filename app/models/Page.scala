package models

import play.api.mvc.QueryStringBindable

class Searchable(val filter: String, pageable: Pageable) extends Pageable(pageable)

class Pageable(val index: Int, val size: Int, val sort: Int) {
  def this(p: Pageable) = this(p.index, p.size, p.sort)
  def offset = index * size
  def unapply = (index, size, sort)
}

object Pageable {
  def apply(index: Int, size: Int, sort: Int) = 
    new Pageable(index, size, sort)

  implicit def queryStringBinder(implicit intBinder: QueryStringBindable[Int]) = 
    new PageableBinder(intBinder)
  
  object key extends Enumeration {
    val index = Value("i")
    val size = Value("s")
    val sort = Value("o")
  } 
}

class PageableBinder(intBinder: QueryStringBindable[Int]) extends QueryStringBindable[Pageable] {
  override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Pageable]] = {
    for {
      index <- intBinder.bind(s"$key.${Pageable.key.index}", params)
      size <- intBinder.bind(s"$key.${Pageable.key.size}", params)
      sort <- intBinder.bind(s"$key.${Pageable.key.sort}", params)
    } yield (index, size, sort) match {
      case (Right(index), Right(size), Right(sort)) => Right(Pageable(index, size, sort))
      case _ => Left("Unable to bind a Pageable")
    }
  }
  override def unbind(key: String, p: Pageable): String = {
    intBinder.unbind(s"$key.${Pageable.key.index}", p.index) + "&" + 
    intBinder.unbind(s"$key.${Pageable.key.size}", p.size)+ "&" + 
    intBinder.unbind(s"$key.${Pageable.key.sort}", p.sort)
  }
}

class Page[P <:Pageable, C](val criteria: P, val content: Seq[C], val total: Long) extends Pagination {

  def index = criteria.index
  def size = criteria.size
  def offset = criteria.offset
  
  def from = offset + 1
  def to = offset + content.length
  def hasContent = content.nonEmpty
  
  var _link = (pageable: Pageable) => ""

  def withLink(newLink: (Pageable) => String) = {
      _link = newLink
      this
    }

  def link(index: Int = 0, size: Int = criteria.size, sort: Int = criteria.sort): String = 
    _link(Pageable(index, size, sort))
  
  def pageLink(newPage: Int): String = link(index = newPage)
  def sizeLink(newSize: Int): String = link(size = newSize)
  def sortLink(newSort: Int): String = link(sort = 
      if(newSort == scala.math.abs(criteria.sort)) -criteria.sort 
      else newSort)
  
  def sortClass(newSort: Int, 
      upClass: String = "headerSortUp", 
      downClass: String = "headerSortDown"): String = 
    if(scala.math.abs(criteria.sort) == newSort) 
      if(criteria.sort < 0) downClass else upClass
    else ""

  def input(key: String, index: Int = 0, size: Int = criteria.size, sort: Int = criteria.sort) = {
    <input type="hidden" name={s"$key.${Pageable.key.index}"} value={s"$index"}/>
    <input type="hidden" name={s"$key.${Pageable.key.size}"} value={s"$size"}/>
    <input type="hidden" name={s"$key.${Pageable.key.sort}"} value={s"$sort"}/>
  }

}

trait Pagination {
  
  def index: Int
  def size: Int
  def offset: Int
	
  def total: Long
  def from: Int
  def to: Int
  
  def pageLink(newPage: Int): String
  def sizeLink(newSize: Int): String
  def sortLink(newSort: Int): String

  def first = 0
  def last = (total/size-(if(total%size==0) 1 else 0)).toInt
  def firstLink = pageLink(first)
  def lastLink = pageLink(last)
  
  def hasPrev = prev >= first
  def hasNext = total > to
  def prev = index - 1
  def next = index + 1
  def prevLink = pageLink(prev)
  def nextLink = pageLink(next)
  
  def bound(range: Int = 5) = {
    val low = index - range
    val high = index + range
    val lenght = range * 2
    
    if(lenght > last) first to last
    else if(first > low) first to lenght
    else if(high > last) last-lenght to last
    else low to high
  }
  
}
