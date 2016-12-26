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

class RemarksTest extends FlatSpec with Matchers {
  import Example._

  "Remarks.print" should "print remarks" in {
    m.print should be(List(
      "this: invalid",
      "id: ok",
      "name: invalid",
      "name.first: ok",
      "name.last: invalid",
      "friendIds: ok",
      "friendIds[0]: ok",
      "friendIds[1]: ok",
      "friendIds[2]: invalid"
    ))
  }

  "TypeRemarks.print" should "print remarks" in {
    tm.print should be(List(
      "this: id plus length(name) must be greater than 0",
      "id: must be greater than 0",
      "name.first: must not be empty",
      "name.last: must not be empty",
      "friendIds: may contain zero or more members",
      "friendIds[*]: must be greater than 0"
    ))
  }
}
