
lazy val metadata = project

lazy val explain_json =
  project
    .dependsOn(metadata)

lazy val explain_play_json =
  project
    .dependsOn(explain_json)