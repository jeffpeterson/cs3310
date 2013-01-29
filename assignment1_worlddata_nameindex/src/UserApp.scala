import scala.util.matching.Regex
import UserInterface.log
import UserInterface.println

object UserApp {
  def main (args: Array[String]) {
    log("started UserApp")

    processTransactions

    NameIndex.backup

    log(s"ended UserApp - ${} transactions processed")
    UserInterface.finish
  }

  def processTransactions {
    val regex = """^([QDIL]N) *(.*) *$""".r
    log("opened TransData FILE")
    UserInterface.foreachTransaction { line =>
      regex findFirstIn line match {
        case Some(regex("QN", name))   => println(line); NameIndex(name)
        case Some(regex("DN", name))   => println(line); println(" SORRY, DeleteByName not yet oeprational")
        case Some(regex("IN", record)) => println(line); NameIndex.insert(new Record(RawData.parseNameFromLine(record)))
        case Some(regex("LN", _))      => println(line); println("  " + (NameIndex.listNames mkString "\n  "))
        case _                         => return
      }
    }
    log("closed TransData FILE")
  }
}
