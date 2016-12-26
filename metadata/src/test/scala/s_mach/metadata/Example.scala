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
       ;it1i ,,,:::;;;::1tti      s_mach.metadata
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.metadata

object Example {

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

  m.print.foreach(println)

  val tm : TypeMetadata[List[String]] = TypeMetadata.Rec(
    value = List("id plus length(name) must be greater than 0"),
    fields = Seq(
      "id" -> TypeMetadata.Val(List("must be greater than 0")),
      "name" -> TypeMetadata.Rec(
        value = Nil,
        fields = Seq(
          "first" -> TypeMetadata.Val(List("must not be empty")),
          "last" -> TypeMetadata.Val(List("must not be empty"))
        )
      ),
      "friendIds" -> TypeMetadata.Arr(
        value = List("may contain zero or more members"),
        cardinality = Cardinality.ZeroOrMore,
        membersTypeMetadata = TypeMetadata.Val(List("must be greater than 0"))
      )
    )
  )

  tm.print.foreach(println)
}
