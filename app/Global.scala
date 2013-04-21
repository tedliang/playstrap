import play.api._

import models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }
  
  object InitialData {
    def insert() {
      
      if(Roles.count==0){
	    Roles.insertAll(
	      Role(Some(1L), "Admin", Option("admin")),
	      Role(Some(2L), "User", Option("user"))
	    )
      }

      if(Users.count==0){
	    Users.insertAll(
	      User(Some(1L), "Acme", "123", "a@a.com"),
	      User(Some(2L), "Superior", "321", "b@b.com"),
	      User(Some(3L), "Ground", "111", "c@c.com")
	    )
      }
      
      if(UserRole.count==0){
	      UserRole.insert(1, 1);
	      UserRole.insert(1, 2);
	      UserRole.insert(2, 2);
	      UserRole.insert(3, 2);
      }

    }
  }
  
}