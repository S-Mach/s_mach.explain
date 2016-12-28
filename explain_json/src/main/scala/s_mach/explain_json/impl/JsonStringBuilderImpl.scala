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
//import scala.collection.mutable

/**
  * Efficient JsonBuilder for String. Performs no validation.
  *
  * @param initialBufferSize initial string buffer size
  */
class JsonStringBuilderImpl(initialBufferSize: Int = 256) extends JsonStringBuilder {
  val sb = new StringBuilder(initialBufferSize)
//  val positionStack = mutable.Stack[Int]()
  var lastStartPos = -1

  def append(value: Boolean) = {
    lastStartPos = sb.length
    if(value) {
      sb.append("true,")
    } else {
      sb.append("false,")
    }
    ()
  }

  def append(value: BigDecimal) = {
    lastStartPos = sb.length
    sb.append(value.toString())
    sb.append(',')
    ()
  }

  def append(value: String) = {
    lastStartPos = sb.length
    sb.append(s""""$value",""")
    ()
  }

  def append(n: Null) = {
    lastStartPos = sb.length
    sb.append("null,")
    ()
  }

  def appendArray[A](f: => A) = {
    val pos = sb.length
    sb.append('[')
    val retv = f
    if(pos + 1 < sb.length) {
      // throw away last comma
      sb.setLength(sb.length - 1)
    }
    sb.append("],")
    lastStartPos = pos
    retv
  }

  def appendObject[A](f: => A) = {
    val pos = sb.length
    sb.append('{')
    val retv = f
    if(pos + 1 < sb.length) {
      // throw away last comma
      sb.setLength(sb.length - 1)
    }
    sb.append("},")
    lastStartPos = pos
    retv
  }

  def appendField[A](fieldName: String)(build: => A) = {
    val pos = sb.length
    sb.append(s""""$fieldName":""")
    val retv = build
    lastStartPos = pos
    retv
  }


  private[this] def last = if(lastStartPos > -1) {
    sb.substring(lastStartPos,sb.length - 1)
  } else {
    ""
  }

//  def buildIf(f: => Boolean) = {
//    positionStack.push(sb.length)
//    if(f) {
//      positionStack.pop()
//      true
//    } else {
//      sb.setLength(positionStack.pop())
//      false
//    }
//  }

  /** JSON representation of null */
  def lastIsNull = last == "null"

  /** JSON representation of an empty array */
  def lastIsEmptyArray = last == "[]"

  /** JSON representation of an empty string */
  def lastIsEmptyString = last == "\"\""

  /** JSON representation of an empty object */
  def lastIsEmptyObject = last == "{}"

  //  def undo() = {
//    if(lastStartPos > -1) {
//      sb.setLength(lastStartPos)
//      lastStartPos = -1
//    } else {
//      throw new NoSuchElementException
//    }
//  }

  type SavedState = Int

  def save() = sb.length

  def restore(prevState: Int) = {
    sb.setLength(prevState)
  }

  // throw away extra comma
  def build() = sb.substring(0,sb.length - 1)
}
