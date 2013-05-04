package activiti

import app.Global
import scala.collection.JavaConversions._
import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.ProcessDefinition
import org.activiti.engine.repository.ProcessDefinitionQuery
import models.Page

/**
 * Public Activiti API
 *
 * The underlying process engine is provided by the Activiti Plugin.
 */
object Activiti {

  private def engine = Global.engine

  def processEngine = engine
  def runtimeService = engine.getRuntimeService
  def repositoryService = engine.getRepositoryService
  def taskService = engine.getTaskService
  def managementService = engine.getManagementService
  def identityService = engine.getIdentityService
  def historyService = engine.getHistoryService
  def formService = engine.getFormService
  
  def processList(page: Int, pageSize: Int, filter: String, orderBy: Int) = {
    val proDefquery = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike(s"%$filter%").orderByDeploymentId().desc();
    val deployQuery = repositoryService.createDeploymentQuery
    Page(proDefquery.listPage(pageSize*page, pageSize) map { proDef => 
        (proDef, deployQuery.deploymentId(proDef.getDeploymentId).singleResult())
      }, 
      page, 
      offset = pageSize * page, 
      proDefquery.count)
  } 
  
}