package s_mach.explain_play_json.impl

import play.api.libs.json._
import s_mach.explain_play_json.PlayJsonBuilder
import scala.collection.mutable

class PlayJsonBuilderImpl extends PlayJsonBuilder {
  var current : JsValue = JsNull
  var currentField : String = ""
  var stack = mutable.Stack[(JsValue,String)]()

  private[this] def setVal(js: JsValue) : Unit = {
    current match {
      case o@JsObject(_) =>
        current = o.copy(o.fields :+ (currentField -> js))
      case a@JsArray(_) =>
        current = a.copy(a.value :+ js)
      case _ =>
        current = js
    }
  }

  def jsBoolean(value: Boolean) = {
    setVal(JsBoolean(value))
  }
  def jsNumber(value: BigDecimal) = {
    setVal(JsNumber(value))
  }
  def jsString(value: String) = {
    setVal(JsString(value))
  }
  def jsInteger(value: Int) = {
    setVal(JsNumber(value))
  }
  def jsNull() = {
    setVal(JsNull)
  }

  def jsObject[A](build: => A) = {
    setVal(JsObject(Seq.empty))
    build
  }

  def jsArray[A](build: => A) = {
    setVal(JsArray(Seq.empty))
    build
  }

  def jsField[A](fieldName: String)(build: => A) = {
    currentField = fieldName
    build
  }

  def buildIf(build: => Boolean) = {
    stack.push((current,currentField))
    if(build) {
      stack.pop()
      true
    } else {
      val (prevCurrent,prevCurrentField) = stack.pop()
      current = prevCurrent
      currentField = prevCurrentField
      false
    }
  }

  def build() = current
}