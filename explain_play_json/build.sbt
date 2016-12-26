// No 2.12 support from play yet
crossScalaVersions := Seq("2.11.8")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.9",
  "net.s_mach" %% "i18n" % "1.0.1",
  "net.s_mach" %% "codetools-play_json" % "2.0.0"
)
