package activiti

import app.Global
import scala.collection.JavaConversions._
import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.ProcessDefinition
import org.activiti.engine.repository.ProcessDefinitionQuery
import models.{Pageable, Page}

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
  
  def processList(pageable: Pageable, filter: String) = {
    val proDefquery = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike(s"%$filter%");

    scala.math.abs(pageable.order) match {
      case 2 => proDefquery.orderByProcessDefinitionName()
      case 3 => proDefquery.orderByProcessDefinitionKey()
      case 4 => proDefquery.orderByDeploymentId()
      case _ => proDefquery.orderByProcessDefinitionId()
    }
    if(pageable.order<0) proDefquery.desc() else proDefquery.asc()
    
    val deployQuery = repositoryService.createDeploymentQuery
    Page(pageable, for(proDef <- proDefquery.listPage(pageable.offset, pageable.pageSize)) 
      yield(proDef, deployQuery.deploymentId(proDef.getDeploymentId).singleResult()), 
      proDefquery.count)
  } 
  
}