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

import s_mach.metadata._
import s_mach.explain_json._
import s_mach.explain_json.JsonExplanationNode._
import s_mach.i18n.I18NConfig

object ExampleExpectedOutput {
  def expected_jsonExplanation_Name(implicit i18ncfg: I18NConfig) : JsonExplanation =
    TypeMetadata.Rec(
        JsonObject(),
        Seq(
          "first" -> explainPlayJson[String],
          "middle" -> explainPlayJson[Option[String]],
          "last" -> explainPlayJson[String]
        )
      )

  def expected_jsonExplanation_Person(implicit i18ncfg: I18NConfig)  : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(),
      Seq(
        "name" -> expected_jsonExplanation_Name,
        "age" -> explainPlayJson[Int],
        "friendIds" -> explainPlayJson[List[Int]]
      )
    )
}
