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
package s_mach

import s_mach.i18n.I18NConfig

package object explain_play_json extends ExplainPlayJsonImplicits {

  /*
  Note: putting this in ExplainPlayJsonImplicits result in error:
[error] /Users/lancegatlin/Code/s_mach.explain_play_json/src/test/scala/s_mach/explain_play_json/ExampleUsage.scala:45: reference to explainPlayJson is ambiguous;
[error] it is both defined in trait ExplainPlayJsonImplicits and imported subsequently by
[error] import s_mach.explain_play_json._
[error]   explainPlayJson[Person].explain.printJsonSchema[JsValue]("http://test.org").pretty
[error]   ^
   */
  def explainPlayJson[A](implicit
    e: ExplainPlayJson[A],
    i18ncfg: I18NConfig
  ) = e.explain
}
