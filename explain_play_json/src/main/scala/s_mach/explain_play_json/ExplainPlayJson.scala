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


import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox
import s_mach.codetools.IsValueClass
import s_mach.codetools.macros.ProductBuilder
import s_mach.explain_json._
import s_mach.explain_play_json.impl._
import s_mach.i18n.I18NConfig

/**
 * Type-class to fetch remarks to explain a Play JSON Format
 * */
trait ExplainPlayJson[A] {
  def explain(implicit i18ncfg: I18NConfig) : JsonExplanation
}

object ExplainPlayJson {
  def apply[A](implicit e: ExplainPlayJson[A]) = e

  def apply[A](f: I18NConfig => JsonExplanation) : ExplainPlayJson[A] =
    ExplainPlayJsonOps.ExplainPlayJsonImpl(f)

  def materialized[A](explain: JsonExplanation) : ExplainPlayJson[A] =
    ExplainPlayJsonOps.MaterializedExplainPlayJson(explain)

  def builder[A] : ProductBuilder[ExplainPlayJson,A] =
    ExplainPlayJsonForProduct[A]()

  /** @return an ExplainPlayJson for a value */
  def forVal[A](jsi: JsonExplanationNode.JsonVal) : ExplainPlayJson[A] =
    ExplainPlayJsonOps.forVal(jsi)

  /** @return an ExplainPlayJson for a value class */
  def forValueClass[V <: IsValueClass[A],A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[V] = ExplainPlayJsonOps.forValueClass(ea)

  /** @return an ExplainPlayJson for a distinct type alias */
  def forDistinctTypeAlias[V <: A,A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[V] = ExplainPlayJsonOps.forDistinctTypeAlias(ea)

  /** @return an ExplainPlayJson for a product */
  def forProductType[A <: Product] : ExplainPlayJson[A] =
    macro macroForProductType[A]

  // Note: Scala requires this to be public
  def macroForProductType[A:c.WeakTypeTag](
    c: blackbox.Context
  ) : c.Expr[ExplainPlayJson[A]] = {
    val builder = new ExplainPlayJsonMacroBuilder(c)
    builder.build[A]().asInstanceOf[c.Expr[ExplainPlayJson[A]]]
  }

  implicit def forOption[A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[Option[A]] = ExplainPlayJsonOps.forOption

  implicit def forTraversable[M[AA] <: Traversable[AA],A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[M[A]] = ExplainPlayJsonOps.forTraversable

}

