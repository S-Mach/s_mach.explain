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
       ;it1i ,,,:::;;;::1tti      s_mach.explain_json
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.explain_json

/**
  * A builder that accepts commands to build a serialized
  * JSON representation
  *
  * Note: not thread safe
  * 
  * @tparam JsonRepr type of JSON representation being built (e.g. String)
  */
trait JsonBuilder[JsonRepr] {
  /**
    * @param value JSON boolean value to append to builder
    */
  def append(value: Boolean) : Unit
  /**
    * @param value JSON number value to append to builder
    */
  def append(value: BigDecimal) : Unit
  /**
    * @param value JSON string value to append to builder
    */
  def append(value: String) : Unit
  /**
    * Append JSON null value to builder
    */
  def append(_null: Null) : Unit

  /**
    * Append a JSON object to the builder.
    * @param buildFields build commands for the fields of the object
    * @tparam A return type
    * @return value returned by build function
    */
  def appendObject[A](buildFields: => A) : A

  /**
    * Append a JSON field to the current object being built
    * by the builder.
    * @param buildFieldValue build commands for value of the field
    * @tparam A return type
    * @return value returned by build function
    */
  def appendField[A](fieldName: String)(buildFieldValue: => A) : A

  /**
    * Append a JSON array to the builder. The members of
    * the array are determined by the commands issued by
    * the build function.
    * @param buildMembers build commands for members of the array
    * @tparam A return type
    * @return value returned by build function
    */
  def appendArray[A](buildMembers: => A) : A

  /** JSON representation of null */
  def lastIsNull : Boolean
  /** JSON representation of an empty object */
  def lastIsEmptyObject : Boolean
  /** JSON representation of an empty array */
  def lastIsEmptyArray : Boolean
  /** JSON representation of an empty string */
  def lastIsEmptyString : Boolean

  def lastIsEmpty : Boolean =
    lastIsNull ||
    lastIsEmptyArray ||
    lastIsEmptyObject ||
    lastIsEmptyString

  type SavedState

  /**
    * @return the current state of the builder
    */
  def save() : SavedState
  /**
    * Restore a previously saved state of the builder
    */
  def restore(prevState: SavedState) : Unit

  /**
    * Builds the final JSON representation
    * @return
    */
  def build() : JsonRepr
}
