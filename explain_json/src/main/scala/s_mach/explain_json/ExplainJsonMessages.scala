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

import s_mach.i18n._
import s_mach.i18n.messages._

object ExplainJsonMessages {
  val m_boolean = 'm_boolean.m0
  val m_string = 'm_string.m0
  val m_object = 'm_object.m0
  val m_number = 'm_number.m0
  val m_integer = 'm_integer.m0
  val m_array = 'm_array.m0

  val m_optional = 'm_optional.m0

  val m_unicode_letters = 'm_unicode_letters.literal
  val m_uppercase_letters = 'm_uppercase_letters.literal
  val m_lowercase_letters = 'm_lowercase_letters.literal
  val m_letters = 'm_letters.literal
  val m_word_letters = 'm_word_letters.literal
  val m_digits = 'm_digits.literal
  val m_underscores = 'm_underscores.literal
  val m_hyphens = 'm_hyphens.literal
  val m_spaces = 'm_spaces.literal
  val m_whitespace = 'm_whitespace.literal
  val m_or = 'm_or.literal
  val m_must_contain_only_$string = 'm_must_contain_only_$string.m[I18NString]

  val m_must_be_$string = 'm_must_be_$string.m[I18NString]

  val m_must_be_less_than_$number = 'm_must_be_less_than_$number.m[BigDecimal]
  val m_must_be_less_than_or_equal_$number = 'm_must_be_less_than_or_equal_$number.m[BigDecimal]
  val m_must_be_greater_than_$number = 'm_must_be_greater_than_$number.m[BigDecimal]
  val m_must_be_greater_than_or_equal_to_$number = 'm_must_be_greater_than_or_equal_to_$number.m[BigDecimal]
  
  val m_must_not_be_longer_than_$int_characters = 'm_must_not_be_longer_than_$int_characters.m[Int]
  val m_must_not_be_empty = 'm_must_not_be_empty.m0
  val m_must_have_at_least_$int_characters = 'm_must_have_at_least_$int_characters.m[Int]
  val m_must_match_regex_pattern_$string = 'm_must_match_regex_pattern_$string.m[I18NString]
}
