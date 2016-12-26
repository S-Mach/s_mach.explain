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

import s_mach.explain_json.JsonStringBuilder
import scala.collection.mutable

/**
  * Efficient JsonBuilder for String. Performs no validation.
  *
  * @param initialBufferSize initial string buffer size
  */
class JsonStringBuilderImpl(initialBufferSize: Int = 256) extends JsonStringBuilder {
  val sb = new StringBuilder(initialBufferSize)
  val positionStack = mutable.Stack[Int]()

  def jsBoolean(value: Boolean) = {
    if(value) {
      sb.append("true,")
    } else {
      sb.append("false,")
    }
    ()
  }

  def jsNumber(value: BigDecimal) = {
    sb.append(value.toString())
    sb.append(',')
    ()
  }

  def jsString(value: String) = {
    sb.append(s""""$value",""")
    ()
  }

  def jsNull() = {
    sb.append("null,")
    ()
  }

  def jsArray[A](f: => A) = {
    sb.append('[')
    val retv = f
    // throw away last comma
    sb.setLength(sb.length - 1)
    sb.append("],")
    retv
  }

  def jsObject[A](f: => A) = {
    sb.append('{')
    val retv = f
    // throw away last comma
    sb.setLength(sb.length - 1)
    sb.append("},")
    retv
  }

  def jsField[A](fieldName: String)(build: => A) = {
    sb.append(s""""$fieldName":""")
    build
  }


  def buildIf(f: => Boolean) = {
    positionStack.push(sb.length)
    if(f) {
      positionStack.pop()
      true
    } else {
      sb.setLength(positionStack.pop())
      false
    }
  }

  // throw away extra comma
  def build() = sb.substring(0,sb.length - 1)
}
