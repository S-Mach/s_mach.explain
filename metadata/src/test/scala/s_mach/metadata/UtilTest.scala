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

import org.scalatest.{Matchers, FlatSpec}

class UtilTest extends FlatSpec with Matchers {
  "Metadata.Path.print" should "properly print a path" in {
    import Metadata.PathNode._
    val p1 = List.empty[Metadata.PathNode]
    p1.print should be("this")
    val p2 = SelectField("name") :: Nil
    p2.print should be("name")
    val p3 = SelectField("first") :: SelectField("name") :: Nil
    p3.print should be("name.first")
    val p4 = SelectMember(Cardinality.ZeroOrMore,0) :: SelectField("friendIds") :: Nil
    p4.print should be("friendIds[0]")
    val p5 = SelectMember(Cardinality.ZeroOrOne,0) :: SelectField("nickName") :: Nil
    p5.print should be("nickName")
  }

  "TypeMetadata.Path.print" should "properly print a path" in {
    import TypeMetadata.PathNode._
    val p1 = List.empty[Metadata.PathNode]
    p1.print should be("this")
    val p2 = SelectField("name") :: Nil
    p2.print should be("name")
    val p3 = SelectField("first") :: SelectField("name") :: Nil
    p3.print should be("name.first")
    val p4 = SelectMembers(Cardinality.ZeroOrMore) :: SelectField("friendIds") :: Nil
    p4.print should be("friendIds[*]")
    val p5 = SelectMembers(Cardinality.ZeroOrOne) :: SelectField("nickName") :: Nil
    p5.print should be("nickName")
  }
}
