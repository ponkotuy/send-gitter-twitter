package gitter

import java.util.Properties

object GitterSettings {
  val conf = new Properties()
  conf.load(getClass.getResource("gitter.properties").openStream())

  val oauth = OAuth.fromProperties
  val code = Option(conf.getProperty("oauth.code"))
  val accessToken = Option(conf.getProperty("oauth.access_token"))

  case class OAuth(key: String, secret: String, redirectUrl: String)
  object OAuth {
    def fromProperties: Option[OAuth] = {
      for {
        key <- Option(conf.getProperty("oauth.key"))
        secret <- Option(conf.getProperty("oauth.secret"))
        url <- Option(conf.getProperty("oauth.redirect_url"))
      } yield {
        OAuth(key, secret, url)
      }
    }
  }
}
