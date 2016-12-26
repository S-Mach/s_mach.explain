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
import TestCases._
import s_mach.explain_json.Util._
import TestDI._

class JsonExplanationPrintJsonSchemaTest extends FlatSpec with Matchers {

  "jsonExplanation_Boolean.printJsonSchema" should "correctly render JsonSchema for String" in {
    jsonExplanation_Boolean.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "boolean"
}"""
    )
  }

  "jsonExplanation_String.printJsonSchema" should "correctly render JsonSchema for String" in {
    jsonExplanation_String.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "string"
}"""
    )
  }

 "jsonExplanation_Int.printJsonSchema" should "correctly render JsonSchema for Int" in {
    jsonExplanation_Int.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "integer"
}"""
    )
  }

 "jsonExplanation_Double.printJsonSchema" should "correctly render JsonSchema for Int" in {
    jsonExplanation_Double.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "number"
}"""
    )
  }

 "jsonExplanation_Option_String.printJsonSchema" should "correctly render JsonSchema for Int" in {
    jsonExplanation_Option_String.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "string"
}"""
    )
  }

 "jsonExplanation_Seq_Int.printJsonSchema" should "correctly render JsonSchema for Int" in {
    jsonExplanation_Seq_Int.printJsonSchema[String]("http://test.org").prettyJson should be(
"""{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "id" : "http://test.org",
  "type" : "array",
  "minItems" : 0,
  "uniqueItems" : false,
  "additionalItems" : false,
  "items" : {
    "id" : "http://test.org/1",
    "type" : "integer"
  }
}"""
    )
  }

// "printJsonSchema[Name]" should "correctly render JsonSchema for a simple case class" in {
//    jsonExplanation_B.printJsonSchema[String]("http://test.org").prettyJson should be(
//"""{
//  "$schema" : "http://json-schema.org/draft-04/schema#",
//  "id" : "http://test.org",
//  "type" : "object",
//  "s" : {
//    "first" : {
//      "id" : "http://test.org/first",
//      "type" : "string"
//    }
//  },
//  "additionalProperties" : true,
//  "required" : [ "first", "last" ],
//  "additionalRules" : [ "name_type_rule1", "name_type_rule2" ],
//  "comments" : [ "name_type_comment1", "name_type_comment2" ]
//}"""
//    )
//  }
//
// "printJsonSchema[Person]" should "correctly render JsonSchema for a complex case class" in {
//    jsonExplanation_Zoo.printJsonSchema[String]("http://test.org").prettyJson should be(
//"""{
//  "$schema" : "http://json-schema.org/draft-04/schema#",
//  "id" : "http://test.org",
//  "type" : "object",
//  "properties" : {
//    "b" : {
//      "id" : "http://test.org/name/b",
//      "type" : "boolean"
//    },
//    "d" : {
//      "id" : "http://test.org/name/d",
//      "type" : "number"
//    },
//    "i" : {
//      "id" : "http://test.org/name/i",
//      "type" : "integer"
//    }
//    "s" : {
//      "id" : "http://test.org/name/s",
//      "type" : "string"
//    },
//    "os" : {
//      "id" : "http://test.org/name/os",
//      "type" : "array"
//    }
//    "xi" : {
//      "id" : "http://test.org/name/xi",
//      "type" : "array"
//    },
//    "z" : {
//    }
//  },
//  "additionalProperties" : true,
//  "required" : [ "name", "age", "friendIds" ]
//}"""
//    )
//  }


}
