package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse, HttpSession }

import co.informatica.mvc.models.Post.convertDbObjectToModel
import co.informatica.mvc.models.{ Post, User, Comment }
import play.api.libs.json._

class PostController extends BaseController {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    implicit val userFormat = Json.format[User]
    implicit val postFormat = Json.format[Post]
    implicit val commentFormat = Json.format[Comment]

    val session: HttpSession = req.getSession(false)
    var name = ""

    if (session != null) {
      name = session.getAttribute("name").toString
    }

    val oid = req.getPathInfo.substring(req.getPathInfo.lastIndexOf("/") + 1)
    val post = Post.get(oid)

    post match {
      case Some(post) => {
        val jsonObj = Json.obj("element" -> Json.obj(
            "id" -> post.id,
            "title" -> post.title,
            "subtitle" -> post.subtitle,
            "content" -> post.content,
            "user" -> post.user,
            "createdDate" -> post.createdDate,
            "comments" -> post.comments))
        resp.getWriter.print(jsonObj)
      }
      case None => {
        val jsonObj = Json.obj("msg" -> ("No fue posible encontrar el post: " + oid))
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND)
        resp.getWriter().print(jsonObj)
      }
    }

    resp.getWriter.close()

  }
}
