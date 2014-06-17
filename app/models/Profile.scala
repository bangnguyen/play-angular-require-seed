package models

import java.util.{UUID, Date}

import utils.Constant
import javax.persistence.{Entity, Column, Id}
import scala.annotation.meta.field
import utils.Position
import utils.Position.Position
import com.netflix.astyanax.serializers.AbstractSerializer;
import com.netflix.astyanax.entitystore.Serializer
import java.nio.ByteBuffer
import java.nio.charset.Charset
import utils.Constant._


@Entity
case class Profile(
                    @(Column@field) email: String = "",
                    @(Column@field) phone: String = "",
                    @(Column@field) firstName: String = "",
                    @(Column@field) lastName: String = "",
                    @(Column@field) address: String = "",
                    @(Column@field) @(Serializer@field)(classOf[PositionSerializer]) position: Position = Position.Student,
                    @(Column@field) birthday: Date = new Date()
                    ) extends BaseEntity {
  @(Id@field) @(Column@field)(name = "key") var id: String = UUID.randomUUID().toString
  @(Column@field) var created: Date = new Date()

  def this() = this(null)
  var fullName = firstName + " " + lastName

  override def getData(option : Int = forView): Map[String, Any] = Map(
    "email" -> email,
    "phone" -> phone.toString,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "address" -> address,
    "position" -> position.toString,
    "birthday" -> (if (option == forView)  birthday.getTime else created) ,
    "created" -> (if (option == forView)  created.getTime else created) ,
    "id" -> id,
    "fullName" -> fullName
  )

  override def getId: String = id
}

class PositionSerializer extends AbstractSerializer[Position] {

  import PositionSerializer._

  override def fromByteBuffer(byteBuffer: ByteBuffer): Position = {
    if (byteBuffer == null) {
      return null;
    }
    return Position.withName(charset.decode(byteBuffer).toString());

  }


  override def toByteBuffer(obj: Position): ByteBuffer = {
    if (obj == null) {
      return null;
    }
    return ByteBuffer.wrap(obj.toString().getBytes(charset));
  }
}


object PositionSerializer {

  private val UTF_8 = "UTF-8"

  private val charset = Charset.forName(UTF_8)

  private val instance = new PositionSerializer()

  def get(): PositionSerializer = instance
}

//object PositionSerializer

object Profiles extends CassandraDAO[Profile, String](classOf[Profile], Constant.defaultEntityManager)   {


  /*def findByKeyValue(key : String, value : Any) ={
    var cql = ""
    if(value.isInstanceOf[String]){
       cql = String.format("Select * from %s where %s = '%s' ",
        "Profile", key, value.asInstanceOf[String]
      )
    } else {
      cql = String.format("Select * from %s where %s = '%s' ",
        "Profile", key, value.toString
      )
    }
    val objects: List[Profile] = find(cql)
    if (objects.size >= 1) objects.head
    else null
  }*/






}


/*  Elastic.esClient.sync.execute {
    ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData
  }*/


/* Elastic.esClient.sync.execute {
   ElasticDsl.update(profile.id) in IndexesTypes(defaultIndex, profileType) doc(profile.getData)
 }*/


/*esClient.sync.execute {
  ElasticDsl.delete id id from url(profileType)
}*/






