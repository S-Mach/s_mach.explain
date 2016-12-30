package s_mach.explain_json

import org.scalatest.{FlatSpec, Matchers}

class JsonStringBuilderTest extends FlatSpec with Matchers {
  def mkBuilder() = JsonStringBuilder(1024)

  "JsonStringBuilder.append(Boolean)" should "append a boolean to built JSON" in {
    {
      val builder = mkBuilder()
      builder.append(true)
      builder.build() shouldBe "true"
    }
    {
      val builder = mkBuilder()
      builder.append(false)
      builder.build() shouldBe "false"
    }
  }

  "JsonStringBuilder.append(Int)" should "append a number to built JSON" in {
    val builder = mkBuilder()
    builder.append(123)
    builder.build() shouldBe "123"
  }

  "JsonStringBuilder.append(Long)" should "append a number to built JSON" in {
    val builder = mkBuilder()
    builder.append(123l)
    builder.build() shouldBe "123"
  }

  "JsonStringBuilder.append(BigDecimal)" should "append a number to built JSON" in {
    val builder = mkBuilder()
    builder.append(BigDecimal("123.00"))
    builder.build() shouldBe "123.00"
  }

  "JsonStringBuilder.append(String)" should "append a string to built JSON" in {
    val builder = mkBuilder()
    builder.append("abc123")
    builder.build() shouldBe """"abc123""""
  }

  "JsonStringBuilder.append(null)" should "append a string to built JSON" in {
    val builder = mkBuilder()
    builder.append(null)
    builder.build() shouldBe "null"
  }

  "JsonStringBuilder.appendObject" should "append an empty object to built JSON" in {
    val builder = mkBuilder()
    builder.appendObject {

    }
    builder.build() shouldBe "{}"
  }

  "JsonStringBuilder.appendField" should "append fields contained in an object to built JSON" in {
    val builder = mkBuilder()
    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
      builder.appendField("2") {
        builder.append(123)
      }
    }
    builder.build() shouldBe """{"1":true,"2":123}"""
  }

  "JsonStringBuilder.appendArray" should "append an empty array to built JSON" in {
    val builder = mkBuilder()
    builder.appendArray {

    }
    builder.build() shouldBe "[]"
  }

  "JsonStringBuilder.append/appendArray" should "append values in an array to built JSON" in {
    val builder = mkBuilder()
    builder.appendArray {
      builder.append(true)
      builder.append(123)
      builder.appendObject {

      }
    }
    builder.build() shouldBe "[true,123,{}]"
  }

  "JsonStringBuilder.lastIsNull" should "return TRUE if last appended JSON is null" in {
    val builder = mkBuilder()
    builder.lastIsNull shouldBe false
    builder.append(null)
    builder.lastIsNull shouldBe true
  }

  "JsonStringBuilder.lastIsEmptyString" should "return TRUE if last appended JSON is empty string" in {
    val builder = mkBuilder()
    builder.lastIsEmptyString shouldBe false
    builder.append("")
    builder.lastIsEmptyString shouldBe true
  }

  "JsonStringBuilder.lastIsEmptyObject" should "return TRUE if last appended JSON is an empty object" in {
    val builder = mkBuilder()
    builder.lastIsEmptyObject shouldBe false
    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
    }
    builder.lastIsEmptyObject shouldBe false
    builder.appendObject {

    }
    builder.lastIsEmptyObject shouldBe true
  }

  "JsonStringBuilder.lastIsEmptyArray" should "return TRUE if last appended JSON is an empty array" in {
    val builder = mkBuilder()
    builder.lastIsEmptyArray shouldBe false
    builder.appendArray {
      builder.append(true)
    }
    builder.lastIsEmptyArray shouldBe false
    builder.appendArray {

    }
    builder.lastIsEmptyArray shouldBe true
  }

  "JsonStringBuilder.lastIsEmpty" should "return TRUE if last appended JSON is an empty object, empty array, empty string or null" in {
    val builder = mkBuilder()
    builder.lastIsEmpty shouldBe false
    builder.append(null)
    builder.lastIsEmpty shouldBe true
    builder.append(123)
    builder.lastIsEmpty shouldBe false
    builder.append("")
    builder.lastIsEmpty shouldBe true
    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
    }
    builder.lastIsEmpty shouldBe false
    builder.appendObject {

    }
    builder.lastIsEmpty shouldBe true

    builder.appendObject {
      builder.appendField("1") {
        builder.append("")
      }
      builder.lastIsEmpty shouldBe true
    }
    builder.lastIsEmpty shouldBe false

    builder.appendArray {
      builder.append(true)
    }
    builder.lastIsEmpty shouldBe false
    builder.appendArray {

    }
    builder.lastIsEmpty shouldBe true
  }

  "JsonStringBuilder.save" should "save the current length of the accumulated JSON output" in {
    val builder = mkBuilder()
    builder.append(123)
    builder.save() shouldBe 4

    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
    }
    // "123,{"1":true},"
    //  123456789012345
    builder.save() shouldBe 15

    builder.appendObject {
      builder.save() shouldBe 16
      builder.appendField("2") {
        builder.append(false)
      }
      builder.save() shouldBe 26
    }
    // "123,{"1":true},{"2":false}"
    //  12345678901234567890123456
    builder.save() shouldBe 27

    builder.appendArray {
    }
    // "123,{"1":true},{"2":false},[],"
    //  123456789012345678901234567890
    builder.save() shouldBe 30

    builder.appendArray {
      builder.append("abc")
      builder.append("123")
    }
    // "123,{"1":true},{"2":false},[],["abc","123"],"
    //  12345678901234567890123456789012345678901234
    builder.save() shouldBe 44
  }

  "JsonStringBuilder.restore" should "restore the state of the accumulated JSON output to a previous saved state" in {
    val builder = mkBuilder()
    builder.append(123)
    val saved = builder.save()
    builder.append(true)
    builder.restore(saved)
    builder.build() shouldBe "123"

    val saved2 = builder.save()
    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
    }
    builder.restore(saved2)
    builder.build() shouldBe "123"

    builder.appendObject {
      builder.appendField("1") {
        builder.append(true)
      }
      val saved3 = builder.save()
      builder.appendField("2") {
        builder.append(123)
      }
      builder.restore(saved3)
    }
    builder.build() shouldBe """123,{"1":true}"""

    val saved4 = builder.save()
    builder.appendArray {
    }
    builder.restore(saved4)
    builder.build() shouldBe """123,{"1":true}"""

    builder.appendArray {
      builder.append(true)
      val saved5 = builder.save()
      builder.append(123)
      builder.restore(saved5)
    }
    builder.build() shouldBe """123,{"1":true},[true]"""
  }
}