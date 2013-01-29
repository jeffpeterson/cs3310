// Author: Jeff Peterson

import scala.io.Source
import com.petersonj.io.Output

object UserInterface {
  val out = Output.toFile("tmp/Log.txt", true)

  log("opened Log FILE")

  def log(thing: Any) = println(">> " + thing)

  def finish = {
    log("closed Log FILE")
    out.close
  }
  
  def println(str: String) {
    out.write(str)
    out.newLine
    out.flush
  }
  

  // note that Source.fromFile returns a BufferedSource and doesn't load the entire file into memory
  // (though for a file this small, it probably does)
  def foreachTransaction = Source.fromFile("docs/TransData.txt")(io.Codec.ISO8859).getLines.drop(1).foreach _
}
