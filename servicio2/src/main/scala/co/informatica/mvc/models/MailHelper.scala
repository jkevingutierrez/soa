package co.informatica.mvc.models

import javax.mail.internet.{ InternetAddress, MimeMessage }
import javax.mail.{ Message, PasswordAuthentication, Session, Transport }

object MailHelper {

  def send(to: String, subject: String, content: String): Unit = {

    println("Sending Email")

    // Sender's email ID needs to be mentioned
    val from = "kevingutierrezg@gmail.com"

    val username = "kevingutierrezg@gmail.com"
    val password = ""

    // Get system properties
    val properties = System.getProperties

    // Setup mail server
    properties.setProperty("mail.smtp.host", "smtp.gmail.com")
    properties.setProperty("mail.smtp.port", "587")
    properties.setProperty("mail.smtp.auth", "true")
    properties.setProperty("mail.smtp.starttls.enable", "true")

    // Get the default Session object.
    val session = Session.getDefaultInstance(properties, new javax.mail.Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication = {
        new PasswordAuthentication(username, password)
      }
    })

    try { // Create a default MimeMessage object.
      val message = new MimeMessage(session)
      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from))
      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
      // Set Subject: header field
      message.setSubject(subject)
      // Now set the actual message
      message.setContent(content, "text/html; charset=utf-8")
      // Send message
      Transport.send(message)
    } catch {
      case e: Throwable => println(e)
    }
  }

}
