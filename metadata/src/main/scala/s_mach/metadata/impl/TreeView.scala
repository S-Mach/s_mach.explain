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
//package s_mach.metadata
//
//import scala.language.higherKinds
//
///**
// * Base trait for lazy map, flatMap and merge operations of Metadata
// * @tparam A type of value
// */
//sealed trait TreeView[A,M[_]] {
//  def treeOps : TreeOps[M]
//
//  def map[B](f: A => B) : TreeView[B,M]
//  def flatMap[B](f: A => TreeView[B,M]) : TreeView[B,M]
//  def merge(other: M[A])(f: (A,A) => A) : TreeView[A,M]
//
//  def force : M[A]
//}
//
//object TreeView {
//  case class Apply[A,M[_]](
//    base: M[A]
//  )(implicit
//    val treeOps: TreeOps[M]
//  ) extends TreeView[A,M] {
//    def force = base
//
//    def map[B](f: A => B) : TreeView[B,M] =
//      TreeView.Map[A,B,M](this, f)
//
//    def flatMap[B](f: A => TreeView[B,M]) : TreeView[B,M] =
//      TreeView.FlatMap[A,B,M](this ,f)
//
//    def merge(other: M[A])(f: (A,A) => A) : TreeView[A,M] =
//      TreeView.Merge[A,M](this, other, f)
//
//  }
//  case class Map[A,B,M[_]](
//    base: TreeView[A,M],
//    f: A => B
//  )(implicit
//    val treeOps: TreeOps[M]
//  ) extends TreeView[B,M]{
//    def force = treeOps.map(base.force, f)
//
//    def map[C](f2: B => C) =
//      Map(base, f andThen f2)
//
//    def flatMap[C](f2: B => TreeView[C,M]) =
//      FlatMap[A,C,M](base, f andThen f2)
//
//    def merge(other: M[B])(f: (B, B) => B) =
//      Merge[B,M](this, other, f)
//  }
//
//  case class FlatMap[A,B,M[_]](
//    base: TreeView[A,M],
//    f: A => TreeView[B,M]
//  )(implicit
//    val treeOps: TreeOps[M]
//  ) extends TreeView[B,M] {
//    def force = treeOps.flatMap(base.force, f.andThen(_.force))
//
//    def map[C](f2: B => C) =
//      FlatMap(base, f andThen(_.map(f2)))
//
//    def flatMap[C](f2: B => TreeView[C,M]) =
//      FlatMap(base, f andThen(_.flatMap(f2)))
//
//    def merge(other: M[B])(f: (B, B) => B) =
//      Merge[B,M](this, other, f)
//
//  }
//
//  case class Merge[A,M[_]](
//    base: TreeView[A,M],
//    other: M[A],
//    f: (A,A) => A
//  )(implicit
//    val treeOps: TreeOps[M]
//  ) extends TreeView[A,M] {
//    def force = treeOps.merge(base.force, other, f)
//
//    def map[B](f2: A => B) =
//      Map(this, f2)
//
//    def flatMap[B](f2: A => TreeView[B,M]) =
//      FlatMap(this, f2)
//
//    def merge(other: M[A])(f: (A, A) => A) =
//      Merge[A,M](this, other, f)
//  }
//}
//
//
