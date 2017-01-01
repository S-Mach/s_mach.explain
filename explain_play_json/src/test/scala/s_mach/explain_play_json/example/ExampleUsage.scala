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
package s_mach.explain_play_json.example

object ExampleUsage {
  import java.util.Locale

  import s_mach.i18n._
  implicit val i18ncfg = I18NConfig(Locale.US)
  import play.api.libs.json.JsValue
  import s_mach.codetools.play_json._
  import s_mach.explain_json._
  import s_mach.explain_play_json._

  case class Name(
    first: String,
    middle: Option[String],
    last: String
  )
  implicit val explainJson_Name =
    ExplainPlayJson.forProductType[Name]

  case class Person(
    name: Name,
    age : Int,
    friendIds: List[Int]
  )
  implicit val explainJson_Person =
    ExplainPlayJson.forProductType[Person]

  println(explainPlayJson[Person].printJsonSchema[JsValue]("http://test.org").pretty)

  explainPlayJson[Person].printRemarks.printJson[JsValue].pretty

  explainPlayJson[Person].printRemarks
}