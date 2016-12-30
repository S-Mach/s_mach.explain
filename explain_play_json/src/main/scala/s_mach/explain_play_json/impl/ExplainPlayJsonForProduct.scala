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
package s_mach.explain_play_json.impl

import s_mach.codetools.macros.ProductBuilder
import s_mach.explain_json.JsonExplanationNode
import s_mach.metadata.TypeMetadata
import s_mach.explain_play_json._

object ExplainPlayJsonForProduct {
  case class FieldExplainPlayJson[A,B](
    unapply: A => B,
    efb: ExplainPlayJson[B]
  )
}

case class ExplainPlayJsonForProduct[A](
  fields: List[(String,ExplainPlayJsonForProduct.FieldExplainPlayJson[A,_])] = Nil
) extends ProductBuilder[ExplainPlayJson,A] {
  import ExplainPlayJsonForProduct._

  def field[B](
    name: String, 
    unapply: A => B
  )(
    f: ExplainPlayJson[B] => ExplainPlayJson[B]
  )(implicit baseVb: ExplainPlayJson[B]) = {
    val efb = f(baseVb)
    val fef = FieldExplainPlayJson(unapply,efb)
    copy(fields =
      name -> fef :: fields
    )
  }

  def build() = ExplainPlayJson { implicit i18ncfg =>
    TypeMetadata.Rec[JsonExplanationNode](
      JsonExplanationNode.JsonObject(),
      fields.map { case (f,fieldExplainPlayJson) =>
        f -> fieldExplainPlayJson.efb.explain
      }.reverse
    )
  }
}