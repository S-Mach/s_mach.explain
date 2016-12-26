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
       ;it1i ,,,:::;;;::1tti      s_mach.data
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.metadata

import org.scalatest.{Matchers, FlatSpec}

import scala.collection.mutable

class MetadataTest extends FlatSpec with Matchers {

  def mkTestTree(f: String => String) =
    Metadata.Rec(
      value = f("invalid"),
      fields = Seq(
        "id" -> Metadata.Val(f("ok")),
        "name" -> Metadata.Rec(
          value = f("invalid"),
          fields = Seq(
            "first" -> Metadata.Val(f("ok")),
            "last" -> Metadata.Val(f("invalid"))
          )
        ),
        "friendIds" -> Metadata.Arr(
          value = f("ok"),
          cardinality = Cardinality.ZeroOrMore,
          members = Seq(
            Metadata.Val(f("ok")),
            Metadata.Val(f("ok")),
            Metadata.Val(f("invalid"))
          )
        )
      )
    )

  def mkTestTreeN(f: String => String) = {
    var i = 0
    mkTestTree { s =>
      val next = s + i
      i = i + 1
      f(next)
    }
  }

  val m = mkTestTreeN(s => s)
  val mExpectedValues = List(
    "invalid0",
    "ok1",
    "invalid2",
    "ok3",
    "invalid4",
    "ok5",
    "ok6",
    "ok7",
    "invalid8"
  )
  val mExpectedNodes = {
    import Metadata.PathNode._
    List(
      (Nil,m),
      (SelectField("id") :: Nil,m.fields(0)._2),
      (SelectField("name") :: Nil,m.fields(1)._2),
      (SelectField("first") :: SelectField("name") :: Nil,m.fields(1)._2.asInstanceOf[Metadata.Rec[String]].fields(0)._2),
      (SelectField("last") :: SelectField("name") :: Nil,m.fields(1)._2.asInstanceOf[Metadata.Rec[String]].fields(1)._2),
      (SelectField("friendIds") :: Nil,m.fields(2)._2),
      (SelectMember(Cardinality.ZeroOrMore, 0) :: SelectField("friendIds") :: Nil,m.fields(2)._2.asInstanceOf[Metadata.Arr[String]].indexToMetadata(0)),
      (SelectMember(Cardinality.ZeroOrMore, 1) :: SelectField("friendIds") :: Nil,m.fields(2)._2.asInstanceOf[Metadata.Arr[String]].indexToMetadata(1)),
      (SelectMember(Cardinality.ZeroOrMore, 2) :: SelectField("friendIds") :: Nil,m.fields(2)._2.asInstanceOf[Metadata.Arr[String]].indexToMetadata(2))
    )
  }

  "Metadata.walk" should "traverse of all nodes in the tree" in {
    val values = mutable.Buffer[(Metadata.Path,Metadata[String])]()
    m.walk(Nil)((path,m) => values += ((path,m)))

    values.toList should be(mExpectedNodes)
  }

  "Metadata.walkStream" should "stream all nodes in the tree" in {
    m.walkStream(Nil).toList should be(mExpectedNodes.toStream)
  }

  "Metadata.values" should "return a Traversable of all values in the tree" in {
    m.values.toList should be(mExpectedValues)
  }

  "Metadata.nodes" should "return a Traversable of all nodes in the tree" in {
    m.nodes.toList should be(mExpectedNodes)
  }

  "Metadata.valuesStream" should "return a Stream of all values in the tree" in {
    m.valuesStream should be(mExpectedValues.toStream)
  }

  "Metadata.nodesStream" should "return a Stream of all nodes in the tree" in {
    m.nodesStream should be(mExpectedNodes.toStream)
  }

  "Metadata.map" should "transform all values in the tree" in {
    m.map(_ + "_").values.toList should be(mExpectedValues.map(_ + "_"))
  }


  "Metadata.merge" should "preserve the tree structure if the two merged trees have the same structure" in {
    val mUnit = m.map(_ => ())
    mUnit.merge(mUnit)((_,_) => ()) should be(mUnit)
  }

  "Metadata.merge" should "preserve the same tree structure" in {
    val m1 = m.map(_ + "_")
    val m2 = m.merge(m1)(_ + _)

    val expected = mkTestTreeN(s => s * 2 + "_")
    m2 should be(expected)
  }

  val rec = m
  val arr = Metadata.Arr(
    value = "ok",
    cardinality = Cardinality.ZeroOrMore,
    members = Seq.empty
  )
  val v1 = Metadata.Val("asdf")
  val v2 = Metadata.Val("other")

  "Metadata.merge" should "throw an exception if tree structure is incompatible" in {
    an [IllegalTreeMergeException] should be thrownBy rec.merge(arr)(_ + _)
    an [IllegalTreeMergeException] should be thrownBy arr.merge(rec)(_ + _)
  }

  "Metadata.merge" should "throw an exception if fields in Rec don't match up" in {
    val idField = "id" -> rec.fieldToMetadata("id")
    val nameField = "name" -> rec.fieldToMetadata("name")
    val friendsIdsField = "friendsIds" -> rec.fieldToMetadata("friendIds")

    val rec2 = rec.copy(
      fields = Seq(
        idField,
        nameField,
        friendsIdsField,
        // Extra field
        "test" -> idField._2
      )
    )
    an [IllegalTreeMergeException] should be thrownBy rec.merge(rec2)(_ + _)
    an [IllegalTreeMergeException] should be thrownBy rec2.merge(rec)(_ + _)
    val rec3 = rec.copy(
      fields = Seq(
        idField,
        nameField
        // Missing field
      )
    )
    an [IllegalTreeMergeException] should be thrownBy rec.merge(rec3)(_ + _)
    an [IllegalTreeMergeException] should be thrownBy rec3.merge(rec)(_ + _)

    val rec4 = rec.copy(
      fields = Seq(
        // Field order swapped
        nameField,
        idField,
        friendsIdsField
      )
    )
    an [IllegalTreeMergeException] should be thrownBy rec.merge(rec4)(_ + _)
    an [IllegalTreeMergeException] should be thrownBy rec4.merge(rec)(_ + _)
  }

  "Metadata.merge" should "always merge Metadata.Val" in {
    rec.merge(v1)(_ + _) should be (rec.value(rec.value + v1.value))
    arr.merge(v1)(_ + _) should be (arr.value(arr.value + v1.value))
    v2.merge(v1)(_ + _) should be (v2.value(v2.value + v1.value))
  }
}
