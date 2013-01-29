import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.io.File

object NameIndex {
  private var rootIndex = -1
  val nodes: ArrayBuffer[Tree] = ArrayBuffer()

  loadBackupFile

  def tree: Tree = this(rootIndex)

  def insert(record: Record) = {
    val index = nodes.length
    nodes append new Tree(index, record)

    if (rootIndex < 0) rootIndex = index
    else tree.insert( new Tree(index, record) )
    UserInterface.println(record.name)
  }

  def apply(index: Int): Tree = {
    if (index < 0) null else nodes(index)
  }

  def apply(name: String):Record = if (tree != null) tree.search( new Record(name) ) else null

  def delete(name: String) = {
    this( name )
  }

  def loadBackupFile {
    val regex = """""".r
    val file = new File("tmp/NameIndexBackup.txt")
    if (!file.exists) return

    for (line <- Source.fromFile(file).getLines) { line:String =>
      println(line)
    }

  }

  def backup {
    UserInterface.println(nodes.mkString("\n"))
  }


  class Tree(private val _index: Int, private val _record: Record) {
    def record = _record
    def index  = _index

    private var rightIndex: Int = -1
    private var leftIndex:  Int = -1

    def left:  Tree = NameIndex(leftIndex)
    def right: Tree = NameIndex(rightIndex)

    def insert(node: Tree) {
      if (node.record < record)
        if (leftIndex < 0) leftIndex = node.index
        else left insert node
      else
        if (rightIndex < 0) rightIndex = node.index
        else right insert node
    }

    def search(record: Record):Record = {
      if (record < _record) searchLeft(record)
      else if (record > _record) searchRight(record)
      else record
    }

    def searchLeft(record: Record):Record  = if (leftIndex >= 0) left search record else {
      UserInterface.println("ERROR - not a valid country name")
      null
    }
    def searchRight(record: Record):Record = if (rightIndex >= 0) right search record else {
      UserInterface.println("ERROR - not a valid country name")
      null
    }

    override def toString:String = List(index, record.name, record.id, leftIndex, rightIndex ).mkString("\t")
  }
}

class Record(private val _name: String) extends Ordered[Record] {
  private val _id = Record.nextId

  def name = _name
  def id   = _id


  def compare(that: Record) = _name.compareToIgnoreCase(that.name)
  override def toString:String = _name
}

object Record {
  private var currentId = 0

  def nextId: Int = {
    currentId += 1
    currentId
  }
}
