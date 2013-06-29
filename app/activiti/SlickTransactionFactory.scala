package activiti

import java.sql.Connection
import java.util.Properties
import javax.sql.DataSource
import org.apache.ibatis.session.TransactionIsolationLevel
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransaction
import org.apache.ibatis.transaction.managed.ManagedTransaction
import play.api.Play.current
import play.api.db.slick.DB

class SlickTransactionFactory extends TransactionFactory {

  override def setProperties(props: Properties) {}

  override def newTransaction(connection: Connection) = {
    DB.withSession { session =>
      if (session.conn == connection)
        new ManagedTransaction(connection, false)
      else
        new JdbcTransaction(connection)
    }
  }

  override def newTransaction(dataSource: DataSource, level: TransactionIsolationLevel, autoCommit: Boolean) = {
    new JdbcTransaction(dataSource, level, autoCommit)
  }

}