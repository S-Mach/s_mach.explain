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

trait Implicits {
  implicit object JsonStringBuilderFactory extends JsonBuilderFactory[String] {
    def apply() = JsonStringBuilder(1024)
  }

  implicit object buildJson_ListString extends BuildJson[List[String]] {
    def build(builder: JsonBuilder[_], a: List[String]) = {
      import builder._
      jsArray {
        a.foreach(jsString)
        a.nonEmpty
      }
    }
  }

  implicit def buildJson_Metadata[A](implicit b: BuildJson[A]) =
    new impl.MetadataBuildJsonImpl[A]()

  implicit def buildJson_TypeMetadata[A](implicit b: BuildJson[A]) =
    new impl.TypeMetadataBuildJsonImpl[A]()
}

object Implicits extends Implicits
