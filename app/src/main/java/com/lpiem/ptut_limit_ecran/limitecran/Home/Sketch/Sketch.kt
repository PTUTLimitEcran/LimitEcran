package com.lpiem.ptut_limit_ecran.limitecran.Home.Sketch

import android.graphics.Color
import android.os.Environment
import com.lpiem.ptut_limit_ecran.limitecran.R
import processing.core.PApplet
import java.text.SimpleDateFormat
import java.util.*


class Sketch(private var gram: String, private var toSave: Boolean) : PApplet() {

    private var flowerStack: Stack<Node<Char>> = Stack()
    private var smallOffset = 80f
    private var bigOffset = 120f


    private fun saveThePicture(saveTheImage: Boolean) {
        if (saveTheImage) {
            val dateFormat = SimpleDateFormat(context.getString(R.string.dateFormatForFile))
            dateFormat.isLenient = false
            val date = Date()
            val dateFormatted = dateFormat.format(date)
            save(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/LimitEcran/wonder_tree$dateFormatted.png"
            )
        }

    }

    override fun settings() {
        size(1200, 1600, P3D)
    }

    override fun setup() {
        frameRate = 1f
    }


    override fun draw() {
        background(Color.WHITE)
        readAndDraw(gram)
        saveThePicture(toSave)
        noLoop()
    }


    private fun drawNode(node: Node<Char>) {
        beginShape()
        fill(Color.parseColor("#8B4F39"))
        stroke(Color.parseColor("#8B4F39"))
        ellipse(node.coordX, node.coordY, 15f, 15f)
        endShape()

    }

    private fun readAndDraw(gram: String) {

        val source = Node('S', 600f, 1620f)
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
                gram[j] == LeafDirection.SOURCE.direction -> {
                    drawLeaf(LeafDirection.SOURCE, stack.peek())
                    currentLeaf = LeafDirection.SOURCE
                }
                gram[j] == LeafDirection.NEW_NODE.direction -> {
                    val nodeValue = nodeName
                    var newNode: Node<Char>
                    newNode = setNewNode(currentLeaf, nodeValue, stack)
                    stack.push(newNode)
                    drawNode(newNode)
                    nodeName++
                }
                gram[j] == LeafDirection.END_NODE.direction -> {
                    val popNode = stack.pop()
                }
            }
        }

        while (!flowerStack.empty()) {
            drawFlower(flowerStack)
        }
    }

    private fun setNewNode(
        currentLeaf: LeafDirection,
        nodeValue: Char,
        stack: Stack<Node<Char>>
    ): Node<Char> {
        if (stack.peek().value != 'S') {
            flowerStack.push(stack.peek())
        }
        return when (currentLeaf) {
            LeafDirection.SOURCE -> Node(
                nodeValue,
                stack.peek().coordX - bigOffset,
                stack.peek().coordY - bigOffset
            )
            LeafDirection.LEFT -> {
                return Node(
                    nodeValue,
                    stack.peek().coordX - smallOffset,
                    stack.peek().coordY - bigOffset
                )
            }
            LeafDirection.RIGHT -> {
                return Node(
                    nodeValue,
                    stack.peek().coordX + smallOffset,
                    stack.peek().coordY - bigOffset
                )
            }
            LeafDirection.CENTER -> Node(
                nodeValue,
                stack.peek().coordX,
                stack.peek().coordY - bigOffset
            )
            else -> Node('O', 0f, 0f)
        }
    }

    private fun drawLeaf(direction: LeafDirection, node: Node<Char>) {

        beginShape()
        val img = loadImage("frontend_large.jpg")
        when (direction) {
            LeafDirection.RIGHT -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX - 6, node.coordY - 6, 0f, 0f)
                vertex(node.coordX + 6, node.coordY + 6, 0f, 0f)
                vertex(node.coordX + smallOffset, node.coordY - bigOffset, 0f, 0f)
                vertex(node.coordX + smallOffset -13f, node.coordY - (bigOffset-6f), 0f, 0f)
                endShape()
            }
            LeafDirection.LEFT -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX + 10, node.coordY + 10)
                vertex(node.coordX - smallOffset , node.coordY - bigOffset - 10f)
                vertex(node.coordX - smallOffset, node.coordY - bigOffset + 20f)
                vertex(node.coordX - 10, node.coordY + 10)
                endShape()
            }
            LeafDirection.CENTER -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX + 5, node.coordY)
                vertex(node.coordX + 5, node.coordY - bigOffset )
                vertex(node.coordX - 5, node.coordY - bigOffset )
                vertex(node.coordX - 5, node.coordY)
                endShape()
            }
            LeafDirection.SOURCE -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX + 20- bigOffset, node.coordY)
                vertex(node.coordX + 5- bigOffset, node.coordY - bigOffset )
                vertex(node.coordX - 5- bigOffset, node.coordY - bigOffset )
                vertex(node.coordX - 20- bigOffset, node.coordY)
                endShape()
            }
            else -> Unit
        }

    }

    private fun drawFlower(stack: Stack<Node<Char>>) {
        beginShape()
        var angle = 0
        angle += 5
        val value = (PApplet.cos(PApplet.radians(angle.toFloat())) * 12.0).toFloat()
        var a = 0
        fill(Color.parseColor("#${(0..9).random()}${(0..9).random()}${(0..9).random()}${(0..9).random()}${(0..9).random()}${(0..9).random()}"))
        while (a < 360) {
            val xOff = PApplet.cos(PApplet.radians(a.toFloat())) * value
            val yOff = PApplet.sin(PApplet.radians(a.toFloat())) * value
            ellipse(
                stack.peek().coordX  + xOff,
                stack.peek().coordY  + yOff,
                value + 30,
                value + 30
            )
            a += 75
        }
        fill(125)
        ellipse(20f, 10f, 2f, 2f)
        endShape()
        stack.pop()
    }

}
