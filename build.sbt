
lazy val metadata = project.settings(coverageEnabled := true)

lazy val explain_json =
  project
    .dependsOn(metadata).settings(coverageEnabled := true)

lazy val explain_play_json =
  project
    .dependsOn(explain_json).settings(coverageEnabled := true)