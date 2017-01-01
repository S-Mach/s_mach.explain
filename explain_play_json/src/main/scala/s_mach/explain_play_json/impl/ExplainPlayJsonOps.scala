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


import scala.language.higherKinds
import scala.language.existentials
import s_mach.codetools.IsValueClass
import s_mach.metadata._
import s_mach.explain_json._
import s_mach.explain_play_json._

object ExplainPlayJsonOps {
  case class ExplainPlayJsonImpl[A](explanation: JsonExplanation) extends ExplainPlayJson[A] {
    override def explain: JsonExplanation = explanation
  }

  case class DelegatedExplainPlayJson[A](wrapped: ExplainPlayJson[_]) extends ExplainPlayJson[A] {
    override def explain: JsonExplanation =
      wrapped.explain
  }

  /** @return an ExplainPlayJson for a value */
  def forVal[A](jsi: JsonExplanationNode.JsonVal) : ExplainPlayJson[A] =
    ExplainPlayJsonImpl[A](
      TypeMetadata.Val(jsi)
    )

  /** @return an ExplainPlayJson for a value class */
  def forValueClass[V <: IsValueClass[A],A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[V] = DelegatedExplainPlayJson[V](ea)

  /** @return an ExplainPlayJson for a distinct type alias */
  def forDistinctTypeAlias[V <: A,A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[V] = DelegatedExplainPlayJson[V](ea)

  implicit def forOption[A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[Option[A]] = {
    new ExplainPlayJson[Option[A]] {
      override def explain: JsonExplanation = {
        TypeMetadata.Arr(
          // Note: Option[A] in JSON is represented as "optional" A
          // renders should simply throw away this wrapper and mark the
          // the contained jsonSchema as optional
          // Wrapper is required for preserving normal TypeMetadata structure
          JsonExplanationNode.OptionMarker,
          Cardinality.ZeroOrOne,
          ea.explain.value {
            ea.explain.value match {
              case jst:JsonExplanationNode.JsonType =>
                jst.isOptional(true)
              case JsonExplanationNode.OptionMarker =>
                throw new UnsupportedOperationException("Can't make OptionMarker optional")
            }
          }
        )
      }
    }
    //    ExplainPlayJsonImpl[Option[A]] {
//      // Note: Instead of TypeMetadata.Arr like Traversable below, Option is
//      // collapsed here to a copy of the inner with isOptional set true since
//      // in Format.writes optional fields values are not represented as arrays
//      // but instead are omitted if not set and emitted if set
//      ea.jsonSchema.value(ea.jsonSchema.value.copy(isOptional = true))
//    }
  }

  implicit def forTraversable[M[AA] <: Traversable[AA],A](implicit
    ea: ExplainPlayJson[A]
  ) : ExplainPlayJson[M[A]] =
    ExplainPlayJsonImpl[M[A]] {
      TypeMetadata.Arr(
        // Note: Option[A] is represented in JSON by "optional" A
        // the Option is not actually emitted
        JsonExplanationNode.JsonArray(),
        Cardinality.ZeroOrMore,
        ea.explain
      )
    }

}
