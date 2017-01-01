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
package s_mach.explain_json

import s_mach.i18n.messages.BoundMessage

/**
 * A base trait for a node in a TypeMetadata tree that
 * represents the JSON schema for a data type
 */
sealed trait JsonExplanationNode

// Note: I would prefer this be named JsonExplanation, but Scala 2.11.7 generates
// odd cyclic dependency error in test module
object JsonExplanationNode {

  /**
   * Base trait for JsonSchema rules
   **/
  sealed trait JsonRule
  object JsonRule {
    case class Maximum(value: BigDecimal, exclusive: Boolean) extends JsonRule
    case class Minimum(value: BigDecimal, exclusive: Boolean) extends JsonRule
    case class StringMaxLength(value: Int) extends JsonRule
    case class StringMinLength(value: Int) extends JsonRule
    case class StringPattern(value: String) extends JsonRule
    // Note: these are handled through cardinality
  //  case class MaxItems(value: Int) extends JsonSchemaRule
  //  case class MinItems(value: Int) extends JsonSchemaRule
  }

  /**
   * Base trait for a JSON type node
   */
  sealed trait JsonType extends JsonExplanationNode {
    def isOptional: Boolean
    def isOptional(value: Boolean) : JsonType
    def additionalRules: List[BoundMessage]
    def additionalRules(value: List[BoundMessage]) : JsonType
    def comments: List[BoundMessage]
    def comments(value: List[BoundMessage]) : JsonType
  }

  /**
   * Base trait for a JSON value node 
   */
  sealed trait JsonVal extends JsonType {
    def rules: List[JsonRule]
  }

  case class JsonBoolean(
    isOptional: Boolean = false,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonVal {
    def rules = Nil

    override def isOptional(value: Boolean) : JsonBoolean =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonBoolean =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonBoolean =
      copy(comments = value)
  }

  case class JsonString(
    isOptional: Boolean = false,
    rules: List[JsonRule] = Nil,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonVal {
    override def isOptional(value: Boolean) : JsonString =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonString =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonString =
      copy(comments = value)
  }

  case class JsonNumber(
    isOptional: Boolean = false,
    rules: List[JsonRule] = Nil,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonVal {
    override def isOptional(value: Boolean) : JsonNumber =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonNumber =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonNumber =
      copy(comments = value)
  }

  case class JsonInteger(
    isOptional: Boolean = false,
    rules: List[JsonRule] = Nil,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonVal {
    override def isOptional(value: Boolean) : JsonInteger =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonInteger =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonInteger =
      copy(comments = value)
  }

  case class JsonArray(
    isOptional: Boolean = false,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonType {
    override def isOptional(value: Boolean) : JsonArray =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonArray =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonArray =
      copy(comments = value)
  }

  case class JsonObject(
    isOptional: Boolean = false,
    additionalRules: List[BoundMessage] = Nil,
    comments: List[BoundMessage] = Nil
  ) extends JsonType {
    override def isOptional(value: Boolean) : JsonObject =
      copy(isOptional = value)
    override def additionalRules(value: List[BoundMessage]) : JsonObject =
      copy(additionalRules = value)
    override def comments(value: List[BoundMessage]) : JsonObject =
      copy(comments = value)
  }

  /**
   * A marker node for Option in a TypeMetadata tree
   * (Option is not emitted in JSON Schema)
   */
  case object OptionMarker extends JsonExplanationNode
}
