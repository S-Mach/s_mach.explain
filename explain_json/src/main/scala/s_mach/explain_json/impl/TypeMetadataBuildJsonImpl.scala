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

import s_mach.metadata._
import s_mach.explain_json._

class TypeMetadataBuildJsonImpl[A](implicit
  buildVal: BuildJson[A]
) extends BuildJson[TypeMetadata[A]] {

  def build[JsonRepr](builder: JsonBuilder[JsonRepr], a: TypeMetadata[A]) = {
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

    def loop : TypeMetadata[A] => Unit = {
      case TypeMetadata.Val(value) =>
        _buildVal(value)

      case TypeMetadata.Arr(value,Cardinality.ZeroOrOne,members) =>
        // Note: arrays with cardinality of zero or one are treated
        // as normal fields
        loop(members)

      case TypeMetadata.Arr(value,cardinality,membersTypeMetadata) =>
        pruneIfEmpty {
          appendObject {
            pruneIfEmpty {
              appendField("this") {
                _buildVal(value)
              }
            }
            pruneIfEmpty {
              appendField("*") {
                loop(membersTypeMetadata)
              }
            }
          }
          ()
        }
        ()

      case r@TypeMetadata.Rec(value,_) =>
        appendObject {
          pruneIfEmpty {
            appendField("this") {
              _buildVal(value)
            }
          }
          r.fieldToTypeMetadata.foreach { case (field, fieldTypeMetadata) =>
            pruneIfEmpty {
              appendField(field) {
                loop(fieldTypeMetadata)
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
