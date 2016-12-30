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
       ;it1i ,,,:::;;;::1tti      s_mach.explain_play_json
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.explain_play_json

import scala.language.higherKinds
import play.api.libs.json.JsValue
import s_mach.explain_json.JsonBuilderFactory
import s_mach.explain_json.JsonExplanationNode._
import s_mach.explain_play_json.impl.ExplainPlayJsonOps

object ExplainPlayJsonImplicits extends ExplainPlayJsonImplicits
trait ExplainPlayJsonImplicits {

  implicit val explainPlayJson_Boolean = ExplainPlayJson.forVal[Boolean](JsonBoolean())
  implicit val explainPlayJson_Byte = ExplainPlayJson.forVal[Byte](JsonInteger())
  implicit val explainPlayJson_Short = ExplainPlayJson.forVal[Short](JsonInteger())
  implicit val explainPlayJson_Int = ExplainPlayJson.forVal[Int](JsonInteger())
  implicit val explainPlayJson_Long = ExplainPlayJson.forVal[Long](JsonInteger())
  implicit val explainPlayJson_Float = ExplainPlayJson.forVal[Float](JsonNumber())
  implicit val explainPlayJson_Double = ExplainPlayJson.forVal[Double](JsonNumber())
  implicit val explainPlayJson_Char = ExplainPlayJson.forVal[Char](JsonString())
  implicit val explainPlayJson_String = ExplainPlayJson.forVal[String](JsonString())
  implicit val explainPlayJson_BigInt = ExplainPlayJson.forVal[BigInt](JsonInteger())
  implicit val explainPlayJson_BigDecimal = ExplainPlayJson.forVal[BigDecimal](JsonNumber())

  implicit object JsonBuilderFactory_JsValue extends JsonBuilderFactory[JsValue] {
    def apply() = PlayJsonBuilder()
  }

  implicit def forOption[A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[Option[A]] = ExplainPlayJsonOps.forOption

  implicit def forTraversable[M[AA] <: Traversable[AA],A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[M[A]] = ExplainPlayJsonOps.forTraversable
}
