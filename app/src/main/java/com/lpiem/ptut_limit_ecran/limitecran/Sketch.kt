package com.lpiem.ptut_limit_ecran.limitecran

import android.graphics.Color
import android.os.Environment
import android.util.Log
import com.lpiem.melkius.testprocessing.LeafDirection
import com.lpiem.melkius.testprocessing.Node
import processing.core.PApplet
import java.io.Serializable
import java.text.DateFormat
import java.util.*


class Sketch(date:Date): PApplet(), Serializable {
    private var creationDate: Date = date
        var TreeDate:Date
        get() = this.creationDate
        set(value){
            this.creationDate = value
        }


    override fun settings() {
        size(1000, 2000)
        //fullScreen(1)
    }

    override fun setup() {

    }

    fun formatDate():String{
        return creationDate.day.toString() +"/"+
                creationDate.day.toString() +"/"+
                creationDate.day.toString()
    }


    override fun draw() {

        background(context.resources.getColor(R.color.colorPrimary))
        readAndDraw()
        save(Environment.getExternalStorageDirectory().absolutePath + "/LimitEcran/wonder_tree${DateFormat.getDateInstance().format(DateFormat.HOUR_OF_DAY0_FIELD)}.png")
        noLoop()
    }


    private fun drawNode(node: Node<Char>) {
        beginShape()
        fill(Color.BLUE)
        stroke(Color.BLUE)
        ellipse(node.coordX, node.coordY, 10f, 10f)
        endShape()

    }

    fun readAndDraw() {
        //val gram = "[L[LR]R[L[R]R[LR]]"
        //val gram = "[L[LR[LR]]R[L[R]R[LR]]]"
        //val gram = "[C[C]]"
        val gram = "[C[L[C[L[L]C[LCR]]C[R]]]R[C[LCR]]]"

        val source = Node('s', 800f, 1900f)

        var currentLeaf = LeafDirection.SOURCE


        val stack: Stack<Node<Char>> = Stack()
        stack.push(source)

        var nodeName = 'a'

        for (j in 0 until gram.length) {
            when {
                gram[j] == LeafDirection.LEFT.direction -> {
                    drawLeaf(LeafDirection.LEFT, stack.peek())
                    currentLeaf = LeafDirection.LEFT
                }
                gram[j] == LeafDirection.RIGHT.direction -> {
                    drawLeaf(LeafDirection.RIGHT, stack.peek())
                    currentLeaf = LeafDirection.RIGHT
                }
                gram[j] == LeafDirection.CENTER.direction -> {
                    drawLeaf(LeafDirection.CENTER, stack.peek())
                    currentLeaf = LeafDirection.CENTER
                }
                gram[j] == '[' -> {
                    val nodeValue = nodeName
                    var newNode: Node<Char>
                    newNode = setNewNode(currentLeaf, nodeValue, stack)
                    stack.push(newNode)
                    drawNode(newNode)
                    nodeName++
                    Log.d("TEST_NODE", "new Node added to stack")

                }
                gram[j] == ']' -> {
                    val popNode = stack.pop()
                    Log.d("TEST_NODE", "node pop : ${popNode.value}")

                }
            }
        }
    }

    private fun setNewNode(
        currentLeaf: LeafDirection,
        nodeValue: Char,
        stack: Stack<Node<Char>>
    ): Node<Char> {
        return when (currentLeaf) {
            LeafDirection.SOURCE -> Node(
                nodeValue,
                stack.peek().coordX - 200f,
                stack.peek().coordY - 200f
            )
            LeafDirection.LEFT -> Node(
                nodeValue,
                stack.peek().coordX - 120f,
                stack.peek().coordY - 200f
            )
            LeafDirection.RIGHT -> Node(
                nodeValue,
                stack.peek().coordX + 120f,
                stack.peek().coordY - 200f
            )
            LeafDirection.CENTER -> Node(
                nodeValue,
                stack.peek().coordX ,
                stack.peek().coordY - 200f)
        }
    }

    private fun drawLeaf(direction: LeafDirection, node: Node<Char>) {
        beginShape()
        fill(Color.YELLOW)
        when (direction) {
            LeafDirection.RIGHT -> line(node.coordX, node.coordY, node.coordX + 120f, node.coordY - 200f)
            LeafDirection.LEFT -> line(node.coordX, node.coordY, node.coordX - 120f, node.coordY - 200f)
            LeafDirection.CENTER -> line(node.coordX, node.coordY, node.coordX , node.coordY - 200f)
            LeafDirection.SOURCE -> { }
        }
    }



}
