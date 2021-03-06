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


import s_mach.i18n.I18NConfig
import s_mach.metadata._
import s_mach.explain_json._
import JsonExplanationNode._
import s_mach.i18n.messages.BoundMessage
import s_mach.string.CharGroupPattern

object PrintJsonSchemaOps {
  private def isOptionalField(field: (String,JsonExplanation)) : Boolean = {
    field._2.value match {
      case OptionMarker => true
      case _ => false
    }
  }

  def maybeRules[JsonRepr](
    rules: List[JsonRule]
  )(implicit
    builder: JsonBuilder[JsonRepr]
  ) : Unit = {
    import builder._
    import JsonRule._

    rules.foreach {
      case Maximum(value, exclusive) =>
        appendField("maximum") {
          append(value)
        }
        if(exclusive) {
          appendField("exclusiveMaximum") {
            append(true)
          }
        }
      case Minimum(value, exclusive) =>
        appendField("minimum") {
          append(value)
        }
        if(exclusive) {
          appendField("exclusiveMinimum") {
            append(true)
          }
        }
      case StringMaxLength(value) =>
        appendField("maxLength") {
          append(value)
        }
      case StringMinLength(value) =>
        appendField("minLength") {
          append(value)
        }
      case StringPattern(value) =>
        appendField("pattern") {
          append(value)
        }
//        case MaxItems(value) => ???
//        case MinItems(value) => ???
    }
  }

  def maybeAdditionalRules[JsonRepr](
    additionalRules: List[BoundMessage]
  )(implicit
    builder: JsonBuilder[JsonRepr],
    i18nconfig: I18NConfig
  ) : Unit = {
    import builder._

    if (additionalRules.nonEmpty) {
      appendField("additionalRules") {
        appendArray {
          additionalRules.foreach(rule =>
            append(rule())
          )
        }
      }
    }
  }

  def maybeComments[JsonRepr](
    comments: List[BoundMessage]
  )(implicit
    builder: JsonBuilder[JsonRepr],
    i18nconfig: I18NConfig
  ) : Unit = {
    import builder._

    if (comments.nonEmpty) {
      appendField("comments") {
        appendArray {
          comments.foreach { comment =>
            append(comment())
          }
        }
      }
    }
  }

  def commentsForRule(
    rule: JsonRule
  ) : List[BoundMessage] = {
    rule match {
      // Note: only need comments for StringPattern explanation of pattern
      case JsonRule.StringPattern(value) =>
          CharGroupPattern.unapplySeq(value).map { groups =>
            BoundMessage { implicit cfg:I18NConfig =>
              JsonExplanationOps.explainCharGroups(groups)
            }
          }.toList
      case _ => Nil
    }
  }

  def printJsonSchema[JsonRepr](
    id: String,
    head: JsonExplanation,
    builder: JsonBuilder[JsonRepr]
  )(implicit
    cfg: I18NConfig
  ) : Unit = {
    implicit val _builder = builder
    import builder._

    def loop(id: String, tm: JsonExplanation) : Unit = {
      tm match {
        case TypeMetadata.Val(value:JsonVal) =>
          appendField("id") {
            append(id)
          }
          appendField("type") {
            append(JsonExplanationOps.printJsonType(value))
          }
          maybeRules(value.rules)
          maybeAdditionalRules(value.additionalRules)
          maybeComments(
            value.comments ::: value.rules.flatMap(commentsForRule)
          )
        // Note: Option[A] isn't directly represented in JSON
        // Emit nothing for Option and recurse on A
        // JsonType for A will be marked optional
        case TypeMetadata.Arr(_,Cardinality.ZeroOrOne,member) =>
          appendField("id") {
            append(id)
          }
          loop(id,member)
        case TypeMetadata.Arr(value:JsonArray,cardinality,members) =>
          appendField("id") {
            append(id)
          }
          appendField("type") {
            append("array")
          }
          import Cardinality._
          appendField("minItems") {
            append {
              cardinality match {
                case ZeroOrMore => 0
                case ZeroOrOne => 0
                case OneOrMore => 1
                case MinMax(min,_) => min
              }
            }
          }
          cardinality match {
            case ZeroOrMore =>
            case ZeroOrOne =>
              appendField("maxItems") {
                append(1)
              }
            case OneOrMore =>
            case MinMax(_,max) =>
              appendField("maxItems") {
                append(max)
              }
          }
          appendField("uniqueItems") {
            append(false)
          }
          appendField("additionalItems") {
            append(false)
          }
          appendField("items") {
            appendObject {
              loop(s"$id/1", members)
            }
          }
          maybeAdditionalRules(value.additionalRules)
          maybeComments(value.comments)
        case r@TypeMetadata.Rec(value:JsonObject,_) =>
          appendField("id") {
            append(id)
          }
          appendField("type") {
            append("object")
          }
          appendField("properties") {
            appendObject {
              r.fields.foreach { case (field, tm) =>
                appendField(field) {
                  appendObject {
                    loop(s"$id/$field", tm)
                  }
                }
              }
            }
          }
          appendField("additionalProperties") {
            append(true)
          }
          appendField("required") {
            appendArray {
              r.fields.foreach { case t@(f, _) =>
                if (isOptionalField(t) == false) {
                  append(f)
                }
              }
            }
          }
          maybeAdditionalRules(value.additionalRules)
          maybeComments(value.comments)
        case unsupported =>
          throw new UnsupportedOperationException(s"Unsupported TypeMetadata: $unsupported")
      }
    }

    appendObject {
      appendField("$schema") {
        append("http://json-schema.org/draft-04/schema#")
      }
      loop(id, head)
    }
  }
}
