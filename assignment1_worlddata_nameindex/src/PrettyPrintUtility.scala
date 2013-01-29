// Author: Jeff Peterson

import com.petersonj.io.Output

object PrettyPrintUtility {
  def main (args: Array[String]) {
    val out = Output.toFile("tmp/Log.txt", true)

    out.println(">> started PrettyPrintUtility")

    out.println(f"N is ${NameIndex.size}, MaxID is ${Record.maxId}, RootPtr is ${NameIndex.rootIndex}%03d")
    out.println("[SUB]  - - - Name - - - - - - - - -  DRP  LCh  RCh")

    for (node <- NameIndex.nodes)
      out.println(f"[${node.index}%03d]  ${node.record.name}%-29.29s ${node.record.id}%03d  ${node.leftIndex}%03d  ${node.rightIndex}%03d")
    out.println("@ @ @ @ @ @ @ @ @ END OF FILE @ @ @ @ @ @ @ @")

    out.println(">> ended PrettyPrintUtility")
    out.close
  }
}
