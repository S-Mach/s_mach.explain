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
package s_mach.explain_json.example

import s_mach.explain_json.JsonExplanationNode.{JsonArray, _}
import s_mach.explain_json.{JsonExplanationNode, _}
import s_mach.metadata._

object ExtendedExampleUsage {
  // Note: specific explain json type-classes will auto-generate these
  // It isn't expected that these will be declared by hand
  val jsonExplanation_String : JsonExplanation =
    TypeMetadata.Val(JsonString(
      additionalRules = "string_rule1" :: "string_rule2" :: Nil,
      comments = "string_comment1" :: "string_comment2" :: Nil
    ))

  val jsonExplanation_Option_String : JsonExplanation =
    TypeMetadata.Arr(
      OptionMarker,
      Cardinality.ZeroOrOne,
      TypeMetadata.Val(
        jsonExplanation_String.value.asInstanceOf[JsonExplanationNode.JsonType].isOptional(true)
      )
    )

  val jsonExplanation_Name : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(
        additionalRules = "name_type_rule1" :: "name_type_rule2" :: Nil,
        comments = "name_type_comment1" :: "name_type_comment2" :: Nil
      ),
      Seq(
        "first" -> {
          val e = jsonExplanation_String
          val ev = e.value.asInstanceOf[JsonType]
          e.value(
            ev
              .additionalRules("first_field_rule1" :: "first_field_rule2" :: ev.additionalRules)
              .comments("first_field_comment1" :: "first_field_comment2" :: ev.comments)
          )
        },
        "middle" -> jsonExplanation_Option_String,
        "last" -> jsonExplanation_String
      )
    )

  val jsonExplanation_Int : JsonExplanation =
    TypeMetadata.Val(JsonInteger(
      additionalRules = "int_rule1" :: "int_rule2" :: Nil,
      comments = "int_comment1" :: "int_comment2" :: Nil
    ))

  val jsonExplanation_List_Int : JsonExplanation =
    TypeMetadata.Arr(
      JsonArray(),
      Cardinality.ZeroOrMore,
      jsonExplanation_Int
    )

  val jsonExplanation_Person : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(
        additionalRules = "person_type_rule1" :: "person_type_rule2" :: Nil,
        comments = "person_type_comment1" :: "person_type_comment2" :: Nil
      ),
      Seq(
        "name" -> {
          val e = jsonExplanation_Name
          val ev = e.value.asInstanceOf[JsonType]
          e.value(
            ev
              .additionalRules("name_field_rule1" :: "name_field_rule2" :: ev.additionalRules)
              .comments("name_field_comment1" :: "name_field_comment2" :: ev.comments)
          )
        },
        "age" -> jsonExplanation_Int,
        "friendIds" -> jsonExplanation_List_Int
      )
    )

}