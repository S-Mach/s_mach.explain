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

import org.scalatest.{FlatSpec, Matchers}
import s_mach.explain_json.example.ExtendedExampleUsage._
import s_mach.metadata.{Cardinality, Metadata}
import s_mach.explain_json._
import s_mach.explain_json.Util._
import s_mach.explain_json.TestDI._

class TypeMetadataPrintJsonTest extends FlatSpec with Matchers {
  val m : Metadata[List[String]] = Metadata.Rec(
    value = List("invalid"),
    fields = Seq(
      "id" -> Metadata.Val(List("ok")),
      "name" -> Metadata.Rec(
        value = List("invalid"),
        fields = Seq(
          "first" -> Metadata.Val(List("ok")),
          "last" -> Metadata.Val(List("invalid"))
        )
      ),
      "friendIds" -> Metadata.Arr(
        value = List("ok"),
        cardinality = Cardinality.ZeroOrMore,
        members = Seq(
          Metadata.Val(List("ok")),
          Metadata.Val(List("ok")),
          Metadata.Val(List("invalid"))
        )
      )
    )
  )

  "Remarks.printJson" should "correctly print JSON for TypeMetadata[A]" in {
    m.printJson.prettyJson should equal(
"""{
  "this" : [ "invalid" ],
  "id" : [ "ok" ],
  "name" : {
    "this" : [ "invalid" ],
    "first" : [ "ok" ],
    "last" : [ "invalid" ]
  },
  "friendIds" : {
    "this" : [ "ok" ],
    "0" : [ "ok" ],
    "1" : [ "ok" ],
    "2" : [ "invalid" ]
  }
}""")
  }

  "TypeRemarks.printRemarksJson" should "correctly print JSON for TypeMetadata[A]" in {
    jsonExplanation_Person.printRemarks.printJson.prettyJson should equal(
"""{
  "this" : [ "person_type_rule1", "person_type_rule2", "person_type_comment1", "person_type_comment2" ],
  "name" : {
    "this" : [ "name_field_rule1", "name_field_rule2", "name_type_rule1", "name_type_rule2", "name_field_comment1", "name_field_comment2", "name_type_comment1", "name_type_comment2" ],
    "first" : [ "must be string", "first_field_rule1", "first_field_rule2", "string_rule1", "string_rule2", "first_field_comment1", "first_field_comment2", "string_comment1", "string_comment2" ],
    "middle" : [ "must be string", "optional", "string_rule1", "string_rule2", "string_comment1", "string_comment2" ],
    "last" : [ "must be string", "string_rule1", "string_rule2", "string_comment1", "string_comment2" ]
  },
  "age" : [ "must be integer", "int_rule1", "int_rule2", "int_comment1", "int_comment2" ],
  "friendIds" : {
    "this" : [ "must be array" ],
    "*" : [ "must be integer", "int_rule1", "int_rule2", "int_comment1", "int_comment2" ]
  }
}""")
  }


}
