import gitter.{Gitter, GitterCredentials, GitterSettings, GitterToken}

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
