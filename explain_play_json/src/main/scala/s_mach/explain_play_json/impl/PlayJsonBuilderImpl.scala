package s_mach.explain_play_json.impl

import play.api.libs.json._
import s_mach.explain_play_json.PlayJsonBuilder

class PlayJsonBuilderImpl extends PlayJsonBuilder {
  var current : JsValue = JsNull
  var currentField : String = ""
//  var stack = mutable.Stack[(JsValue,String)]()

  private[this] def setVal(js: JsValue) : Unit = {
    current =
      current match {
        case JsObject(fields) =>
          JsObject(fields :+ (currentField -> js))
        case JsArray(members) =>
          JsArray(members :+ js)
        case _ => js
      }
  }

  def append(value: Boolean) = {
    setVal(JsBoolean(value))
  }
  def append(value: BigDecimal) = {
    setVal(JsNumber(value))
  }
  def append(value: String) = {
    setVal(JsString(value))
  }
  def append(value: Int) = {
    setVal(JsNumber(value))
  }
  def append(n: Null) = {
    setVal(JsNull)
  }

  def appendObject[A](build: => A) = {
    val prev = current
    setVal(JsObject(Seq.empty))
    val retv = build
    current = prev
    retv
  }

  def appendArray[A](build: => A) = {
    val prev = current
    setVal(JsArray(Seq.empty))
    val retv = build
    current = prev
    retv
  }

  def appendField[A](fieldName: String)(build: => A) = {
    val prev = currentField
    currentField = fieldName
    val retv = build
    currentField = prev
    retv
  }


//  def buildIf(build: => Boolean) = {
//    stack.push((current,currentField))
//    if(build) {
//      stack.pop()
//      true
//    } else {
//      val (prevCurrent,prevCurrentField) = stack.pop()
//      current = prevCurrent
//      currentField = prevCurrentField
//      false
//    }
//  }

  /** JSON representation of null */
  def lastIsNull = current == JsNull

  /** JSON representation of an empty string */
  def lastIsEmptyString = current match {
    case JsString(value) if value.isEmpty => true
    case _ => false
  }

  /** JSON representation of an empty array */
  def lastIsEmptyArray = current match {
    case JsArray(members) if members.isEmpty => true
    case _ => false
  }

  /** JSON representation of an empty object */
  def lastIsEmptyObject = current match {
    case JsObject(fields) if fields.isEmpty => true
    case _ => false
  }

  type SavedState = JsValue

  def save() = current

  def restore(prevState: SavedState) =
    current = prevState

  def build() = current
}