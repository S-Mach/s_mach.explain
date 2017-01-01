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

import java.util.Locale
import s_mach.i18n._
import s_mach.i18n.messages._

object TestDI {
  implicit val i18ncfg = I18NConfig(
      UTF8Messages(Locale.US) orElse Messages(
      Locale.US,
      'string_rule1 -> MessageFormat.Literal("string_rule1"),
      'string_rule2 -> MessageFormat.Literal("string_rule2"),
      'string_comment1 -> MessageFormat.Literal("string_comment1"),
      'string_comment2 -> MessageFormat.Literal("string_comment2"),
      'name_type_rule1 -> MessageFormat.Literal("name_type_rule1"),
      'name_type_rule2 -> MessageFormat.Literal("name_type_rule2"),
      'name_type_comment1 -> MessageFormat.Literal("name_type_comment1"),
      'name_type_comment2 -> MessageFormat.Literal("name_type_comment2"),
      'first_field_rule1 -> MessageFormat.Literal("first_field_rule1"),
      'first_field_rule2 -> MessageFormat.Literal("first_field_rule2"),
      'first_field_comment1 -> MessageFormat.Literal("first_field_comment1"),
      'first_field_comment2 -> MessageFormat.Literal("first_field_comment2"),
      'int_rule1 -> MessageFormat.Literal("int_rule1"),
      'int_rule2 -> MessageFormat.Literal("int_rule2"),
      'int_comment1 -> MessageFormat.Literal("int_comment1"),
      'int_comment2 -> MessageFormat.Literal("int_comment2"),
      'person_type_rule1 -> MessageFormat.Literal("person_type_rule1"),
      'person_type_rule2 -> MessageFormat.Literal("person_type_rule2"),
      'person_type_comment1 -> MessageFormat.Literal("person_type_comment1"),
      'person_type_comment2 -> MessageFormat.Literal("person_type_comment2"),
      'name_field_rule1 -> MessageFormat.Literal("name_field_rule1"),
      'name_field_rule2 -> MessageFormat.Literal("name_field_rule2"),
      'name_field_comment1 -> MessageFormat.Literal("name_field_comment1"),
      'name_field_comment2 -> MessageFormat.Literal("name_field_comment2")
    )
  )
  
  val m_string_rule1 = 'string_rule1.literal
  val m_string_rule2 = 'string_rule2.literal
  val m_string_comment1 = 'string_comment1.literal
  val m_string_comment2 = 'string_comment2.literal
  val m_name_type_rule1 = 'name_type_rule1.literal
  val m_name_type_rule2 = 'name_type_rule2.literal
  val m_name_type_comment1 = 'name_type_comment1.literal
  val m_name_type_comment2 = 'name_type_comment2.literal
  val m_first_field_rule1 = 'first_field_rule1.literal
  val m_first_field_rule2 = 'first_field_rule2.literal
  val m_first_field_comment1 = 'first_field_comment1.literal
  val m_first_field_comment2 = 'first_field_comment2.literal
  val m_int_rule1 = 'int_rule1.literal
  val m_int_rule2 = 'int_rule2.literal
  val m_int_comment1 = 'int_comment1.literal
  val m_int_comment2 = 'int_comment2.literal
  val m_person_type_rule1 = 'person_type_rule1.literal
  val m_person_type_rule2 = 'person_type_rule2.literal
  val m_person_type_comment1 = 'person_type_comment1.literal
  val m_person_type_comment2 = 'person_type_comment2.literal
  val m_name_field_rule1 = 'name_field_rule1.literal
  val m_name_field_rule2 = 'name_field_rule2.literal
  val m_name_field_comment1 = 'name_field_comment1.literal
  val m_name_field_comment2 = 'name_field_comment2.literal
}
