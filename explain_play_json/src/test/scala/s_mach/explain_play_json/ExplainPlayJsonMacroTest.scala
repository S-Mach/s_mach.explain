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
import example.ExampleUsage._
import ExampleExpectedOutput._
import s_mach.i18n.I18NConfig

class ExplainPlayJsonMacroTest extends FlatSpec with Matchers {
  implicit val i18ncfg = I18NConfig(Locale.US)

  "explainPlayJson[Name]" should "return macro-generated JSON explanation for Name" in {
    explainPlayJson[Name] should equal(
      expected_jsonExplanation_Name
    )
  }

  "explainPlayJson[Person]" should "return macro-generated JSON explanation for Person" in {
    explainPlayJson[Person] should equal(
      expected_jsonExplanation_Person
    )
  }
}
