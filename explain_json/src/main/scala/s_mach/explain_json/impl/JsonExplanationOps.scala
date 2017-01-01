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


import s_mach.i18n._
import s_mach.metadata._
import s_mach.explain_json._
import JsonExplanationNode._
import s_mach.string._
import ExplainJsonMessages._

object JsonExplanationOps {

  // todo: this should go somewhere common prob s_mach.i18n
  def explainCharGroup(cg: CharGroup)(implicit i18ncfg: I18NConfig) : I18NString = {
    import CharGroup._
    cg match {
      case UnicodeLetter => m_unicode_letters()
      case UppercaseLetter => m_uppercase_letters()
      case LowercaseLetter => m_lowercase_letters()
      case Letter => m_letters()
      case WordLetter => m_word_letters()
      case Digit => m_digits()
      case Underscore => m_underscores()
      case Hyphen => m_hyphens()
      case Space => m_spaces()
      case Whitespace => m_whitespace()
    }
  }

  def explainCharGroups(groups: Seq[CharGroup])(implicit i18ncfg: I18NConfig) : I18NString = {
    val printGroups = groups.map(explainCharGroup)

    // todo: i18n should provide this functionality?
    val csmiddle = printGroups.init.mkString(", ")
    val last = if(printGroups.size > 1) s" ${m_or()} ${printGroups.last}" else printGroups.last

    m_must_contain_only_$string(s"$csmiddle$last".asI18N)
  }

  def printJsonType(jsonType: JsonType)(implicit cfg:I18NConfig) : I18NString = {
    jsonType match {
      case _:JsonBoolean => m_boolean()
      case _:JsonString => m_string()
      case _:JsonObject => m_object()
      case _:JsonNumber => m_number()
      case _:JsonInteger => m_integer()
      case _:JsonArray => m_array()
    }
  }

  def printJsonTypeRemark(jsonType: JsonType)(implicit cfg:I18NConfig) : I18NString = {
    m_must_be_$string(printJsonType(jsonType))
  }

  def printJsonRuleRemark(jsonSchemaRule: JsonRule)(implicit cfg:I18NConfig) : I18NString = {
    import JsonRule._
    jsonSchemaRule match {
      case Maximum(value, exclusive) =>
        if(exclusive) {
          m_must_be_less_than_$number(value)
        } else {
          m_must_be_less_than_or_equal_$number(value)
        }
      case Minimum(value, exclusive) =>
        if(exclusive) {
          m_must_be_greater_than_$number(value)
        } else {
          m_must_be_greater_than_or_equal_to_$number(value)
        }
      case StringMaxLength(value) =>
        m_must_not_be_longer_than_$int_characters(value)
      case StringMinLength(value) =>
        value match {
          case 1 =>
            m_must_not_be_empty()
          case n =>
            m_must_have_at_least_$int_characters(n)
        }
      case StringPattern(value) =>
        CharGroupPattern.unapplySeq(value).map(explainCharGroups).getOrElse(
          m_must_match_regex_pattern_$string(I18NString(value))
        )
    }
  }

  def toTypeRemarks(
    tm: JsonExplanation
  )(implicit
    cfg: I18NConfig
  ) : TypeRemarks = {
    tm.map {
      case OptionMarker => Nil
      case jst:JsonType =>
        {
          jst match {
            case _:JsonObject => Nil
            case _ => printJsonTypeRemark(jst) :: Nil
          }
        } ::: {
          if(jst.isOptional) {
            m_optional() :: Nil
          } else {
            Nil
          }
        } ::: {
          jst match {
            case jsv:JsonVal =>
              jsv.rules.map(printJsonRuleRemark)
            case _ => Nil
          }
        } :::
        jst.additionalRules.map(_(cfg)) :::
        jst.comments.map(_(cfg))
    }
  }

}
