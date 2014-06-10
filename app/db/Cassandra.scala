package db

import com.google.common.collect.ImmutableMap
import com.netflix.astyanax
import com.netflix.astyanax.{AstyanaxContext, Keyspace}
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.connectionpool.impl.{ConnectionPoolConfigurationImpl, CountingConnectionPoolMonitor}
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.serializers._
import com.netflix.astyanax.thrift.ThriftFamilyFactory
import com.typesafe.config.ConfigFactory
import db.ColumnFamilyParams._
import prosource.core.search.Elastic
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._


import org.apache.cassandra.thrift.InvalidRequestException
import utils.Constant
import models._
import prosource.core.search.Elastic._

object Cassandra {
 // SingletonEmbeddedCassandra.getInstance
  Thread.sleep(1000 * 1)

  var keyspace: astyanax.Keyspace = null
  var context: AstyanaxContext[astyanax.Keyspace] = null
  val TEST_CLUSTER_NAME: String = "junit_cass_sandbox"
  try {
    context = new AstyanaxContext.Builder().forKeyspace(getString(Constant.CASSANDRA_KEYSPACE))
    //  .forCluster(TEST_CLUSTER_NAME)
      .withAstyanaxConfiguration(
        new AstyanaxConfigurationImpl()
          .setCqlVersion("3.0.0")
          .setTargetCassandraVersion("1.2")
          .setDiscoveryType(NodeDiscoveryType.NONE))
      .withConnectionPoolConfiguration(
        new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(getInt(Constant.CASSANDRA_PORT))
          .setMaxConnsPerHost(getInt(Constant.CASSANDRA_MAXCONNSPERHOST))
          .setSeeds(getString(Constant.CASSANDRA_HOST).concat(":").concat(getString(Constant.CASSANDRA_PORT)))
          .setSocketTimeout(30000)
          .setMaxConnsPerHost(100)
          .setInitConnsPerHost(10)
      )
      .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
      .buildKeyspace(ThriftFamilyFactory.getInstance())
    context.start();
    keyspace = context.getClient
  }
  catch {
    case exception: Exception => {
      println("problem with keyspace " + Constant.CASSANDRA_KEYSPACE)
    }
  }


  def isExistKeySpace: Boolean = {
    var existKeySpace = false
    try {
      keyspace.describeKeyspace()
      existKeySpace = true
    }
    catch {
      case exception: com.netflix.astyanax.connectionpool.exceptions.BadRequestException => {
        if (exception.getCause.isInstanceOf[org.apache.cassandra.thrift.InvalidRequestException]) {
          val ex: InvalidRequestException = exception.getCause.asInstanceOf[org.apache.cassandra.thrift.InvalidRequestException]
          if (ex.getWhy.toLowerCase().contains("keyspace") && ex.getWhy.toLowerCase().contains("not exist"))
            existKeySpace = false
          else throw exception
        }
        else throw exception
      }
      case ex => throw ex
    }
    existKeySpace
  }

  def createKeyspace: Keyspace = {
    var ksOptions: ImmutableMap[String, Object] = ImmutableMap.builder[String, Object]()
      .put("strategy_options", ImmutableMap.builder[String, Object]().put("replication_factor", "1").build()).
      put("strategy_class", "SimpleStrategy").build();
    keyspace.createKeyspace(ksOptions)
    keyspace
  }

  def dropKeySpace = {
    try {
      keyspace.dropKeyspace
    }
    catch {
      case e: Exception => {
        //   e.printStackTrace
      }
    }
  }

  def getString(key: String): String = {
    ConfigFactory.load().getString(key)
  }

  def getInt(key: String): Int = {
    ConfigFactory.load().getString(key).toInt
  }

  def createColumnFamily[K](cfName: String): ColumnFamily[String, String] = {
    ColumnFamily.newColumnFamily(cfName,
      StringSerializer.get,
      StringSerializer.get)
  }


  def firstTime = {
    dropKeySpace
    createKeyspace
    Users.createStorage(null)
    Profiles.createStorage(profileOptions)
    //Profiles.createStorage(profileOptions)
    Courses.createStorage(courseOptions)
    CourseStudents.createStorage(null)
    Elastic.esClient.execute { deleteIndex(Elastic. defaultIndex) }
    Elastic.createIndex

  }

  def buildRowKey(className: String, key: String) = StringBuilder.newBuilder.append(className).append("_").append(key).toString().toLowerCase()
}

