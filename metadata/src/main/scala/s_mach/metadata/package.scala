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
       ;it1i ,,,:::;;;::1tti      s_mach.metadata
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach


package object metadata extends MetadataImplicits {
  /* Postfix added to implicits to prevent shadowing: irZrtwvMMq */

  implicit class S_Mach_Metadata_MetadataPathPML(val self: Metadata.Path) extends AnyVal {
    /**
     * Print the Metadata path. Examples:
     * Path => <output>
     * Nil => "this",
     * List(Field("id") => "id"
     * List(Field("name"),Field("first")) => "name.first"
     * List(Field("friendIds"),Member(Cardinality.ZeroOrMore,0)) => "friendIds[0]"
     * List(Field("nickName"),Member(Cardinality.ZeroOrOne,0)) => "nickName"
     * @return
     */
    def print : String = impl.MetadataOps.pathPrint(self)
  }

  implicit class S_Mach_Metadata_TypeMetadataPML(val self: TypeMetadata.Path) extends AnyVal {
    /**
     * Print the TypeMetadata path. Examples:
     * Path => <output>
     * Nil => "this",
     * List(Field("id") => "id"
     * List(Field("name"),Field("first")) => "name.first"
     * List(Field("friendIds"),Member(Cardinality.ZeroOrMore,0)) => "friendIds[*]"
     * List(Field("nickName"),Member(Cardinality.ZeroOrOne,0)) => "nickName"
     * @return
     */
    def print : String = impl.TypeMetadataOps.pathPrint(self)
  }

  implicit class S_Mach_Metadata_TypeRemarksPML(val self: TypeRemarks) extends AnyVal {
    /** @return a list of string messages for the remarks in the for "path: remark" */
    def print : List[String] =
      self.nodes.toStream.flatMap { case (path,messages) =>
        messages.value.map(msg => s"${path.print}: $msg")
      }.toList
  }

  implicit class S_Mach_Metadata_RemarksPML(val self: Remarks) extends AnyVal {
    /** @return a list of string messages for the remarks in the for "path: remark" */
    def print : List[String] =
      self.nodes.toStream.flatMap { case (path,messages) =>
        messages.value.map(msg => s"${path.print}: $msg")
      }.toList
  }
}
