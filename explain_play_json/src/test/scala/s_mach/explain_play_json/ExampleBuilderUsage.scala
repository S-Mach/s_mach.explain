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

object ExampleBuilderUsage {
  case class Name(
    first: String,
    middle: Option[String],
    last: String
  )
  implicit val explainJson_Name =
    ExplainPlayJson.productBuilder[Name]
      .field("first",_.first)()
      .field("middle",_.middle)()
      .field("last",_.last)()
      .build()

  case class Person(
    name: Name,
    age : Int,
    friendIds: List[Int]
  )
  implicit val explainJson_Person =
    ExplainPlayJson.productBuilder[Person]
      .field("name",_.name)()
      .field("age",_.age)()
      .field("friendIds",_.friendIds)()
      .build()
}