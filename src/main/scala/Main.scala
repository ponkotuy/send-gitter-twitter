import java.util.Properties

import com.amatkivskiy.gitter.sdk.GitterOauthUtils
import com.amatkivskiy.gitter.sdk.credentials.{GitterDeveloperCredentials, SimpleGitterCredentialsProvider}
import com.amatkivskiy.gitter.sdk.model.response.AccessTokenResponse
import com.amatkivskiy.gitter.sdk.sync.client.{SyncGitterApiClient, SyncGitterAuthenticationClient}

object Main extends App {
  val oauth = GitterSettings.oauth.getOrElse(gitterPropertiesException)
  val gitterCredentials = new GitterCredentials(oauth)
  val gitterRoom = "5435ede9163965c9bc209ffb"
  args.headOption.fold(throw new RuntimeException("Not found arguments.")) { arg =>
      assert(arg.length <= 127, "Too long messages.")
//    gitterSendMessage(arg)
    Twiter.tweet(s"${arg} #MyFleetGirls")
  }

  def gitterCredentialsUrl(): Unit = {
    println(gitterCredentials.getUrl())
  }

  def gitterCredentialsCode(): Unit = {
    val gitter = new GitterToken(gitterCredentials, GitterSettings.code.getOrElse(gitterPropertiesException))
    println(gitter.token().accessToken)
  }

  def userRooms(): Unit = {
    val gitter = gitterApi()
    gitter.getCurrentUserRooms().foreach(println)
  }

  def gitterSendMessage(mes: String): Unit = {
    val gitter = gitterApi()
    gitter.sendMessage(gitterRoom, mes)
  }

  private def gitterPropertiesException = throw new RuntimeException("Wrong gitter.properties")
  private def gitterApi() = new Gitter(gitterCredentials, GitterSettings.accessToken.getOrElse(gitterPropertiesException))
}

object Twiter {
  import twitter4j._
  private val api = TwitterFactory.getSingleton
  def tweet(content: String): Status = {
    api.updateStatus(content)
  }
}

class GitterCredentials(oauth: GitterSettings.OAuth) {
  GitterDeveloperCredentials.init(new SimpleGitterCredentialsProvider(oauth.key, oauth.secret, oauth.redirectUrl))
  def getUrl(): String = {
    GitterOauthUtils.buildOauthUrl()
  }
}

class GitterToken(credentials: GitterCredentials, code: String) {
  private val client = new SyncGitterAuthenticationClient.Builder().build()
  def token(): AccessTokenResponse = {
    client.getAccessToken(code)
  }
}

class Gitter(credentials: GitterCredentials, accessToken: String) {
  import scala.collection.JavaConverters._
  private val client = new SyncGitterApiClient.Builder()
      .withAccountToken(accessToken)
      .build()
  def getCurrentUserRooms() = client.getCurrentUserRooms.asScala
  def sendMessage(roomId: String, message: String) = client.sendMessage(roomId, message)

}


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
