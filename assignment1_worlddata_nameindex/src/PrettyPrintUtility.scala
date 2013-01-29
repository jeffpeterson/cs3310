import UserInterface.log
import UserInterface.println

object PrettyPrintUtility {
  def main (args: Array[String]) {
    log("started PrettyPrintUtility")

    println(f"N is ${NameIndex.size}, MaxID is ${Record.maxId}, RootPtr is ${NameIndex.rootIndex}%03d")
    println("[SUB]  - - - Name - - - - - - - - -  DRP  LCh  RCh")

    for (node <- NameIndex.nodes)
      println(f"[${node.index}%03d]  ${node.record.name}%-29.29s ${node.record.id}%03d  ${node.leftIndex}%03d  ${node.rightIndex}%03d")
    println("@ @ @ @ @ @ @ @ @ END OF FILE @ @ @ @ @ @ @ @")

    log("ended PrettyPrintUtility")
    UserInterface.finish
  }
}
