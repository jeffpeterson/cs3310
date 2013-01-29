// Author: Jeff Peterson

import scala.io.Source
import scala.util.matching.Regex

import UserInterface.log
import UserInterface.println

// only used by SetupProgram

object RawData {
  val REGEX = new Regex("""'\w+','([^,]+)'""")

  def foreach(block: String => Unit) = {
    log("opened RawDataTester FILE")
    Source.fromFile("docs/RawDataTester.csv")(io.Codec.ISO8859).getLines.drop(1).foreach { line =>
      block(parseNameFromLine(line))
    }
    log("closed RawDataTester FILE")
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

