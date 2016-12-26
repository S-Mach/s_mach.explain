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
import ExtendedExampleUsage._
import s_mach.metadata.{Cardinality, TypeMetadata}
import s_mach.explain_json.TestDI._

class JsonExplanationPrintTypeRemarksTest extends FlatSpec with Matchers {
  "jsonExplanation_String.printRemarks" should "return TypeRemarks based on JSON explanation for String" in {
    jsonExplanation_String.printRemarks should equal(TypeMetadata.Val(
      List("must be string", "string_rule1","string_rule2","string_comment1","string_comment2")
    ))
  }

  "jsonExplanation_Option_String.printRemarks" should "return TypeRemarks based on JSON explanation for Option[String]" in {
    jsonExplanation_Option_String.printRemarks should equal(TypeMetadata.Arr(
      Nil,
      Cardinality.ZeroOrOne,
      TypeMetadata.Val(
        List("must be string","optional","string_rule1","string_rule2","string_comment1","string_comment2")
      )
    ))
  }

  "jsonExplanation_List_Int.printRemarks" should "return TypeRemarks based on JSON explanation for List[Int]" in {
    jsonExplanation_List_Int.printRemarks should equal(TypeMetadata.Arr(
      List("must be array"),
      Cardinality.ZeroOrMore,
      jsonExplanation_Int.printRemarks
    ))
  }


  "jsonExplanation_Name.printRemarks" should "return TypeRemarks based on JSON explanation for Name" in {
    jsonExplanation_Name.printRemarks should equal(TypeMetadata.Rec(
      List("name_type_rule1","name_type_rule2","name_type_comment1","name_type_comment2"),
      Seq(
        "first" -> TypeMetadata.Val(
          List(
            "must be string",
            "first_field_rule1",
            "first_field_rule2",
            "string_rule1",
            "string_rule2",
            "first_field_comment1",
            "first_field_comment2",
            "string_comment1",
            "string_comment2"
          )
        ),
        "middle" -> TypeMetadata.Arr(
          Nil,
          Cardinality.ZeroOrOne,
          TypeMetadata.Val(
            List("must be string","optional","string_rule1","string_rule2","string_comment1","string_comment2")
          )
        ),
        "last" -> jsonExplanation_String.printRemarks
      )
    ))
  }


  "jsonExplanation_Person.printRemarks" should "return TypeRemarks based on JSON explanation for Person" in {
    jsonExplanation_Person.printRemarks should equal(TypeMetadata.Rec[List[String]](
        List("person_type_rule1","person_type_rule2","person_type_comment1","person_type_comment2"),
        Seq(
          "name" -> {
            val base = jsonExplanation_Name.printRemarks
            val remarks = List(
              "name_field_rule1",
              "name_field_rule2",
              "name_type_rule1",
              "name_type_rule2",
              "name_field_comment1",
              "name_field_comment2",
              "name_type_comment1",
              "name_type_comment2"
            )

            TypeMetadata.Rec(
              remarks,
              base.asInstanceOf[TypeMetadata.Rec[List[String]]].fields
            )
          },
          "age" -> jsonExplanation_Int.printRemarks,
          "friendIds" -> jsonExplanation_List_Int.printRemarks
        )
    ))
  }


  "jsonExplanation_Person.printRemarks.print" should "return List of strings based on JSON explanation for Person" in {
    jsonExplanation_Person.printRemarks.print should equal(List(
      "this: person_type_rule1",
      "this: person_type_rule2",
      "this: person_type_comment1",
      "this: person_type_comment2",
      "name: name_field_rule1",
      "name: name_field_rule2",
      "name: name_type_rule1",
      "name: name_type_rule2",
      "name: name_field_comment1",
      "name: name_field_comment2",
      "name: name_type_comment1",
      "name: name_type_comment2",
      "name.first: must be string",
      "name.first: first_field_rule1",
      "name.first: first_field_rule2",
      "name.first: string_rule1",
      "name.first: string_rule2",
      "name.first: first_field_comment1",
      "name.first: first_field_comment2",
      "name.first: string_comment1",
      "name.first: string_comment2",
      "name.middle: must be string",
      "name.middle: optional",
      "name.middle: string_rule1",
      "name.middle: string_rule2",
      "name.middle: string_comment1",
      "name.middle: string_comment2",
      "name.last: must be string",
      "name.last: string_rule1",
      "name.last: string_rule2",
      "name.last: string_comment1",
      "name.last: string_comment2",
      "age: must be integer",
      "age: int_rule1",
      "age: int_rule2",
      "age: int_comment1",
      "age: int_comment2",
      "friendIds: must be array",
      "friendIds[*]: must be integer",
      "friendIds[*]: int_rule1",
      "friendIds[*]: int_rule2",
      "friendIds[*]: int_comment1",
      "friendIds[*]: int_comment2"
    ))
  }

}
