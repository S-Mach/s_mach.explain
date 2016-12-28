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
package s_mach

import s_mach.explain_json.impl._
import s_mach.i18n.I18NConfig
import s_mach.metadata._

package object explain_json extends Implicits {
  type JsonExplanation = TypeMetadata[JsonExplanationNode]

  implicit class S_Mach_Explain_Json_EverythingPML[A](val self: A) extends AnyVal {
    /**
      * Issue build commands to a builder to serialize self to a JSON value
      * @param builder builder to issue comands to
      * @param b type-class for building A
      * @tparam JsonRepr type of JSON value
      * @return TRUE if built JSON value is empty array, empty object or null
      */
    def buildJson[JsonRepr](
      builder: JsonBuilder[JsonRepr]
    )(implicit
      b: BuildJson[A]
    ) : Unit =
      b.build(builder,self)

    def printJson[JsonRepr](implicit
      jbf:JsonBuilderFactory[JsonRepr],
      b: BuildJson[A]
    ) : JsonRepr = {
      val builder = jbf()
      b.build(builder,self)
      builder.build()
    }
  }

  implicit class S_Mach_Explain_Json_TypeMetadataExplainJsonNodePML(val self: JsonExplanation) extends AnyVal {
    /** @return print remarks for JSONExplanation */
    def printRemarks(implicit
      cfg: I18NConfig
    ) : TypeRemarks =
      JsonExplanationOps.toTypeRemarks(self)

    /** @return print JSONSchema for JSONExplanation */
    def printJsonSchema[JsonRepr](
      id: String
    )(implicit
      cfg: I18NConfig,
      jbf: JsonBuilderFactory[JsonRepr]
    ) : JsonRepr = {
      val builder = jbf()
      PrintJsonSchemaOps.printJsonSchema(id, self, builder)
      builder.build()
    }

    /** @return print JSONSchema for JSONExplanation */
    def printJsonSchema[JsonRepr](
      id: String,
      builder: JsonBuilder[JsonRepr]
    )(implicit
      cfg: I18NConfig
    ) : Unit = {
      PrintJsonSchemaOps.printJsonSchema(id, self, builder)
    }
  }
}
