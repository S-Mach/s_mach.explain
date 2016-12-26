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

import s_mach.metadata._
import s_mach.metadata.Metadata._

object MetadataOps {
  @inline def pathPrint(self : Path) : String = {
    import self._
    self match {
      case Nil => "this"
      case _ =>
        val sb = new StringBuilder(128)
        def loop : List[PathNode] => Unit = {
          case Nil =>
          case PathNode.SelectField(field) :: PathNode.SelectMember(Cardinality.ZeroOrOne,_) :: tail =>
            sb.append(s"$field.")
            ()
          case PathNode.SelectField(field) :: PathNode.SelectMember(_,index) :: tail =>
            sb.append(s"$field[$index].")
            ()
          case PathNode.SelectField(field) :: tail =>
            sb.append(field).append(".")
            loop(tail)
          case PathNode.SelectMember(_,_) :: tail =>
            loop(tail)
        }
        loop(self.reverse)
        sb.substring(0,sb.size - 1)
    }
  }

  @inline def defaultMerge[A, AA >: A](self: Metadata[A], other: Metadata[AA], f: (AA, AA) => AA) = {
    import self._
    other match {
      case Metadata.Val(otherValue) => value(f(value,otherValue))
      case _ => throw new IllegalTreeMergeException(s"Can't merge $this and $other")
    }
  }

  @inline def Rec_walk[A](self: Rec[A], basePath: Path, callback: (Path, Metadata[A]) => Unit) : Unit = {
    import self._
    callback(basePath, self)
    // Walk fields in index order
    fields.foreach { case (field,metadata) =>
      metadata.walk(
        PathNode.SelectField(field) :: basePath
      )(callback)
    }
  }

  @inline def Rec_walkStream[A](self: Rec[A], basePath: Path) : Stream[(List[PathNode],Metadata[A])]  = {
    import self._
    (basePath, self) #::
    // Stream fields in index order
    fields.toStream.flatMap { case (field,metadata) =>
      metadata.walkStream(
        PathNode.SelectField(field) :: basePath
      )
    }
  }

  @inline def Rec_merge[A,AA >: A](self: Rec[A], other: Metadata[AA], f: (AA, AA) => AA) : Metadata[AA] = {
    def fieldIndexToMetadata[B](
      fields: Seq[(String,Metadata[B])]
    ) : List[((String,Int),Metadata[B])] = {
      fields.iterator.zipWithIndex.map { case ((field,m),i) =>
        (field,i) -> m
      }.toList
    }

    import self._
    other match {
      case Rec(otherValue,otherFields) =>
        val newFields = {
          val fs =
            (fieldIndexToMetadata(fields) ::: fieldIndexToMetadata(otherFields))
              .groupBy(_._1)
              .values
          fs.forall { matchedFields =>
            // Always at least one element in matchedFields
            val (field,index) = matchedFields.head._1
            if (matchedFields.size == 1) {
              throw new IllegalTreeMergeException(s"Missing field $field index $index")
            } else if (matchedFields.size > 2) {
              throw new IllegalTreeMergeException(s"More than two fields with name $field index $index ")
            }
            true
          }
          fs
            .map { case List(f1,f2) =>
              (f1._1,f1._2.merge(f2._2)(f))
            }
            .toSeq
            // sort by index
            .sortBy(_._1._2)
            .map { case ((field,index),m) => field -> m }
        }

        Rec(
          value = f(value,otherValue),
          fields = newFields
        )
      case _ => defaultMerge(self, other, f)
    }
  }

  @inline def Arr_walk[A](self: Arr[A], basePath: Path, callback: (Path, Metadata[A]) => Unit) : Unit = {
    import self._
    callback(basePath, self)
    indexToMetadata.foreach { case (idx,member) =>
      member.walk(
        PathNode.SelectMember(cardinality, idx) :: basePath
      )(callback)
    }
  }

  @inline def Arr_walkStream[A](self: Arr[A], basePath: Path) : Stream[(List[PathNode],Metadata[A])]  = {
    import self._
    (basePath, self) #::
      indexToMetadata.toStream.flatMap { case (idx,member) =>
      member.walkStream(
        PathNode.SelectMember(cardinality, idx) :: basePath
      )
    }
  }

  @inline def Arr_merge[A,AA >: A](self: Arr[A], other: Metadata[AA], f: (AA, AA) => AA) : Metadata[AA] = {
    import self._
    other match {
      case a@Arr(otherValue, otherCardinality, otherMembers)
        if cardinality == otherCardinality  =>
        val newMembers =
          (indexToMetadata.toList ::: a.indexToMetadata.toList)
            .groupBy(_._1)
            .mapValues(
              _.map(_._2).reduce(_.merge(_)(f))
            )
        Arr(
          value = f(value,otherValue),
          cardinality = cardinality,
          members = newMembers.toSeq.sortBy(_._1).map(_._2)
        )
      case _ => defaultMerge(self, other, f)
    }
  }

  @inline def Val_merge[A,AA >: A](self: Val[A], other: Metadata[AA], f: (AA, AA) => AA) : Metadata[AA] = {
    import self._
    other match {
      case Rec(_, _) | Arr(_, _, _) => other.value(f(value, other.value))
      case _ => defaultMerge(self, other, f)
    }
  }
}
