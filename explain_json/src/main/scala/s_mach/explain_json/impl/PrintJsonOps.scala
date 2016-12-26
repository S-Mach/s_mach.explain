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
package s_mach.explain_json.impl

object PrintJsonOps {
  // Note: not used currently, but saving this
//  def merge(js1: JsValue, js2: JsValue) : JsValue = {
//    (js1,js2) match {
//      case (JsNull,_) => js2
//      case (_,JsNull) => js1
//      case (JsUndefined(),_) => js2
//      case (_,JsUndefined()) => js1
//      case (JsArray(members1),JsArray(members2)) =>
//        JsArray(members1 ++ members2)
//      case (o1@JsObject(fields1),o2@JsObject(fields2)) =>
//        val fields =
//          (fields1.zip(Stream.from(0,2)) ++ fields2.zip(Stream.from(1,2)))
//            .map {
//              // always bump 'this' field to top of list
//              case (("this",jsv),_) => (("this",jsv),-1)
//              case t => t
//            }
//            .groupBy(_._1._1)
//            .mapValues(
//              _
//                .reduce { (t1,t2) =>
//                  val ((fieldName,jsv1),idx1) = t1
//                  val ((_,jsv2),idx2) = t2
//                  ((fieldName,merge(jsv1,jsv2)),Math.min(idx1,idx2))
//                }
//            )
//            .toSeq
//            .sortBy(_._2._2)
//            .map { case (fieldName,((_,jsv),_)) => (fieldName,jsv) }
//
//        JsObject(fields)
//      case (JsArray(members),jsv) =>
//        JsArray(members :+ jsv)
//      case (jsv,JsArray(members)) =>
//        JsArray(jsv +: members)
//      case _ =>
//        JsArray(Seq(js1,js2))
//    }
//  }


}
