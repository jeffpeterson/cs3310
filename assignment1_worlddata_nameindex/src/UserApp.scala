// Author: Jeff Peterson

import scala.util.matching.Regex
import UserInterface.log
import UserInterface.println

object UserApp {
  var transactionsProcessed = 0

  def main (args: Array[String]) {
    log("started UserApp")

    processTransactions

    NameIndex.finish

    log(s"ended UserApp - $transactionsProcessed transactions processed")
    UserInterface.finish
  }

  def processTransactions {
    val regex = """^([QDIL]N) *(.*) *$""".r
    log("opened TransData FILE")
    UserInterface.foreachTransaction { line =>
      transactionsProcessed += 1
      regex findFirstIn line match {
        case Some(regex("QN", name))   => println(line); NameIndex.queryByName(name)
        case Some(regex("DN", name))   => println(line); NameIndex.deleteByName(name)
        case Some(regex("IN", record)) => println(line); NameIndex.insert(new Record(RawData.parseNameFromLine(record)))
        case Some(regex("LN", _))      => println(line); println("  " + (NameIndex.listNames mkString "\n  "))
        case _                         => return
      }
    }

    log("closed TransData FILE")
  }
}
