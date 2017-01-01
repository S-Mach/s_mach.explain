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
package s_mach.explain_play_json.impl

import scala.reflect.macros.blackbox
import s_mach.codetools.macros.{BlackboxHelper, Result}
import s_mach.explain_play_json.ExplainPlayJson

class ExplainPlayJsonMacroBuilder(
  val c: blackbox.Context,
  override val showDebug : Boolean = false
) extends BlackboxHelper {
  import c.universe._

  def build[A:c.WeakTypeTag]() : c.Expr[ExplainPlayJson[A]] =
    getOrAbort {
      calcProductType(c.weakTypeOf[A]).flatMap(build2)
    }

  case class FieldEx(
    field: ProductType.Field
  ) {
    val index = field.index
    val name = field.name
    val termName = field.termName
    val explainTermName = TermName("explain_" + termName)
    val _type = field._type
  }

  def build2[A:c.WeakTypeTag](
    aProductType: ProductType
  ) : Result[c.Expr[ExplainPlayJson[A]]] = {
    val aType = c.weakTypeOf[A]

    val fields = aProductType.oomField.map(FieldEx.apply)

    val result = c.Expr[ExplainPlayJson[A]] {
      q"""
s_mach.explain_play_json.ExplainPlayJson[$aType] {
  import s_mach.metadata._
  import s_mach.explain_json._
  import s_mach.explain_play_json._

  TypeMetadata.Rec(
    JsonExplanationNode.JsonObject(),
    Seq(..${
      fields.map { field =>
        import field._
        q"""(
              $name,
              implicitly[ExplainPlayJson[${_type}]].explain
            )""".asInstanceOf[c.Tree]
      }
    })
  )
}
       """
    }
    Result(result,Result.Debug(result.toString()))
  }
}
