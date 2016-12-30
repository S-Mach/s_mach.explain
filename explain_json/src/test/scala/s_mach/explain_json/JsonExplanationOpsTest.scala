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
import s_mach.explain_json.JsonExplanationNode._
import s_mach.explain_json.impl.JsonExplanationOps._
import s_mach.string.CharGroup._

class JsonExplanationOpsTest extends FlatSpec with Matchers {
  import TestDI._
  
  "explainCharGroup" should "print an i18n explanation for a character group" in {
    explainCharGroup(UnicodeLetter) shouldBe "unicode letters"
    explainCharGroup(UppercaseLetter) shouldBe "uppercase letters"
    explainCharGroup(LowercaseLetter) shouldBe "lowercase letters"
    explainCharGroup(Letter) shouldBe "letters"
    explainCharGroup(WordLetter) shouldBe "word letters"
    explainCharGroup(Digit) shouldBe "digits"
    explainCharGroup(Underscore) shouldBe "underscores"
    explainCharGroup(Hyphen) shouldBe "hyphens"
    explainCharGroup(Space) shouldBe "spaces"
    explainCharGroup(Whitespace) shouldBe "whitespace"
  }

  "explainCharGroups" should "print an i18n explanation for a seq of character groups" in {
    explainCharGroups(Seq(Letter,Digit,Space)) shouldBe "must contain only letters, digits or spaces"
  }

  "printJsonType" should "print an i18n string for a JsonType" in {
    printJsonType(JsonBoolean()) shouldBe "boolean"
    printJsonType(JsonString()) shouldBe "string"
    printJsonType(JsonObject()) shouldBe "object"
    printJsonType(JsonNumber()) shouldBe "number"
    printJsonType(JsonInteger()) shouldBe "integer"
    printJsonType(JsonArray()) shouldBe "array"
  }

  "printJsonTypeRemark" should "print an i18n type remark for a JsonType" in {
    printJsonTypeRemark(JsonBoolean()) shouldBe "must be boolean"
  }

  "printJsonRuleRemark" should "print an i18n rule remark for a JsonRule" in {
    printJsonRuleRemark(JsonRule.Maximum(BigDecimal("0"),true)) shouldBe "must be less than 0"
    printJsonRuleRemark(JsonRule.Maximum(BigDecimal("0"),false)) shouldBe "must be less than or equal to 0"
    printJsonRuleRemark(JsonRule.Minimum(BigDecimal("0"),true)) shouldBe "must be greater than 0"
    printJsonRuleRemark(JsonRule.Minimum(BigDecimal("0"),false)) shouldBe "must be greater than or equal to 0"
    printJsonRuleRemark(JsonRule.StringMaxLength(64)) shouldBe "must not be longer than 64 characters"
    printJsonRuleRemark(JsonRule.StringMinLength(64)) shouldBe "must have at least 64 characters"
    printJsonRuleRemark(JsonRule.StringPattern("[A-Za-z0-9_]+")) shouldBe "must match regex pattern '[A-Za-z0-9_]+'"
  }
}
