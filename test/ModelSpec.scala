package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import play.api.db.slick.DB

class ModelSpec extends Specification {
  
  import models._

  "Role model" should {
    
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Roles.findById(1L).get.name must equalTo("Admin")
      }
    }
    
    "has 2 records in total" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Roles.count must equalTo(2)
      }
    }
    
  }
  
  "User model" should {
    
    "have 2 roles in user[1]" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Users.findById(1L).get.roles.length must equalTo(2)
      }
    }
  }
  
}