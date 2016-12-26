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

import s_mach.metadata.impl.{TypeMetadataOps, MetaTree}

/**
 * Base trait for a node in a tree of type metadata. A tree
 * of type metadata allows associating metadata values with
 * specific fields, array types or values contained in a the
 * serialized output of a type. Commonly used to by validation
 * code to communicate the set of possible checks for a type
 * to callers.
 *
 * @tparam A type of metadata
 */

sealed trait TypeMetadata[+A] extends MetaTree[A,TypeMetadata] {
  type PathNode = TypeMetadata.PathNode

//  def view = TreeView.Apply[A,TypeMetadata](this)(TypeMetadata.treeOps)

}

object TypeMetadata {
//  implicit val treeOps = new TreeOps[TypeMetadata] {
//    def map[A, B](
//      m: TypeMetadata[A],
//      f: A => B
//    ) = m.map(f)
//    def merge[A](
//      lhs: TypeMetadata[A],
//      rhs: TypeMetadata[A],
//      f: (A, A) => A
//    ) = lhs.merge(rhs)(f)
//  }

  /**
   * A base trait for a node in a path. A path node is
   * either a field name in a record or an object that
   * represents all members of an array
   */
  sealed trait PathNode
  object PathNode {

    /**
     * Path node to select a field of a record
     * @param field name of field to select
     */
    case class SelectField(field: String) extends PathNode

    /**
     * Path node to select the members of an array
     * @param cardinality cardinality of the array
     */
    case class SelectMembers(cardinality: Cardinality) extends PathNode
  }

  /**
   * A case class for a sequence of path nodes that point
   * at a specific field in a record, all members of an
   * array or if no path, at "this" (the implied context
   * of the path).
   *
   * Example:
   * case class Name(first: String, last: String)
   * case class Person(id: Int, name: Name, friendIds: Seq[Int])
   *
   * "this"
   * "name.first"
   * "name.last"
   * "friendIds[*]"
   *
   * @param nodes list of nodes
   */
   type Path = List[PathNode]
   object Path {
     val root = List.empty[PathNode]
   }

  /**
   * Case class for metadata about a record
   * @param value metadata for the record
   * @param fields an ordered sequence of type metadata about the record's fields
   * @tparam A type of value
   */
  case class Rec[+A](
    value: A,
    fields: Seq[(String,TypeMetadata[A])]
  ) extends TypeMetadata[A] {
    lazy val fieldToTypeMetadata: Map[String,TypeMetadata[A]] =
      fields.toMap
    lazy val fieldToIndex =
      fields
        .iterator
        .zipWithIndex
        .map { case ((field,tm),i) => field -> i }
        .toMap

    def value[AA >: A](newValue: AA) = Rec(
      value = newValue,
      fields = fields
    )

    def walk(basePath: Path)(callback: (Path, TypeMetadata[A]) => Unit) =
      TypeMetadataOps.Rec_walk(this, basePath, callback)

    def walkStream(basePath: Path) =
      TypeMetadataOps.Rec_walkStream(this, basePath)

    def map[B](f: A => B) = Rec(
      value = f(value),
      fields = fields.map { case (field,tm) => (field,tm.map(f)) }
    )

    def merge[AA >: A](other: TypeMetadata[AA])(f: (AA, AA) => AA) =
      TypeMetadataOps.Rec_merge(this, other, f)
  }

  /**
   * Case class for type metadata about an array of values
   * @param value metadata for the array
   * @param cardinality allowed number of members in the array
   * @param membersTypeMetadata type metadata about the array members
   * @tparam A type of value
   */
  case class Arr[+A](
    value: A,
    cardinality: Cardinality,
    membersTypeMetadata: TypeMetadata[A]
  ) extends TypeMetadata[A] {
    def value[AA >: A](newValue: AA) = copy(value = newValue)

    def walk(basePath: Path)(callback: (Path, TypeMetadata[A]) => Unit) =
      TypeMetadataOps.Arr_walk(this, basePath, callback)

    def walkStream(basePath: Path) =
      TypeMetadataOps.Arr_walkStream(this, basePath)

    def map[B](f: (A) => B) = copy(
      value = f(value),
      membersTypeMetadata = membersTypeMetadata.map(f)
    )

    def merge[AA >: A](other: TypeMetadata[AA])(f: (AA, AA) => AA) =
      TypeMetadataOps.Arr_merge(this, other, f)
  }

  /**
   * Case class for metadata about a value
   * @param value metadata
   * @tparam A type of value
   */
  case class Val[+A](value: A) extends TypeMetadata[A] {
    def value[AA >: A](newValue: AA) = copy(value = newValue)

    def walk(basePath: Path)(callback: (Path, TypeMetadata[A]) => Unit) =
      callback(basePath,this)

    def walkStream(basePath: Path) =
      Stream.cons((basePath, this),Stream.empty)

    def map[B](f: (A) => B) = Val(f(value))

    def merge[AA >: A](other: TypeMetadata[AA])(f: (AA, AA) => AA) =
      TypeMetadataOps.Val_merge(this, other , f)
  }

}

