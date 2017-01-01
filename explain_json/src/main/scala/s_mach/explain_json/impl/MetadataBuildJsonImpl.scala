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
import s_mach.metadata.{Cardinality, Metadata}

class MetadataBuildJsonImpl[A](implicit
  buildVal: BuildJson[A]
) extends BuildJson[Metadata[A]] {

  def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: Metadata[A]) = {
    import builder._

    val _buildVal = buildVal.build(builder,_:A)
    def pruneIfEmpty(build: => Unit) : Boolean = {
      val saved = save()
      build
      if(lastIsEmpty) {
        restore(saved)
        true
      } else {
        false
      }
    }

    def loop : Metadata[A] => Unit = {
      case Metadata.Val(value) =>
        _buildVal(value)

      case a@Metadata.Arr(value,Cardinality.ZeroOrOne,_) =>
        a.members.zipWithIndex.foreach { case (memberMetadata,index) =>
          loop(memberMetadata)
        }

      case a@Metadata.Arr(value,cardinality,_) =>
        appendObject {
          pruneIfEmpty {
            appendField("this") {
              _buildVal(value)
            }
          }
          a.members.zipWithIndex.foreach { case (memberMetadata, index) =>
            pruneIfEmpty {
              appendField(index.toString) {
                loop(memberMetadata)
              }
            }
          }
        }

      case r@Metadata.Rec(value,_) =>
        appendObject {
          pruneIfEmpty {
            appendField("this") {
              _buildVal(value)
            }
          }
          r.fields.foreach { case (field, memberMetadata) =>
            pruneIfEmpty {
              appendField(field) {
                loop(memberMetadata)
              }
            }
          }
        }
    }

    if(pruneIfEmpty(loop(a))) {
      append(null)
    }
  }
}


