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
package s_mach.metadata

import s_mach.metadata.impl.{MetadataOps, MetaTree}

/**
 * Base trait for a node in a tree of metadata. A tree
 * of metadata allows associating metadata values with
 * specific the fields, array members or values contained in
 * a data instance. Commonly returned by validation code to
 * communicate which specific values in a data instance are
 * invalid to callers.
 *
 * @tparam A type of metadata
 */
sealed trait Metadata[+A] extends MetaTree[A,Metadata] {
  type PathNode = Metadata.PathNode

//  def view = TreeView.Apply[A,Metadata](this)(Metadata.treeOps)
}

object Metadata {
//  implicit val treeOps = new TreeOps[Metadata] {
//    def map[A, B](
//      m: Metadata[A],
//      f: A => B
//    ) = m.map(f)
//    def merge[A](
//      lhs: Metadata[A],
//      rhs: Metadata[A],
//      f: (A, A) => A
//    ) = lhs.merge(rhs)(f)
//  }

  /**
   * A base trait for a node in a path. A path node is
   * either a field name in a record or an index into an
   * array
   */
  sealed trait PathNode
  object PathNode {

    /**
     * Path node to select a field of a record
     * @param field name of field to select
     */
    case class SelectField(field: String) extends PathNode

    /**
     * Path node to select a member in an array
     * @param cardinality cardinality of array
     * @param index index of member in array
     */
    case class SelectMember(cardinality: Cardinality, index: Int) extends PathNode
  }

  /**
   * A type alias for a list of path nodes that point
   * at a specific field in a record, a specific member in an
   * array or if no path, at "this" (the implied context
   * of the path).
   */
  type Path = List[PathNode]
  object Path {
    val root = List.empty[PathNode]
  }

  /**
   * Case class for metadata about a record
   * @param value metadata for the record
   * @param fields an ordered sequence of metadata about the
   *               record's fields
   * @tparam A type of value
   */
  case class Rec[+A](
    value: A,
    fields: Seq[(String,Metadata[A])]
  ) extends Metadata[A] {
    lazy val fieldToMetadata : Map[String,Metadata[A]] = 
      fields.toMap
    lazy val fieldToIndex : Map[String,Int] =
      fields
        .iterator
        .zipWithIndex
        .map { case ((field,_),i) => field -> i }
        .toMap

    def value[AA >: A](newValue: AA) = Rec(
      value = newValue,
      fields = fields
    )

    def walk(basePath: Path)(callback: (Path, Metadata[A]) => Unit) =
      MetadataOps.Rec_walk(this, basePath, callback)

    def walkStream(basePath: Path) =
      MetadataOps.Rec_walkStream(this, basePath)

    def map[B](f: A => B) = Rec(
      value = f(value),
      fields = fields.map { case (field,m) => field -> m.map(f) }
    )

    def merge[AA >: A](other: Metadata[AA])(f: (AA, AA) => AA) =
      MetadataOps.Rec_merge[A, AA](this, other, f)
  }
  
  /**
   * Case class for metadata about an array of values
   * @param value metadata for the array
   * @param cardinality allowed number of members in the array
   * @param members an ordered sequence of metadata about the array members
   * @tparam A type of value
   */
  case class Arr[+A](
    value: A,
    cardinality: Cardinality,
    members: Seq[Metadata[A]]
  ) extends Metadata[A] {
    lazy val indexToMetadata : Map[Int,Metadata[A]] =
      members
        .iterator
        .zipWithIndex
        .map { case (m,i) => i -> m }
        .toMap
    
    def value[AA >: A](newValue: AA) = copy(value = newValue)

    def walk(basePath: Path)(callback: (Path, Metadata[A]) => Unit) = 
      MetadataOps.Arr_walk(this,basePath,callback)

    def walkStream(basePath: Path) =
      MetadataOps.Arr_walkStream(this, basePath)
    
    def map[B](f: A => B) = copy(
      value = f(value),
      members = members.map(_.map(f))
    )

    def merge[AA >: A](other: Metadata[AA])(f: (AA, AA) => AA) = 
      MetadataOps.Arr_merge(this, other, f)
  }

  /**
   * Case class for metadata about a value
   * @param value metadata
   * @tparam A type of value
   */
  case class Val[+A](value: A) extends Metadata[A] {
    def value[AA >: A](newValue: AA) = copy(value = newValue)

    def walk(basePath: Path)(callback: (Path, Metadata[A]) => Unit) =
      callback(basePath,this)

    def walkStream(basePath: Path) = Stream.cons((basePath, this),Stream.empty)

    def map[B](f: A => B) = Val(f(value))

    def merge[AA >: A](other: Metadata[AA])(f: (AA, AA) => AA) = 
      MetadataOps.Val_merge(this, other, f)
  }

}

