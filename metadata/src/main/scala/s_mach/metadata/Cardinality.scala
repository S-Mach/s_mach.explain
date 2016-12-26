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

/**
 * Enumeration of array cardinalities
 */
sealed trait Cardinality

object Cardinality {
  /** Indicates an array with zero or one members */
  case object ZeroOrOne extends Cardinality
  /** Indicates an array with zero or more members */
  case object ZeroOrMore extends Cardinality
  /** Indicates an array with one or more members */
  case object OneOrMore extends Cardinality

  /**
   * Indicates an array that has at least min members
   * and no more than max members
   * @param min minimum number of members (inclusive)
   * @param max maximum number of members (inclusive)
   */
  case class MinMax(min: Int, max: Int) extends Cardinality
}

