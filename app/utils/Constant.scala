package utils


object Constant {

  val AOX_STORE_CASSANDRA: String = "cassandra"
  val AOX_STORE_INMEMORY: String = "inmemory"
  val CASSANDRA_HOST: String = "cassandra.host"
  val CASSANDRA_PORT: String = "cassandra.port"
  val CASSANDRA_MAXCONNSPERHOST: String = "cassandra.maxConnectionsPerHost"
  val CASSANDRA_KEYSPACE: String = "cassandra.keyspace"
  val CASSANDRA_COLUMNFAMILY: String = "cassandra.columnfamily"
  val CASSANDRA_CQL_VERSION: String = "cassandra.cql.version"
  val defaultEntityManager = "default"
  val compositeEntityManager = "composite"
  val forEs = 2
  val forView = 1

}


object FuncResult {
  val CALL_SUCCESS = 0
  val CALL_FAIL = -1
  val CALL_EXISTED_FAIL = -2
  val CALL_NOT_EXISTED_FAIL = -3
  val CALL_OTHER = -4

}

object Position extends Enumeration {
  type Position = Value
  val Teacher, Student, Management, Other = Value
}

object Level extends Enumeration {
  type Level = Value
  val Beginner, Elem, PostElem, PreInter , Inter , PostInter , Advance= Value
}

object Room extends Enumeration {
  type Room = Value
  val Room1, Room2, Room3, Room4 = Value
}