package co.informatica.mvc.filters

import javax.servlet._
import javax.servlet.http.HttpServletResponse

class CorsFilter extends Filter {
  @throws[ServletException]
  override def init(filterConfig: FilterConfig): Unit = {
  }

  @throws[ServletException]
  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain): Unit = {
    val response = servletResponse.asInstanceOf[HttpServletResponse]
    response.setContentType("application/json")
    response.setCharacterEncoding("UTF-8")
    response.setHeader("Access-Control-Allow-Origin", "*")
    response.setHeader("Access-Control-Allow-Credentials", "true")
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, HEAD, OPTIONS")
    response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
    filterChain.doFilter(servletRequest, servletResponse)
  }

  override def destroy(): Unit = {
  }
}