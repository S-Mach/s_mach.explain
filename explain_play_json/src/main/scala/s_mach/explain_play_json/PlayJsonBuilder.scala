package s_mach.explain_play_json

import play.api.libs.json._
import s_mach.explain_json.JsonBuilder

/**
  * Builder for creating Play JSON instances
  */
trait PlayJsonBuilder extends JsonBuilder[JsValue]

object PlayJsonBuilder {
  def apply() : PlayJsonBuilder = new impl.PlayJsonBuilderImpl
}