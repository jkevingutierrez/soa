package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse, HttpSession, Cookie }
import play.api.libs.json._

import co.informatica.mvc.models.User

class LoginController extends BaseController {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    implicit val userFormat = Json.format[User]

    val session: HttpSession = req.getSession(false)

    val cookies = req.getCookies()
    
    if (cookies != null) {
      cookies.foreach {
        cookie => println(cookie.getName)
      }
    }
    
    println("Session " + session)

    if (session != null) {
      val userId = session.getAttribute("id").toString
      val userName = session.getAttribute("name").toString
      val userEmail = session.getAttribute("email").toString

      val user = new User(userId, userName, userEmail)

      println(userId, userName, userEmail)
      println(user)

      val jsonObj = Json.obj("element" -> user)
      resp.getWriter.print(jsonObj)
      resp.getWriter.close()
    } else {
      val jsonObj = Json.obj("msg" -> ("No hay ningun usuario loggeado"))
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND)
      resp.getWriter().print(jsonObj)
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
    val findedUser = User.find(user)

    findedUser match {
      case Some(findedUser) => {
        val session: HttpSession = req.getSession(true)
        val jsonObj = Json.obj("element" -> findedUser)

        val nameCookie = new Cookie("name", findedUser.name)

        nameCookie.setMaxAge(30 * 60);
        resp.addCookie(nameCookie);

        session.setAttribute("id", findedUser.id)
        session.setAttribute("name", findedUser.name)
        session.setAttribute("email", findedUser.email)
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
