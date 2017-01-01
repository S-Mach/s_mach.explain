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
import scala.reflect.macros.blackbox
import s_mach.codetools.IsValueClass
import s_mach.codetools.macros.ProductBuilder
import s_mach.explain_json._
import s_mach.explain_play_json.impl._

/**
 * Type-class to fetch remarks to explain a Play JSON Format
 * */
trait ExplainPlayJson[A] {
  /** @return a general purpose JSON schema that can be converted to
    * human-readable remarks, JSONSchema or other outputs */
  def explain : JsonExplanation
}

object ExplainPlayJson {
  /**
    * Create an ExplainPlayJson type-class for a type that doesn't require
    * i18n configuration.
    *
    * @param explain explanation for the type
    * @tparam A type explained
    * @return ExplainPlayJson[A] type-class
    */
  def apply[A](explain: JsonExplanation) : ExplainPlayJson[A] =
    ExplainPlayJsonOps.ExplainPlayJsonImpl(explain)

  /**
    * A product builder for manually creating an ExplainPlayJson type-class
    * from the fields of the type.
    *
    * @tparam A type explained
    * @return ExplainPlayJson[A] type-class
    */
  def productBuilder[A] : ProductBuilder[ExplainPlayJson,A] =
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

}

