package gitter

import com.amatkivskiy.gitter.sdk.GitterOauthUtils
import com.amatkivskiy.gitter.sdk.credentials.{GitterDeveloperCredentials, SimpleGitterCredentialsProvider}
import com.amatkivskiy.gitter.sdk.model.response.AccessTokenResponse
import com.amatkivskiy.gitter.sdk.sync.client.{SyncGitterApiClient, SyncGitterAuthenticationClient}


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
