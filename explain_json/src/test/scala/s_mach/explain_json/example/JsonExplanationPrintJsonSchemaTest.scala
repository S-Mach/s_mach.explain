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
import s_mach.explain_json.Util._
import s_mach.explain_json.TestDI._

class JsonExplanationPrintJsonSchemaTest extends FlatSpec with Matchers {

  "printJsonSchema" should "correctly render JsonSchema for String" in {
    jsonExplanation_String.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "string",
  "additionalRules" : [ "string_rule1", "string_rule2" ],
  "comments" : [ "string_comment1", "string_comment2" ]
}"""
    )
  }

 "printJsonSchema[Int]" should "correctly render JsonSchema for Int" in {
    jsonExplanation_Int.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "integer",
  "additionalRules" : [ "int_rule1", "int_rule2" ],
  "comments" : [ "int_comment1", "int_comment2" ]
}"""
    )
  }

  // Note: Option are elided when converting to JSON
 "printJsonSchema[Option[String]]" should "correctly render JsonSchema for Option[String]" in {
   jsonExplanation_Option_String.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "string",
  "additionalRules" : [ "string_rule1", "string_rule2" ],
  "comments" : [ "string_comment1", "string_comment2" ]
}"""
    )
  }

 "printJsonSchema[Name]" should "correctly render JsonSchema for Name" in {
    jsonExplanation_Name.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "object",
  "properties" : {
    "first" : {
      "id" : "http://test.org/first",
      "type" : "string",
      "additionalRules" : [ "first_field_rule1", "first_field_rule2", "string_rule1", "string_rule2" ],
      "comments" : [ "first_field_comment1", "first_field_comment2", "string_comment1", "string_comment2" ]
    },
    "middle" : {
      "id" : "http://test.org/middle",
      "type" : "string",
      "additionalRules" : [ "string_rule1", "string_rule2" ],
      "comments" : [ "string_comment1", "string_comment2" ]
    },
    "last" : {
      "id" : "http://test.org/last",
      "type" : "string",
      "additionalRules" : [ "string_rule1", "string_rule2" ],
      "comments" : [ "string_comment1", "string_comment2" ]
    }
  },
  "additionalProperties" : true,
  "required" : [ "first", "last" ],
  "additionalRules" : [ "name_type_rule1", "name_type_rule2" ],
  "comments" : [ "name_type_comment1", "name_type_comment2" ]
}"""
    )
  }

 "printJsonSchema[Person]" should "correctly render JsonSchema for Person" in {
    jsonExplanation_Person.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "object",
  "properties" : {
    "name" : {
      "id" : "http://test.org/name",
      "type" : "object",
      "properties" : {
        "first" : {
          "id" : "http://test.org/name/first",
          "type" : "string",
          "additionalRules" : [ "first_field_rule1", "first_field_rule2", "string_rule1", "string_rule2" ],
          "comments" : [ "first_field_comment1", "first_field_comment2", "string_comment1", "string_comment2" ]
        },
        "middle" : {
          "id" : "http://test.org/name/middle",
          "type" : "string",
          "additionalRules" : [ "string_rule1", "string_rule2" ],
          "comments" : [ "string_comment1", "string_comment2" ]
        },
        "last" : {
          "id" : "http://test.org/name/last",
          "type" : "string",
          "additionalRules" : [ "string_rule1", "string_rule2" ],
          "comments" : [ "string_comment1", "string_comment2" ]
        }
      },
      "additionalProperties" : true,
      "required" : [ "first", "last" ],
      "additionalRules" : [ "name_field_rule1", "name_field_rule2", "name_type_rule1", "name_type_rule2" ],
      "comments" : [ "name_field_comment1", "name_field_comment2", "name_type_comment1", "name_type_comment2" ]
    },
    "age" : {
      "id" : "http://test.org/age",
      "type" : "integer",
      "additionalRules" : [ "int_rule1", "int_rule2" ],
      "comments" : [ "int_comment1", "int_comment2" ]
    },
    "friendIds" : {
      "id" : "http://test.org/friendIds",
      "type" : "array",
      "minItems" : 0,
      "uniqueItems" : false,
      "additionalItems" : false,
      "items" : {
        "id" : "http://test.org/friendIds/1",
        "type" : "integer",
        "additionalRules" : [ "int_rule1", "int_rule2" ],
        "comments" : [ "int_comment1", "int_comment2" ]
      }
    }
  },
  "additionalProperties" : true,
  "required" : [ "name", "age", "friendIds" ],
  "additionalRules" : [ "person_type_rule1", "person_type_rule2" ],
  "comments" : [ "person_type_comment1", "person_type_comment2" ]
}"""
    )
  }


}
