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

import org.scalatest.{FlatSpec, Matchers}
import s_mach.metadata.{Cardinality, Metadata, TypeMetadata}
import Util._

class ImplicitsTest extends FlatSpec with Matchers {
  "JsonStringBuilderFactory" should "create a JsonBuilderFactory for String" in {
    implicitly[JsonBuilderFactory[String]].apply().isInstanceOf[JsonStringBuilder] shouldBe true
  }
  "buildJson_Byte" should "build a number to a JsonBuilder" in {
    123.toByte.printJson[String] shouldBe "123"
  }
  "buildJson_Short" should "build a number to a JsonBuilder" in {
    123.toShort.printJson[String] shouldBe "123"
  }
  "buildJson_Int" should "build a number to a JsonBuilder" in {
    123.printJson[String] shouldBe "123"
  }
  "buildJson_Long" should "build a number to a JsonBuilder" in {
    123l.printJson[String] shouldBe "123"
  }
  "buildJson_Float" should "build a number to a JsonBuilder" in {
    123.0.toFloat.printJson[String] shouldBe "123.0"
  }
  "buildJson_Double" should "build a number to a JsonBuilder" in {
    123.0.printJson[String] shouldBe "123.0"
  }
  "buildJson_Seq" should "build a JSON array to a JsonBuilder" in {
    List("1","2","3").printJson[String] shouldBe """["1","2","3"]"""
  }
  "buildJson_MapStringA" should "build a JSON object to a JsonBuilder" in {
    Map("1" -> 1,"2" -> 2,"3" -> 3).printJson[String] shouldBe """{"1":1,"2":2,"3":3}"""
  }
  "buildJson_Metadata" should "build a JSON representation of Metadata" in {
    val m : Metadata[List[String]] = Metadata.Rec(
      // ensure value is emitted as "this" field
      value = List("invalid"),
      fields = Seq(
        "id" -> Metadata.Val(List("ok")),
        "name" -> Metadata.Rec(
          value = List("invalid"),
          fields = Seq(
            // ensure empty fields are pruned
            "first" -> Metadata.Val(Nil),
            "last" -> Metadata.Val(List("invalid")),
            "nickname" -> Metadata.Arr(
              // ensure value here is not emitted
              value = List("should not be emitted"),
              cardinality = Cardinality.ZeroOrOne,
              members = Seq(Metadata.Val(List("ok")))
            )
          )
        ),
        "friendIds" -> Metadata.Arr(
          value = List("ok"),
          cardinality = Cardinality.ZeroOrMore,
          // ensure members are emitted with index as field to value
          members = Seq(
            Metadata.Val(List("ok")),
            // ensure 1 is pruned
            Metadata.Val(Nil),
            Metadata.Val(List("invalid"))
          )
        )
      )
    )

    m.printJson[String].prettyJson shouldBe
"""{
  "this" : [ "invalid" ],
  "id" : [ "ok" ],
  "name" : {
    "this" : [ "invalid" ],
    "last" : [ "invalid" ],
    "nickname" : [ "ok" ]
  },
  "friendIds" : {
    "this" : [ "ok" ],
    "0" : [ "ok" ],
    "2" : [ "invalid" ]
  }
}"""

  }

  "buildJson_TypeMetadata" should "build a JSON representation of TypeMetadata" in {
    val tm : TypeMetadata[List[String]] = TypeMetadata.Rec(
      // ensure empty "this" is pruned
      value = Nil,
      fields = Seq(
        "id" -> TypeMetadata.Val(List("must be greater than 0")),
        "name" -> TypeMetadata.Rec(
          value = List("first and last name must not be longer than 64 characters"),
          fields = Seq(
            "first" -> TypeMetadata.Val(List("must only contain letters and spaces")),
            "last" -> TypeMetadata.Val(List("must only contain letters and spaces")),
            // ensure arrays with zero or one member are output as fields and not arrays
            "nickname" -> TypeMetadata.Arr(
              // ensure array value is ignored for cardinality zero or one
              value = List("should be ignored"),
              cardinality = Cardinality.ZeroOrOne,
              membersTypeMetadata = TypeMetadata.Val(List("must only contain letters and spaces"))
            )
          )
        ),
        "friendIds" -> TypeMetadata.Arr(
          value = List("can contain no more than 10 members"),
          cardinality = Cardinality.ZeroOrMore,
          membersTypeMetadata = TypeMetadata.Val(List("must be greater than 0"))
        )
      )
    )

    tm.printJson.prettyJson shouldBe
"""{
  "id" : [ "must be greater than 0" ],
  "name" : {
    "this" : [ "first and last name must not be longer than 64 characters" ],
    "first" : [ "must only contain letters and spaces" ],
    "last" : [ "must only contain letters and spaces" ],
    "nickname" : [ "must only contain letters and spaces" ]
  },
  "friendIds" : {
    "this" : [ "can contain no more than 10 members" ],
    "*" : [ "must be greater than 0" ]
  }
}"""
  }
}
