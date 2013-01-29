import scala.io.Source
import scala.util.matching.Regex

// only used by SetupProgram

object RawData {
  val REGEX = new Regex("""'\w+','([^,]+)'""")

  def foreach(block: String => Unit) = {
    Source.fromFile("docs/RawDataTester.csv")(io.Codec.ISO8859).getLines.drop(1).foreach { line =>
      block(parseNameFromLine(line))
    }
  }

  def parseNameFromLine(line: String) = {
  REGEX findFirstIn line match {
      case Some(REGEX(name)) => name
      case None              => ""
    }
  }
  
}

class RawData(val fields: Map[String, String]) {
  def apply = fields.get(_)
  
}

