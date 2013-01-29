import UserInterface.log
import UserInterface.println

object SetupProgram {
  def main (args: Array[String]) {

    log("started SetupProgram")

    RawData.foreach { name =>
      NameIndex.insert(new Record(name))
    }

    NameIndex.backup

    log(s"ended SetupProgram - ${NameIndex.size} data items processed")
  }
}
