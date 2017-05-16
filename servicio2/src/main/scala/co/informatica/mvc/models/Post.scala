package co.informatica.mvc.models

import java.text.SimpleDateFormat
import java.util.Date

import com.mongodb.casbah.Imports._

case class Post(val id: String = "", val title: String, val subtitle: String = "", val content: String, val user: User, var createdDate: String = "") extends Model {
  var comments: Seq[Comment] = Seq[Comment]()

  override def equals(that: Any): Boolean = ???
}

object Post {
  private val coll = MongoFactory.collection("post")
  private val dateFormat = new SimpleDateFormat("dd/MM/yyyy")

  def getByUserId(id: String): Iterator[Post] = {
    val q = MongoDBObject("user.id" -> id)

    val cursorIterator = coll.find(q)
    cursorIterator.map({ mongoObject => convertDbObjectToModel(mongoObject) })
  }

  def getAll: Iterator[Post] = {
    val cursorIterator = coll.find()
    cursorIterator.map({ mongoObject => convertDbObjectToModel(mongoObject) })
  }

  def get(id: String): Option[Post] = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))
    val somePost = coll.findOne(mongoObject)

    somePost match {
      case Some(somePost) => {
        Option(convertDbObjectToModel(somePost))
      }
      case None => {
        None
      }
    }
  }

  def find(model: Model): Option[Model] = {
    val post = model.asInstanceOf[Post]
    val mongoObject = buildDBObject(post)
    val somePost = coll.findOne(mongoObject)

    somePost match {
      case Some(somePost) => {
        Option(convertDbObjectToModel(somePost))
      }
      case None => {
        None
      }
    }
  }

  protected def buildDBObject(post: Post): MongoDBObject = {
    MongoDBObject(
      "title" -> post.title,
      "subtitle" -> post.subtitle,
      "content" -> post.content,
      "date" -> post.createdDate,
      "comments" -> MongoDBList(),
      "user" -> MongoDBObject(
        "id" -> post.user.id,
        "name" -> post.user.name,
        "email" -> post.user.email))
  }

  protected def convertDbObjectToModel(obj: MongoDBObject): Post = {
    val id = obj.getAs[ObjectId]("_id").get.toString()
    val title = obj.getAs[String]("title").get
    val subtitle = obj.getAs[String]("subtitle").get
    val content = obj.getAs[String]("content").get
    val date = obj.getAs[String]("date").get
    val comments = obj.getAs[MongoDBList]("comments")
    val userId = obj.getAs[String]("user.id").get
    val userName = obj.getAs[String]("user.name").get
    val userEmail = obj.getAs[String]("user.email").get

    val user = new User(userId, userName, userEmail)

    val post = new Post(id, title, subtitle, content, user)
    post.createdDate = date

    comments match {
      case Some(comments) => {
        post.comments = transformCommentsList(comments)
        print("Comments")
        print(post.comments)
      }
      case None => {
        None
      }
    }

    post
  }

  protected def transformCommentsList(list: MongoDBList) = {
    println("Transform:")
    println(list.getClass)
    list.map({ element =>
      {
        val mongoObject = element.asInstanceOf[BasicDBObject]
        print(mongoObject.getClass)
        val id = mongoObject.getAs[String]("id").get
        val content = mongoObject.getAs[String]("content").get
        val date = mongoObject.getAs[String]("date").get
        val userId = mongoObject.getAs[String]("user.id").get
        val userName = mongoObject.getAs[String]("user.name").get
        val userEmail = mongoObject.getAs[String]("user.email").get

        val user = new User(userId, userName, userEmail)

        val comment = new Comment(content, user, id)
        comment.createdDate = date
        comment
      }
    })
  }

  def create(model: Model): Post = {
    val post = model.asInstanceOf[Post]
    var mongoObject = MongoDBObject("title" -> post.title)
    val somePost = coll.findOne(mongoObject)
    val date = new Date()
    val createdDate = dateFormat.format(date)

    somePost match {
      case Some(somePost) => {
        println("El Post ya existe")
        post
      }
      case None => {
        post.createdDate = createdDate
        mongoObject = buildDBObject(post)
        coll.insert(mongoObject)
        convertDbObjectToModel(mongoObject)
      }
    }
  }

  def delete(id: String): WriteResult = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))
    coll.remove(mongoObject)
  }

  def addComment(id: String, comment: Comment): WriteResult = {
    val mongoObject = MongoDBObject("_id" -> new ObjectId(id))

    coll.update(mongoObject, $push("comments" -> MongoDBObject(
      "id" -> comment.id,
      "content" -> comment.content,
      "date" -> comment.createdDate,
      "user" -> MongoDBObject(
        "id" -> comment.user.id,
        "name" -> comment.user.name,
        "email" -> comment.user.email))))
  }

}