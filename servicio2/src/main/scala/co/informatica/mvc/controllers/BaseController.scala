package co.informatica.mvc.controllers

import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

trait BaseController extends HttpServlet {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) = {
    println("GET: " + this.getClass.getName)
    println("getPathInfo: " + req.getPathInfo)
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) = {
    println("POST: " + this.getClass.getName)
  }

  override def doDelete(req: HttpServletRequest, resp: HttpServletResponse) = {
    println("Delete: " + this.getClass.getName)
  }

}
