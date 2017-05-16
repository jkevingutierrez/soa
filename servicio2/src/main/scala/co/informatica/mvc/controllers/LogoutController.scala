package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import play.api.libs.json._

class LogoutController extends BaseController {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    super.doGet(req, resp)

    val session = req.getSession(false)
    
    if (session != null) {
      session.invalidate
    }
    
    val jsonObj = Json.obj("msg" -> "Se ha cerrado sesion exitosamente")
    resp.getWriter().print(jsonObj)
    resp.getWriter.close()
  }

}
