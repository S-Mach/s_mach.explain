///*
//                    ,i::,
//               :;;;;;;;
//              ;:,,::;.
//            1ft1;::;1tL
//              t1;::;1,
//               :;::;               _____       __  ___              __
//          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
//         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
//         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
//       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
//       Lft1,:;:       , 1tfL:
//       ;it1i ,,,:::;;;::1tti      s_mach.metadata
//         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
//         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
//          .L1 1tt1ttt,,Li
//            ...1LLLL...
//*/
//package s_mach.metadata.impl
//
//import s_mach.metadata.Cardinality
//
//object CardinalityOps {
//  def symbol(c: Cardinality) : String = {
//    import Cardinality._
//    c match {
//      case ZeroOrOne => "?"
//      case ZeroOrMore => "*"
//      case OneOrMore => "+"
//      case  MinMax(min: Int, max: Int) => s"{$min,$max}"
//    }
//  }
//}
