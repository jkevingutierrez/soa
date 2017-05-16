package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import play.api.libs.json._

import co.informatica.mvc.models.User
import play.api.libs.json.Json

class UsersController extends BaseController {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    implicit val userFormat = Json.format[User]

    if (req.getPathInfo == null) {
      val users = User.getAll
      val jsonObj = Json.obj("elements" -> users.toList)
      resp.getWriter.print(jsonObj)
    } else {

    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doPost(req, resp)

    implicit val userFormat = Json.format[User]

    val jb = new StringBuffer
    var line: String = null
    val reader = req.getReader
    while ({ line = reader.readLine; line != null }) {
      jb.append(line);
    }

    val json: JsValue = Json.parse(jb.toString)
    
    val id = ""
    val name = json \ "name"
    val email = json \ "email"

    val user = new User(id, name.as[String], email.as[String])
    val createdUser = User.create(user)
    
    println("Created User:")
    println(createdUser.id)
    println(createdUser.name)
    println(createdUser.email)

    val jsonObj = Json.obj("element" -> createdUser)
    resp.getWriter.print(jsonObj)
  }
  
  override def doDelete(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doDelete(req, resp)

    val oid = req.getPathInfo.substring(req.getPathInfo.lastIndexOf("/") + 1)
    User.delete(oid)

    val jsonObj = Json.obj("msg" -> "Se ha eliminado al usuario exitosamente")
    resp.getWriter.print(jsonObj)
  }
}
