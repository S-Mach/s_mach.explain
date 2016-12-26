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
package s_mach.metadata.impl

import scala.language.higherKinds

/**
 * A base trait to factor out common code between Metadata & TypeMetadata
 * @tparam A metadata type
 * @tparam MDT the most-derived type that inherits from this trait
 */
trait MetaTree[+A,MDT[+AA] <: MetaTree[AA,MDT]] {
  /** A type for the nodes in a path for a node */
  type PathNode

  /** @return metadata value for this node */
  def value: A
  /** @return a copy of this with the new value */
  def value[AA >: A](newValue: AA) : MDT[AA]

  /** @return a Traversable of all values in the tree */
  final def values : Traversable[A] = new Traversable[A] {
    def foreach[U](f: A => U) = walk(Nil) { (_,m) =>
      f(m.value)
      ()
    }
  }

  /** @return a Traversable of all nodes in the tree, where a node is represented
    *         as a (path,node) pair. */
  final def nodes : Traversable[(List[PathNode],MDT[A])] =
    new Traversable[(List[PathNode],MDT[A])] {
      def foreach[U](f: ((List[PathNode], MDT[A])) => U) = walk(Nil) { (path,m) =>
        f((path,m))
        ()
      }
    }

  /** @return a Stream of all values in the tree */
  final def valuesStream : Stream[A] =
    walkStream(Nil).map(_._2.value)

  /** @return a Stream of all nodes in the tree, where a node is represented
    *         as a (path,node) pair. */
  final def nodesStream : Stream[(List[PathNode],MDT[A])] =
    walkStream(Nil)

  /**
   * Map all nodes with new values
   * @param f function to transform values
   * @tparam B type to map to
   * @return a new tree with the mapped values
   */
  def map[B](f: A => B) : MDT[B]

  /**
   * Merge this tree with another tree. This operation can fail if trees are not
   * "mergable" as determined by derived class.
   * @param other tree to merge
   * @param f function to combine metadata values
   * @return a new tree with the combined values
   */
  def merge[AA >: A](other: MDT[AA])(f: (AA,AA) => AA) : MDT[AA]

  /**
   * Walk the tree from root to leafs in an order determined by derived class, passing
   * each (path,node) pair to the callback function.
   * @param basePath the base path to build node paths from
   * @param callback function to call with each (path,node)
   */
  def walk(
    basePath: List[PathNode] = Nil
  )(
    callback: (List[PathNode],MDT[A]) => Unit
  ) : Unit

  /**
   * Walk the tree from root to leafs in an order determined by derived class, passing
   * each (path,node) pair to the callback function.
   * @param basePath the base path to build node paths from
   * @return a Stream of (path,node) pairs
   */
  def walkStream(
    basePath: List[PathNode] = Nil
  ) : Stream[(List[PathNode],MDT[A])]
}
