package models


import db.Cassandra
import prosource.core.search.Elastic
import utils.Constant

import com.google.common.base.Function
import com.google.common.collect.ImmutableMap
import com.netflix.astyanax.entitystore.{CompositeEntityManager, DefaultEntityManager, EntityManager, NativeQuery}
import com.netflix.astyanax.model.{ColumnFamily, ColumnList}
import com.netflix.astyanax.MutationBatch
import com.netflix.astyanax.util.RangeBuilder
import java.util.Date
import java.lang.Long
import play.Logger
import scala.reflect.ClassTag
import scala.{None, Predef}
import scala.collection.convert.WrapAsJava._
import scala.collection.convert.WrapAsScala._
import com.sksamuel.elastic4s.{IndexesTypes, ElasticDsl}
import com.sksamuel.elastic4s.ElasticDsl._
import prosource.core.search.Elastic._


import org.apache.cassandra.thrift.NotFoundException
import javax.persistence.PersistenceException

/**
 * Created with IntelliJ IDEA.
 * User: levihoang
 * Date: 09/28/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
class CassandraDAO[T, K](clazz: Class[T], mode: String = "default", options: Predef.Map[String, String] = Predef.Map[String, String]()) {




  val keyspace = Cassandra.keyspace
  var entityManager: EntityManager[T, K] = null
  var log = Logger.of(this.clazz)
  mode match {
    case Constant.defaultEntityManager => {
      entityManager = new DefaultEntityManager.Builder[T, K]().withEntityType(clazz).
        withKeyspace(Cassandra.keyspace).build()
    }
    case Constant.compositeEntityManager => {
      entityManager = new CompositeEntityManager.Builder[T, K]().withKeyspace(Cassandra.keyspace)
        .withEntityType(clazz)
        .build()
    }
    case _ =>
  }


  def findByKeyValue(key : String, value : Any) ={
    var cql = ""
    if(value.isInstanceOf[String]){
      cql = String.format("Select * from %s where %s = '%s' ",
        clazz.getSimpleName, key, value.asInstanceOf[String]
      )
    } else {
      cql = String.format("Select * from %s where %s = '%s' ",
        "Profile", key, value.toString
      )
    }
    val objects  = entityManager.find(cql)
    if (objects.size >= 1) objects.head
    else null
  }

  def isExisted ( key:String , value :Any) ={
    findByKeyValue(key,value) !=null
  }


  /**
   * fetch whole row and construct entity object mapping from columns
   * @param id row key
   * @return entity object. null if not exist
   */
  def get(id: K): T = entityManager.get(id)

  /**
   * delete the whole row by id
   * @param id row key
   */
  def delete(id: K) = {
    entityManager.delete(id)
  }

  /**
   * remove an entire entity
   * id row key
   */
  def remove(entity: T) = {

    entityManager.remove(entity)
  }

  /**
   * @return Return all entities.
   *
   * @throws PersistenceException
   */
  def getAll: List[T] = entityManager.getAll.toList


  /**
   * Delete a set of entities by their id
   * @param ids
   * @throws PersistenceException
   */
  def delete(ids: List[K]) = {
    entityManager.delete(ids)
   /* esClient.sync.execute {
      ElasticDsl.delete id id from url(profileType)
    }*/
  }

  /**
   * Delete a set of entities
   * ids
   * @throws PersistenceException
   */
  def remove(entities: List[T]) = {

    entityManager.remove(entities)
  }

  /**
   * Store a set of entities.
   * entites
   * @throws PersistenceException
   */
  def put(entities: List[T]) = {

    entityManager.put(entities)


  }

  def find(cql: String): List[T] = entityManager.find(cql).toList

  /**
   * Execute a 'native' query using a simple API that adheres to cassandra's native
   * models of rows and columns.
   * @return
   */
  def createNativeQuery: NativeQuery[T, K] = entityManager.createNativeQuery()


  /**
   * Create the underlying storage for this entity.  This should only be called
   * once when first creating store and not part of the normal startup sequence.
   * @throws PersistenceException
   */
  def createStorage(options: java.util.Map[String, Object]) = entityManager.createStorage(options)
  /**
   * Delete the underlying storage for this entity.
   * param options
   * @throws PersistenceException
   */
  def deleteStorage = entityManager.deleteStorage()

  /**
   * Truncate all data in the underlying
   * param options
   * @throws PersistenceException
   */
  def truncate = entityManager.truncate()

  /**
   * Commit the internal batch after multiple operations.  Note that an entity
   * manager implementation may autocommit after each operation.
   * @throws PersistenceException
   */
  def commit = entityManager.commit()



  def put(entity: T): Unit = {
    entityManager.put(entity)
   /* Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id entity.asInstanceOf[BaseEntity].getId fields entity.asInstanceOf[BaseEntity].getData
    }*/
    /*Elastic.esClient.sync.execute {
      ElasticDsl.update(entity.asInstanceOf[BaseEntity].getId) in IndexesTypes(defaultIndex, profileType) doc(entity.asInstanceOf[BaseEntity].getData)
    }*/
  }
}

