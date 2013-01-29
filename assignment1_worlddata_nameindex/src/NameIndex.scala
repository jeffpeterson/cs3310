import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter

import UserInterface.log
import UserInterface.println

object NameIndex {
  private var _rootIndex = -1
  val nodes: ArrayBuffer[Tree] = ArrayBuffer()

  def size = nodes.length
  def maxId = Record.maxId
  def rootIndex = _rootIndex

  loadBackupFile

  def tree: Tree = this(_rootIndex)

  def insert(record: Record) = {
    val index = nodes.length
    nodes append new Tree(index, record)

    if (rootIndex < 0) _rootIndex = index
    else tree.insert( new Tree(index, record) )
  }

  def apply(index: Int): Tree = {
    if (index < 0) null else nodes(index)
  }

  def apply(name: String):Record = if (tree != null) tree.search( new Record(name) ) else null
  def listNames: List[String] = if (tree != null) tree.listNames else null

  def delete(name: String) = {
    this( name )
  }

  def loadBackupFile {
    val file = new File("tmp/NameIndexBackup.txt")
    if (!file.exists) return

    _rootIndex = 0

    log("opened NameIndexBackup FILE")
    for (line <- Source.fromFile(file).getLines) {
      val Array(index, name, id, left, right) = line.split('\t')
      
      nodes.append(new Tree(index.toInt, new Record(name), left.toInt, right.toInt))
    }
    log("closed NameIndexBackup FILE")

  }

  def backup {
    val file = new File("tmp/NameIndexBackup.txt")
    file.createNewFile

    val out = new BufferedWriter( new FileWriter(file.getAbsoluteFile(), false) )

    out.write(nodes.mkString("\n"))
    out.close()
  }


  class Tree(private val _index: Int, private val _record: Record, var _leftIndex:Int = -1, var _rightIndex:Int = -1) {
    def record = _record
    def index  = _index

    def left:  Tree = NameIndex(_leftIndex)
    def right: Tree = NameIndex(_rightIndex)

    def leftIndex = _leftIndex
    def rightIndex = _rightIndex

    def insert(node: Tree) {
      if (node.record < record)
        if (_leftIndex < 0) _leftIndex = node.index
        else left insert node
      else
        if (_rightIndex < 0) _rightIndex = node.index
        else right insert node
    }

    def search(record: Record, nodesVisited: Int = 1):Record = {
      if (record < _record) searchLeft(record, nodesVisited + 1)
      else if (record > _record) searchRight(record, nodesVisited + 1)
      else {
        println(f"  ${_record.name} ${record.id}%03d >> $nodesVisited nodes visited")
        _record
      }
    }

    def searchLeft(record: Record, nodesVisited: Int):Record  = if (_leftIndex < 0) {
      println(s"  ERROR - not a valid country name >> $nodesVisited nodes visited")
      null
    } else left.search(record, nodesVisited)

    def searchRight(record: Record, nodesVisited: Int):Record = if (_rightIndex < 0)  {
      println(s"  ERROR - not a valid country name >> $nodesVisited nodes visited")
      null
    } else right.search(record, nodesVisited)

    def listNames: List[String] =
      (if (leftIndex > 0) left.listNames else List()) ++ (record.name :: (if (rightIndex > 0) right.listNames else List()))
    

    override def toString:String = List(index, record.name, record.id, _leftIndex, _rightIndex ).mkString("\t")
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
  def maxId = currentId

  def nextId: Int = {
    currentId += 1
    currentId
  }
}
