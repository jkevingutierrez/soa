package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse, HttpSession }

import co.informatica.mvc.models.{ Comment, Post, User, MailHelper }
import play.api.libs.json._

class CommentsController extends BaseController {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    implicit val userFormat = Json.format[User]
    implicit val postFormat = Json.format[Post]
    implicit val commentFormat = Json.format[Comment]

    if (req.getPathInfo == null) {
      val comments = Comment.getAll
      val jsonObj = Json.obj("elements" -> comments.toList)
      resp.getWriter.print(jsonObj)
    } else {

    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doPost(req, resp)

    implicit val userFormat = Json.format[User]
    implicit val postFormat = Json.format[Post]
    implicit val commentFormat = Json.format[Comment]

    val jb = new StringBuffer
    var line: String = null
    val reader = req.getReader
    while ({ line = reader.readLine; line != null }) {
      jb.append(line);
    }

    val json: JsValue = Json.parse(jb.toString)

    val id = ""
    val content = json \ "content"

    val userId = json \ "userid"
    val userName = json \ "name"
    val userEmail = json \ "email"

    val postId = json \ "postid"

    val post = Post.get(postId.as[String])

    post match {
      case Some(post) => {
        println("Post")
        println(post.id)
        println(post.title)

        val user = new User(userId.as[String], userName.as[String], userEmail.as[String])
        val comment = new Comment(content.as[String], user)
        val createdComment = Comment.create(comment)

        println("Comment: " + comment)
        Post.addComment(post.id, comment)

        MailHelper.send(user.email, "Ha recibido un comentario", "<h1>Se ha agregado un nuevo comentario al post.</h1>")

        val jsonObj = Json.obj("element" -> createdComment)
        resp.getWriter.print(jsonObj)
      }
      case None => {
        val jsonObj = Json.obj("msg" -> "No fue posible encontrar al usuario")
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND)
        resp.getWriter().print(jsonObj)
      }
    }

    resp.getWriter.close()

  }
}
