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

import s_mach.explain_json.JsonExplanationNode._
import s_mach.metadata._

object TestCases {

  val jsonExplanation_Boolean : JsonExplanation =
    TypeMetadata.Val(JsonBoolean())
  val jsonExplanation_Int : JsonExplanation =
    TypeMetadata.Val(JsonInteger())
  val jsonExplanation_Double : JsonExplanation =
    TypeMetadata.Val(JsonNumber())
  val jsonExplanation_String : JsonExplanation =
    TypeMetadata.Val(JsonString())

  val jsonExplanation_Seq_Int : JsonExplanation =
    TypeMetadata.Arr(JsonArray(),Cardinality.ZeroOrMore,jsonExplanation_Int)

  // A prototypical implicit def generated JSON explanation for Option[String]
  val jsonExplanation_Option_String : JsonExplanation =
    TypeMetadata.Arr(
      OptionMarker,
      Cardinality.ZeroOrOne,
      TypeMetadata.Val(
        jsonExplanation_String.value.asInstanceOf[JsonExplanationNode.JsonType].isOptional(true)
      )
    )

  case class Z(
    s: String
  )

  case class Zoo(
    b: Boolean,
    d: Double,
    i: Integer,
    s: String,
    os: Option[String],
    xi: Seq[Int],
    z: Z
  )

  val jsonExplanation_Z : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(),
      Seq(
        "s" -> jsonExplanation_String
      )
    )

  val jsonExplanation_Zoo : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(),
      Seq(
        "b" -> jsonExplanation_Boolean,
        "d" -> jsonExplanation_Double,
        "i" -> jsonExplanation_Int,
        "s" -> jsonExplanation_String,
        "os" -> jsonExplanation_Option_String,
        "xi" -> jsonExplanation_Seq_Int,
        "z" -> jsonExplanation_Z
      )
    )


}
