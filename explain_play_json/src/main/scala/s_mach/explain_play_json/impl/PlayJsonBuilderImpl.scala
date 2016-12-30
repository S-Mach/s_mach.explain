package s_mach.explain_play_json.impl

import play.api.libs.json._
import s_mach.explain_json.JsonStringBuilder
import s_mach.explain_play_json.PlayJsonBuilder

class PlayJsonBuilderImpl extends PlayJsonBuilder {
  // Note: using immutable JsValues directly to construct JSON
  // graph is both clumsy and inefficient. Instead using String
  // to quickly accumulate JSON then Json.parse to convert String
  // JSON to JsValue graph. Also greatly simplifies save/restore
  val jsb = JsonStringBuilder(1024)

  def append(value: Boolean) = jsb.append(value)

  def append(value: Int) = jsb.append(value)

  def append(value: Long) = jsb.append(value)

  def append(value: BigDecimal) = jsb.append(value)

  def append(value: String) = jsb.append(value)

  def append(_null: Null) = jsb.append(_null)

  def appendObject[A](buildFields: =>A) = jsb.appendObject(buildFields)

  def appendField[A](fieldName: String)(buildFieldValue: => A) =
    jsb.appendField(fieldName)(buildFieldValue)

  def appendArray[A](buildMembers: =>A) =
    jsb.appendArray(buildMembers)

  def lastIsNull = jsb.lastIsNull

  def lastIsEmptyArray = jsb.lastIsEmptyArray

  def lastIsEmptyString = jsb.lastIsEmptyString

  def lastIsEmptyObject = jsb.lastIsEmptyObject

  type SavedState = jsb.SavedState

  def save() = jsb.save()

  def restore(prevState: SavedState) = jsb.restore(prevState)

  def build() = Json.parse(jsb.build())
}