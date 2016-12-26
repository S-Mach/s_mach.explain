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

import s_mach.explain_json._
import s_mach.metadata.Metadata

class MetadataBuildJsonImpl[A](implicit
  buildVal: BuildJson[A]
) extends BuildJson[Metadata[A]] {

  def build(builder: JsonBuilder[_], a: Metadata[A]) = {
    import builder._
    val _buildVal = buildVal.build(builder,_:A)

    def loop : Metadata[A] => Boolean = {
      case Metadata.Val(value) =>
        _buildVal(value)

  //      case Metadata.Arr(value,Cardinality.ZeroOrOne,members) =>
  //        jsArray {
  //          members.headOption.foreach(loop)
  //          members.
  //        }

      case a@Metadata.Arr(value,cardinality,_) =>
        jsObject {
          val hasThis = buildIf {
            jsField("this") {
              _buildVal(value)
            }
          }
          val hasMembers=
            a.indexToMetadata.foldLeft(false) { case (acc,(index, memberMetadata)) =>
              buildIf {
                jsField(index.toString) {
                  loop(memberMetadata)
                }
              } || acc
            }
          hasThis || hasMembers
        }

      case r@Metadata.Rec(value,_) =>
        jsObject {
          val hasThis = buildIf {
            jsField("this") {
              _buildVal(value)
            }
          }
          val hasFields =
            r.fieldToMetadata.foldLeft(false) { case (acc,(field, memberMetadata)) =>
              buildIf {
                jsField(field) {
                  loop(memberMetadata)
                }
              } || acc
            }
          hasThis || hasFields
        }
    }

    buildIfOrElse(
      loop(a)
    ) {
      jsNull()
    }
  }
}


