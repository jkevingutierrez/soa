package co.informatica.mvc.models

trait DatabaseFactory {
  protected lazy val SERVER: String = ""
  protected lazy val PORT: Int = 0
  protected lazy val USER: String = ""
  protected lazy val PASSWORD: String = ""
  protected lazy val DATABASE: String = ""
  protected lazy val db: Object = new Object

  def getDatabase: Object = db

}
