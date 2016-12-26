package s_mach.explain_play_json

import play.api.libs.json._
import s_mach.explain_json.JsonBuilder

trait PlayJsonBuilder extends JsonBuilder[JsValue]

object PlayJsonBuilder {
  def apply() : PlayJsonBuilder = new impl.PlayJsonBuilderImpl
}