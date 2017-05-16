package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse, HttpSession }

import co.informatica.mvc.models.Post.convertDbObjectToModel
import co.informatica.mvc.models.{ Post, User, MailHelper }
import play.api.libs.json._

class PostsController extends BaseController {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    implicit val userFormat = Json.format[User]
    implicit val postFormat = Json.format[Post]

    if (req.getPathInfo == null) {
      val posts = Post.getAll
      val jsonObj = Json.obj("elements" -> posts.toList)
      resp.getWriter.print(jsonObj)
    } else {
      val userId = req.getPathInfo.substring(req.getPathInfo.lastIndexOf("/") + 1)
      val posts = Post.getByUserId(userId)
      val jsonObj = Json.obj("elements" -> posts.toList)
      resp.getWriter.print(jsonObj)
    }

    // if (req.getPathInfo contains "delete") {
    //   val oid = req.getPathInfo.substring(req.getPathInfo.lastIndexOf("/") + 1)
    //   Post.delete(oid)
    //
    //   resp.sendRedirect("/posts")
    // }

    resp.getWriter.close()
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doPost(req, resp)

    implicit val userFormat = Json.format[User]
    implicit val postFormat = Json.format[Post]

    val jb = new StringBuffer
    var line: String = null
    val reader = req.getReader
    while ({ line = reader.readLine; line != null }) {
      jb.append(line);
    }

    val json: JsValue = Json.parse(jb.toString)

    val id = ""
    val title = json \ "title"
    val subtitle = json \ "subtitle"
    val content = json \ "content"

    val userId = json \ "id"
    val userName = json \ "name"
    val userEmail = json \ "email"

    val user = new User(userId.as[String], userName.as[String], userEmail.as[String])
    val post = new Post(id, title.as[String], subtitle.as[String], content.as[String], user)

    val createdPost = Post.create(post)

    MailHelper.send(user.email, "El post ha sido creado", "<h1>El post " + createdPost.id + " ha sido creado exitosamente.</h1>")

    val jsonObj = Json.obj("element" -> createdPost)
    resp.getWriter.print(jsonObj)
    resp.getWriter.close()
  }

  override def doDelete(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doDelete(req, resp)

    val oid = req.getPathInfo.substring(req.getPathInfo.lastIndexOf("/") + 1)
    Post.delete(oid)

    val jsonObj = Json.obj("msg" -> "Se ha eliminado el post exitosamente")
    resp.getWriter.print(jsonObj)
  }
}
