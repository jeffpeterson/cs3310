import scala.util.matching.Regex

object UserApp {
  def main (args: Array[String]) {

    // load NameIndex from backup file

    // process transactions in Trans file
    processTransactions

    // send output to log file
    // save index to backup file
  }

  def processTransactions {
    val regex = """^([QDIL]N) ?(.*)$""".r
    UserInterface.foreachTransaction { line =>
      regex findFirstIn line match {
        case Some(regex("QN", name))   => NameIndex(name)
        case Some(regex("DN", name))   => NameIndex.delete(name)
        case Some(regex("IN", record)) => NameIndex.insert(new Record(RawData.parseNameFromLine(record)))
        case Some(regex("LN", _))      => UserInterface.println("LN")
        case None                      =>
      }
    }
  }
}
