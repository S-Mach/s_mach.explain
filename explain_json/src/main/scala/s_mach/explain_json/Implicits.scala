/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____       __  ___              __
          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
       Lft1,:;:       , 1tfL:
       ;it1i ,,,:::;;;::1tti      s_mach.explain_json
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.explain_json

import scala.language.higherKinds

object Implicits extends Implicits
trait Implicits {
  implicit object JsonStringBuilderFactory extends JsonBuilderFactory[String] {
    def apply() = JsonStringBuilder(1024)
  }

  implicit object buildJson_Byte extends BuildJson[Byte] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Byte) = {
      builder.append(a.toInt)
    }
  }

  implicit object buildJson_Short extends BuildJson[Short] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Short) = {
      builder.append(a.toInt)
    }
  }

  implicit object buildJson_Int extends BuildJson[Int] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Int) = {
      builder.append(a)
    }
  }

  implicit object buildJson_Long extends BuildJson[Long] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Long) = {
      builder.append(a)
    }
  }

  implicit object buildJson_Float extends BuildJson[Float] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Float) = {
      builder.append(a.toDouble)
    }
  }

  implicit object buildJson_Double extends BuildJson[Double] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Double) = {
      builder.append(a)
    }
  }

  implicit object buildJson_BigInt extends BuildJson[BigInt] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: BigInt) = {
      builder.append(BigDecimal(a))
    }
  }

  implicit object buildJson_BigDecimal extends BuildJson[BigDecimal] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: BigDecimal) = {
      builder.append(a)
    }
  }

  implicit object buildJson_String extends BuildJson[String] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: String) = {
      builder.append(a)
    }
  }

  implicit def buildJson_MapStringA[A](implicit buildJson: BuildJson[A]) = new BuildJson[Map[String,A]] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Map[String, A]) = {
      import builder._
      appendObject {
        a.foreach { case (key,value) =>
          appendField(key) {
            value.buildJson(builder)
          }
        }
      }
    }
  }

  implicit def buildJson_SeqA[M[AA] <: Seq[AA], A](implicit buildJson: BuildJson[A]) = new BuildJson[M[A]] {
    def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: M[A]) = {
      import builder._
      appendArray {
        a.foreach(_.buildJson(builder))
      }
    }
  }

  implicit def buildJson_Metadata[A](implicit b: BuildJson[A]) =
    new impl.MetadataBuildJsonImpl[A]()

  implicit def buildJson_TypeMetadata[A](implicit b: BuildJson[A]) =
    new impl.TypeMetadataBuildJsonImpl[A]()
}
