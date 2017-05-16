package co.informatica.mvc.models

import com.mongodb.casbah.Imports._

object MongoFactory extends DatabaseFactory {

  // override protected lazy val SERVER = "localhost"
  // override protected lazy val PORT   = 27017
  // override protected lazy val DATABASE = "test"
  // protected val mongoClient = MongoClient(SERVER, PORT)

  override protected lazy val SERVER = "ds161190.mlab.com"
  override protected lazy val PORT = 61190
  override protected lazy val USER = "heroku_mkmf5xpd"
  override protected lazy val PASSWORD = "5rvv6dlhv3ulg57lvrpppdmkfp"
  override protected lazy val DATABASE = "heroku_mkmf5xpd"
  override protected lazy val db = mongoClient(DATABASE)
  protected val URI = MongoClientURI("mongodb://" + USER + ":" + PASSWORD + "@" + SERVER + ":" + PORT + "/" + DATABASE)
  protected val mongoClient = MongoClient(URI)

  def collection(coll: String) = db(coll)

}
