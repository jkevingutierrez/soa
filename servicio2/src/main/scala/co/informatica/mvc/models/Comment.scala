package co.informatica.mvc.models

import java.text.SimpleDateFormat
import java.util.Date

import com.mongodb.casbah.Imports._

case class Comment(val content: String, val user: User, val id: String = "", var createdDate: String = "") extends Model {
  override def equals(that: Any): Boolean = ???
}

object Comment {
  private val coll = MongoFactory.collection("comment")
  private val dateFormat = new SimpleDateFormat("dd/MM/yyyy")

  def getAll: Iterator[Comment] = {
    val cursorIterator = coll.find()
    cursorIterator.map({ mongoObject => convertDbObjectToModel(mongoObject) })
  }

  def get(id: String): Option[Comment] = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))
    val someComment = coll.findOne(mongoObject)

    someComment match {
      case Some(someComment) => {
        Option(convertDbObjectToModel(someComment))
      }
      case None => {
        None
      }
    }
  }

  def find(model: Model): Option[Comment] = {
    val comment = model.asInstanceOf[Comment]
    val mongoObject = buildDBObject(comment)
    val someComment = coll.findOne(mongoObject)

    someComment match {
      case Some(someComment) => {
        Option(convertDbObjectToModel(someComment))
      }
      case None => {
        None
      }
    }
  }

  def create(model: Model): Comment = {
    val comment = model.asInstanceOf[Comment]
    val date = new Date()
    val createdDate = dateFormat.format(date)
    comment.createdDate = createdDate
    
    val mongoObject = buildDBObject(comment)
    coll.insert(mongoObject)
    convertDbObjectToModel(mongoObject)
  }

  protected def convertDbObjectToModel(obj: MongoDBObject): Comment = {
    val id = obj.getAs[ObjectId]("_id").get.toString()
    val content = obj.getAs[String]("content").get
    val date = obj.getAs[String]("date").get
    val userId = obj.getAs[String]("user.id").get
    val userName = obj.getAs[String]("user.name").get
    val userEmail = obj.getAs[String]("user.email").get

    val user = new User(userId, userName, userEmail)

    val comment = new Comment(content, user, id)
    comment.createdDate = date
    comment
  }

  protected def buildDBObject(comment: Comment): MongoDBObject = {
    MongoDBObject(
      "content" -> comment.content,
      "date" -> comment.createdDate,
      "user" -> MongoDBObject(
        "id" -> comment.user.id,
        "name" -> comment.user.name,
        "email" -> comment.user.email))
  }
}