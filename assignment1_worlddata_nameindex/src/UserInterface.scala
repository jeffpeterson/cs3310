import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter
import scala.io.Source

object UserInterface {
  val file = new File("tmp/Log.txt")
  file.createNewFile

  val out = new BufferedWriter( new FileWriter(file.getAbsoluteFile(), true) )
  
  def println(str: String) {
    Console.println(str)
    out.write(str)
    out.newLine
    out.flush
  }

  // note that Source.fromFile returns a BufferedSource and doesn't load the entire file into memory
  // (though for a file this small, it probably does)
  def foreachTransaction = Source.fromFile("docs/TransData.txt")(io.Codec.ISO8859).getLines.drop(1).foreach _
}
