object SetupProgram {
  def main (args: Array[String]) {

    RawData.foreach { name =>
      NameIndex.insert(new Record(name))
    }

    NameIndex.backup

    // save NameIndex to Backup file
  }
}
