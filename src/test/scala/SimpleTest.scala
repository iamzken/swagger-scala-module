import com.wordnik.swagger.converter._

import models._
import models.OrderSize._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }
import com.wordnik.swagger.models.properties._

import scala.collection.JavaConverters._
import scala.annotation.meta.field

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SimpleTest extends FlatSpec with Matchers {
  it should "extract a scala enum" in {
    val converter = ModelConverters.getInstance()

    val schemas = converter.readAll(classOf[SModelWithEnum]).asScala
    val userSchema = schemas("SModelWithEnum")
    val orderSize = userSchema.getProperties().get("orderSize")
    orderSize.isInstanceOf[StringProperty] should be (true)

    val sp = orderSize.asInstanceOf[StringProperty]
    (sp.getEnum().asScala.toSet & Set("TALL", "GRANDE", "VENTI")).size should be (3)
  }

  it should "read a scala case class with properties" in {
    val converter = ModelConverters.getInstance()

    val schemas = converter.readAll(classOf[SimpleUser]).asScala
    val userSchema = schemas("SimpleUser")
    val id = userSchema.getProperties().get("id")
    id.isInstanceOf[LongProperty] should be (true)

    val name = userSchema.getProperties().get("name")
    name.isInstanceOf[StringProperty] should be (true)

    val date = userSchema.getProperties().get("date")
    date.isInstanceOf[DateTimeProperty] should be (true)
    date.getDescription should be ("the birthdate")
  }
}

case class SimpleUser (id: Long, name: String, @(ApiModelProperty @field)(value = "the birthdate") date: java.util.Date)