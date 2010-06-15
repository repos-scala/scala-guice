package uk.me.lings.scalaguice

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

import com.google.inject._

class ScalaBindingExtensionsSpec extends WordSpec with ShouldMatchers {
  
  import BindingExtensions._

  def module(body: Binder => Unit) =  new Module {
      def configure(binder: Binder) = body(binder)
  }

  "Binding extensions" should {
  
    "allow binding source type using a type parameter" in {
      Guice createInjector module { binder =>
        binder.bindType[A].to(classOf[B])
      } getInstance classOf[A]
    }

    "allow binding target type using a type parameter" in {
      Guice createInjector module { binder =>
          binder.bindType[A].toType[B]
      } getInstance(classOf[A])
    }

    "allow binding from a generic type" in {
      val inst = Guice createInjector module { binder =>
          binder.bindType[Gen[String]].toType[C]
      } getInstance(new Key[Gen[String]] {})
      inst.get should equal ("String")
    }
    
    "allow binding between nested types" in {
      val inst = Guice createInjector module { binder =>
          binder.bindType[Outer.Gen[String]].toType[Outer.C]
      } getInstance(new Key[Outer.Gen[String]] {})
      inst.get should equal ("String")
    }

  }

}
