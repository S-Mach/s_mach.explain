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

/**
  * A base trait for serializing to a JSON representation
  * Note: not thread safe
  * 
  * @tparam JsonRepr type of JSON representation
  */
trait JsonBuilder[JsonRepr] {
  // primitives
  def jsBoolean(value: Boolean) : Unit
  def jsNumber(value: BigDecimal) : Unit
  def jsString(value: String) : Unit
  def jsNull() : Unit

  // complex values
  def jsObject[A](build: => A) : A
  def jsField[A](fieldName: String)(build: => A) : A
  def jsArray[A](build: => A) : A

  /**
    * Saves the current state of the builder and then runs the build function.
    * If the build function returns FALSE then the saved state is restored
    * effectively undoing any build commands issued by the build function.
    *
    * @param build invokes build commands. May return FALSE to revert to the builder
    *              state before
    * @return value returned by build function
    */
  def buildIf(build: => Boolean) : Boolean

  def buildIfOrElse(build: => Boolean)(orElse: => Unit) : Boolean =
    if(buildIf(build)) {
      true
    } else {
      orElse
      false
    }

  def build() : JsonRepr
}
