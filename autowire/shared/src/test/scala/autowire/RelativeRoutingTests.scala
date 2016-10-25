package autowire

import utest._
import upickle.default._
import utest.framework.ExecutionContext.RunNow

object RelativeRoutingTests extends TestSuite {
 import utest.PlatformShims.await

  val goodHand = Seq("Black Lotus", "Mountain", "Fireball", "Channel")

  val tests = TestSuite {
    'example {
      // shared API interface
      trait MagicalApi {
        def resetGame(): Seq[String]
        def shuffle(x: Int, s: String, d : String = "Foo"): String
        val table: TableApi
        var avartable: TableApi = _
      }

      trait TableApi {
        def getHand(playerName: String): Seq[String]
      }
      // server-side implementation, and router
      class MyApiImpl(c: String) extends MagicalApi {
        override def resetGame(): Seq[String] = ???
        override val table: TableApi = new TableApi {
          override def getHand(playerId: String): Seq[String] = {
            if (playerId == "Ben") {
              goodHand
            } else {
              Nil
            }
          }
        }
        override def shuffle(x: Int, s: String, d: String = "Foo"): String = x + ":" + s + ":" + d
      }


      trait UPickleSerializers extends Serializers[String, upickle.default.Reader, upickle.default.Writer] {
        override def write[WResult: Writer](r: WResult) = upickle.default.write(r)
        override def read[RResult: Reader](p: String) = upickle.default.read[RResult](p)
      }

      object UPickleServer extends autowire.Server[String, upickle.default.Reader, upickle.default.Writer] with UPickleSerializers
      val x = new MyApiImpl("aa")
      val router = UPickleServer.route[MagicalApi](x)

      // client-side implementation, and call-site
      object MyClient extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] with UPickleSerializers {
        override def doCall(req: Request) = {
          router(req)
        }
      }

      val a = await(MyClient[MagicalApi].shuffle(3, "lol").call())
      val b = await(MyClient[MagicalApi].table.getHand("Ben").call())

      assert("3:lol:Foo" == a)
      assert(goodHand == b)
    }
  }
}
