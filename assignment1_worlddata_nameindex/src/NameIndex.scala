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


  def apply(index: Int): Tree = if (index < 0) new Tree() else nodes(index)

  def apply(name: String):Record = tree.search( new Record(name) )
  def listNames: List[String] = tree.listNames

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

  class Tree(private val _index: Int = -1, private val _record: Record = null, var _leftIndex:Int = -1, var _rightIndex:Int = -1) {
    def record = _record
    def index  = _index

    def left:  Tree = NameIndex(_leftIndex)
    def right: Tree = NameIndex(_rightIndex)
    def left_=(node: Tree) = _leftIndex = node.index
    def right_=(node: Tree) = _rightIndex = node.index

    def leftIndex = _leftIndex
    def rightIndex = _rightIndex
    def isLeaf = record == null

    def insert(node: Tree) {
      if (node.record < record)
        if (left.isLeaf) left = node else left insert node
      else
        if (right.isLeaf) right = node else right insert node
    }

    def search(record: Record, nodesVisited: Int = 1):Record = {
      if (isLeaf) {
        println(s"  ERROR - not a valid country name >> $nodesVisited nodes visited")
        return null
      }

      if (record < _record)      left.search(record, nodesVisited + 1)
      else if (record > _record) right.search(record, nodesVisited + 1)
      else {
        println(f"  ${_record.name} ${record.id}%03d >> $nodesVisited nodes visited")
        _record
      }
    }

    def listNames: List[String] = if (isLeaf) List() else left.listNames ++ (record.name :: right.listNames)

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
