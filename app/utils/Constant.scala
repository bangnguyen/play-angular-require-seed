package utils


object FuncResult {
  val CALL_SUCCESS = 0
  val CALL_FAIL = -1
  val CALL_EXISTED_FAIL = -2
  val CALL_NOT_EXISTED_FAIL = -3
  val CALL_OTHER = -4

}

object Position extends Enumeration {
  type Position = Value
  val Teacher, Student, Management = Value
}

object Level extends Enumeration {
  type Level = Value
  val Beginner, Elem, PostElem, PreInter , Inter , PostInter , Advance= Value
}

object Room extends Enumeration {
  type Room = Value
  val Room1, Room2, Room3, Room4 = Value
}