package s_mach.explain_json.example

import java.util.Locale

object ExampleUsage {
  import s_mach.i18n._
  implicit val i18ncfg = I18NConfig(Locale.US)
  import s_mach.explain_json.JsonExplanationNode._
  import s_mach.explain_json._
  import Util._
  import s_mach.metadata._

  // Generator libraries will provide these mappings between built-in types and JSON types
  val jsonExplanation_String : JsonExplanation =
    TypeMetadata.Val(JsonString())

  // A prototypical implicit def generated JSON explanation for Option[String]
  val jsonExplanation_Option_String : JsonExplanation =
    TypeMetadata.Arr(
      OptionMarker,
      Cardinality.ZeroOrOne,
      TypeMetadata.Val(
        jsonExplanation_String.value.asInstanceOf[JsonExplanationNode.JsonType].isOptional(true)
      )
    )

  case class Name(
    first : String,
    middle: Option[String],
    last: String
  )

  // A prototypical macro-generated JSON explanation for Name case class
  val jsonExplanation_Name : JsonExplanation =
    TypeMetadata.Rec(
      JsonObject(),
      Seq(
        "first" -> jsonExplanation_String,
        "middle" -> jsonExplanation_Option_String,
        "last" -> jsonExplanation_String
      )
    )

  // Note: prettyJson is only used here as test dep for demo
  // s_mach.explain_play_json provides actual impl
  jsonExplanation_Name.printJsonSchema("http://test.org").prettyJson

  jsonExplanation_Name.printRemarks.printJson.prettyJson

  jsonExplanation_Name.printRemarks.print
}
