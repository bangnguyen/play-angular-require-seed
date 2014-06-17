package utils

import play.api.libs.json.{Json, Reads}
import models.{Profiles}
import play.api.data.validation.ValidationError

/**
 * Created by Marco Chu on 6/14/14.
 */
object CustomValidate {

  def profileEmailNotExisted(implicit r: Reads[String]) =
    Reads.filterNot[String](ValidationError("Email is existed"))(Profiles.findByKeyValue("email", _) != null)

  def profilePhoneNotExisted(implicit r: Reads[String]) =
    Reads.filterNot[String](ValidationError("Phone is existed"))(Profiles.findByKeyValue("phone", _) != null)

  def mustBeNumber(implicit r: Reads[String]) =
    Reads.filterNot[String](ValidationError("Must be number"))(
      Integer.parseInt(_) > 0
    )

}
