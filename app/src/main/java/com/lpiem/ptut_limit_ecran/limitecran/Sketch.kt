package com.lpiem.ptut_limit_ecran.limitecran

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.lpiem.melkius.testprocessing.LeafDirection
import com.lpiem.melkius.testprocessing.Node
import processing.core.PApplet
import java.io.Serializable
import java.util.*


class Sketch(private var gram: String, private var toSave: Boolean) : PApplet(), Serializable {

    //private lateinit var saveImage: SaveImage
    //private var toSave = true
    private var flowerStack: Stack<Node<Char>> = Stack()
    private var smallOffset = 100f
    private var bigOffset = 150f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //saveImage = this
        //orderToSaveImage?.saveIt(saveImage)
    }

//    override fun savePictureToStorage(save: Boolean) {
//        if (save) {
//            //toSave = true
//            //saveThePicture(toSave)
//        }
//    }

    private fun saveThePicture(saveTheImage: Boolean) {
        if (saveTheImage) {
            save(
                Environment.getExternalStorageDirectory().absolutePath
                        + "/LimitEcran/wonder_tree_${(0..10).random()}.png"
            )
        }

    }

    override fun settings() {
        size(1200, 1900, P3D)
        //fullScreen(1)
    }

    override fun setup() {
        frameRate = 1f
    }


    override fun draw() {

        background(Color.WHITE)
        readAndDraw(gram)
        saveThePicture(toSave)
        noLoop()
        //Toast.makeText(activity.applicationContext, "Image Saved !", Toast.LENGTH_SHORT).show()
//        if (toSave) {
//            Handler().post {
//
//            }
//
//        }
    }


    private fun drawNode(node: Node<Char>) {
        beginShape()
        fill(Color.BLUE)
        stroke(Color.BLUE)
        ellipse(node.coordX, node.coordY, 10f, 10f)
        endShape()

    }

    fun readAndDraw(gram: String) {

        val source = Node('S', 620f, 1850f)

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

        while (!flowerStack.empty()) {
            drawFlower(flowerStack)
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
                stack.peek().coordX - bigOffset,
                stack.peek().coordY - bigOffset
            )
            LeafDirection.LEFT -> {
                //flowerStack.push(stack.peek())
                return Node(nodeValue, stack.peek().coordX - smallOffset, stack.peek().coordY - bigOffset)
            }
            LeafDirection.RIGHT -> {
                //flowerStack.push(stack.peek())
                return Node(nodeValue, stack.peek().coordX + smallOffset, stack.peek().coordY - bigOffset)
            }
            LeafDirection.CENTER -> Node(
                nodeValue,
                stack.peek().coordX,
                stack.peek().coordY - bigOffset
            )
        }
    }

    private fun drawLeaf(direction: LeafDirection, node: Node<Char>) {

        smallOffset = (120..150).random().toFloat()
        bigOffset = (160..200).random().toFloat()
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
                //flowerStack.push(Node('f',node.coordX + smallOffset,node.coordY - bigOffset))
            }
            LeafDirection.LEFT -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX + 10, node.coordY + 10)
                vertex(node.coordX - smallOffset + 10, node.coordY - 190)
                vertex(node.coordX - smallOffset, node.coordY - bigOffset + 20f)
                vertex(node.coordX - 10, node.coordY + 10)
                endShape()
                //flowerStack.push(Node('f', node.coordX , node.coordY ))

                //line(node.coordX, node.coordY, node.coordX - 120f, node.coordY - 200f)
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

                //flowerStack.push(Node('f', node.coordX - bigOffset, node.coordY - bigOffset))
                //line(node.coordX, node.coordY, node.coordX, node.coordY - 200f)
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
                //line(node.coordX - bigOffset, node.coordY, node.coordX - bigOffset, node.coordY - bigOffset)
            }
        }

    }

    fun drawFlower(stack: Stack<Node<Char>>) {
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
                stack.peek().coordX + smallOffset + xOff,
                stack.peek().coordY - bigOffset + yOff,
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
