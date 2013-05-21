package app

import play.api._
import play.api.Play.current
import play.api.db.DB
import org.activiti.engine.ProcessEngine
import org.activiti.engine.ProcessEngineConfiguration
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.activiti.engine.impl.history.HistoryLevel
import activiti.exceptionhandling.RecoverableJobCommandFactory
import activiti.SlickTransactionFactory
import scala.Array.canBuildFrom
import models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Starting app")
    initialData()
    engine
    loadNewProcessDefinitions(app)
  }
  
  override def onStop(app: Application) {
    Logger.info("Stopping app")
    engine.close()
  }

  lazy val engine: ProcessEngine = ProcessEngineConfiguration.
    createStandaloneProcessEngineConfiguration.asInstanceOf[ProcessEngineConfigurationImpl].
    setFailedJobCommandFactory(new RecoverableJobCommandFactory()).
    setProcessEngineName("Activiti Process Engine").
    setDataSource(DB.getDataSource()).
    setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE).
    setJobExecutorActivate(true).
    setTransactionFactory(new SlickTransactionFactory()).
    setHistory(HistoryLevel.ACTIVITY.getKey).
    buildProcessEngine()

  private def loadNewProcessDefinitions(app: Application) {
    app.configuration.getString("activiti.resourcePath") map { folder => 
      app.getFile(s"conf/$folder").list map { resourceName =>
        Logger.info(s"Loading process definition $folder/$resourceName")
        engine.getRepositoryService
          .createDeployment()
          // Name must be different for each resource, otherwise 'enableDuplicateFiltering' breaks.
          .name(resourceName.takeWhile(_ != '.'))
          .addClasspathResource(s"$folder/$resourceName")
          .enableDuplicateFiltering() // Don't deploy if it's already deployed
          .deploy()
      }
    }
  }

  private def initialData() {

    if (Roles.count == 0) {
      Roles.insertAll(
        Role(None, "Admin", Option("admin")),
        Role(None, "User", Option("user")))
    }

    if (Users.count == 0) {
      Users.insertAll(
        User(None, "Acme", "123", "a@a.com"),
        User(None, "Superior", "321", "b@b.com"),
        User(None, "Ground", "111", "c@c.com"))
    }

    if (UserRole.count == 0) {
      UserRole.insert(1, 1);
      UserRole.insert(1, 2);
      UserRole.insert(2, 2);
      UserRole.insert(3, 2);
    }

  }
  
}