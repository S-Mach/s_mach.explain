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

import java.util.Locale

import org.scalatest.{FlatSpec, Matchers}
import s_mach.metadata.{Cardinality, TypeMetadata}
import s_mach.explain_json._
import JsonExplanationNode._
import s_mach.i18n.I18NConfig

class ExplainPlayJsonBuiltInsTest extends FlatSpec with Matchers {
  implicit val i18ncfg = I18NConfig(Locale.US)

  "explainPlayJson[String]" should "return implicit hard-coded JSON explanation for String" in {
    explainPlayJson[String] should equal(TypeMetadata.Val(JsonString()))
  }

  "explainPlayJson[Option[String]]" should "return code-generated JSON explanation for Option[String]" in {
    explainPlayJson[Option[String]] should equal(
      TypeMetadata.Arr(
        OptionMarker,
        Cardinality.ZeroOrOne,
        TypeMetadata.Val(JsonString(
          isOptional = true
        ))
      )
    )
  }

  "explainPlayJson[List[String]]" should "return code-generated JSON explanation for Option[String]" in {
    explainPlayJson[List[String]] should equal(
      TypeMetadata.Arr(
        JsonArray(),
        Cardinality.ZeroOrMore,
        TypeMetadata.Val(JsonString())
      )
    )
  }
}
