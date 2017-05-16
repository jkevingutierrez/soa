package co.informatica.mvc.models

import com.mongodb.casbah.Imports._

case class User(val id: String = "", val name: String, val email: String) extends Model {
  override def equals(that: Any): Boolean = ???
}

object User {

  private val coll = MongoFactory.collection("user")

  def getAll: Iterator[User] = {
    val cursorIterator = coll.find()
    cursorIterator.map({ mongoObject => convertDbObjectToModel(mongoObject) })
  }

  def get(id: String): Option[User] = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))
    val someUser = coll.findOne(mongoObject)

    someUser match {
      case Some(someUser) => {
        Option(convertDbObjectToModel(someUser))
      }
      case None => {
        None
      }
    }
  }

  def find(model: Model): Option[User] = {
    val user = model.asInstanceOf[User]
    val mongoObject = buildDBObject(user)
    val someUser = coll.findOne(mongoObject)

    someUser match {
      case Some(someUser) => {
        Option(convertDbObjectToModel(someUser))
      }
      case None => {
        None
      }
    }
  }

  def create(model: Model): User = {
    val user = model.asInstanceOf[User]
    var mongoObject = MongoDBObject("email" -> user.email)
    val someUser = coll.findOne(mongoObject)

    someUser match {
      case Some(someUser) => {
        println("El usuario ya existe")
        user
      }
      case None => {
        mongoObject = buildDBObject(user)
        coll.insert(mongoObject)
        convertDbObjectToModel(mongoObject)
      }
    }
  }

  protected def buildDBObject(user: User): MongoDBObject = {
    MongoDBObject(
      "name" -> user.name,
      "email" -> user.email)
  }

  protected def convertDbObjectToModel(obj: MongoDBObject): User = {
    val id = obj.getAs[ObjectId]("_id").get.toString()
    val name = obj.getAs[String]("name").get
    val email = obj.getAs[String]("email").get

    new User(id, name, email)
  }

  def delete(id: String): WriteResult = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))
    coll.remove(mongoObject)
  }

  def update(id: String): User = {
    new User("", "", "")
  }

}
