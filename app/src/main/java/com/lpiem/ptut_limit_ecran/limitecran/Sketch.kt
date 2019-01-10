package com.lpiem.ptut_limit_ecran.limitecran

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.lpiem.melkius.testprocessing.LeafDirection
import com.lpiem.melkius.testprocessing.Node
import processing.core.PApplet
import java.io.Serializable
import java.text.DateFormat
import java.util.*




class Sketch(private var orderToSaveImage: OrderToSaveImage?) : PApplet(), Serializable, SaveImage {

    private lateinit var saveImage: SaveImage
    private var toSave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveImage = this
        orderToSaveImage?.saveIt(saveImage)
    }

    override fun savePictureToStorage(save: Boolean) {
        if (save) {
            toSave = true
        }
    }

    private fun saveThePicture() {
        if (toSave) {
            save(Environment.getExternalStorageDirectory().absolutePath
                    + "/LimitEcran/wonder_tree${DateFormat.getDateInstance()
                .format(DateFormat.HOUR_OF_DAY0_FIELD)}.png")
            //Toast.makeText(context, "Image Saved !", Toast.LENGTH_SHORT).show()
            toSave = false
        }
    }

    override fun settings() {
        size(1000, 2000, P3D)
        //fullScreen(1)
    }

    override fun setup() {

    }


    override fun draw() {

        background(context.resources.getColor(R.color.colorBackground))
        readAndDraw()
        //save(Environment.getExternalStorageDirectory().absolutePath + "/LimitEcran/wonder_tree${DateFormat.getDateInstance().format(DateFormat.HOUR_OF_DAY0_FIELD)}.png")
        //noLoop()
        saveThePicture()
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
            LeafDirection.LEFT -> {

                beginShape()
                var angle = 0
                angle += 5
                val value = (PApplet.cos(PApplet.radians(angle.toFloat())) * 12.0).toFloat()
                var a = 0
                while (a < 360) {
                    val xOff = PApplet.cos(PApplet.radians(a.toFloat())) * value
                    val yOff = PApplet.sin(PApplet.radians(a.toFloat())) * value
                    fill(0 + Random(100).nextInt())
                    ellipse(
                        stack.peek().coordX - 120f + xOff,
                        stack.peek().coordY - 200f + yOff,
                        value + 30,
                        value + 30
                    )
                    a += 75
                }
                fill(125)
                ellipse(20f, 10f, 2f, 2f)
                endShape()

                return Node(nodeValue, stack.peek().coordX - 120f, stack.peek().coordY - 200f)
            }
            LeafDirection.RIGHT -> {
                beginShape()
                var angle = 0
                angle += 5
                val value = (PApplet.cos(PApplet.radians(angle.toFloat())) * 12.0).toFloat()
                var a = 0
                while (a < 360) {
                    val xOff = PApplet.cos(PApplet.radians(a.toFloat())) * value
                    val yOff = PApplet.sin(PApplet.radians(a.toFloat())) * value
                    fill(0 + Random(100).nextInt())
                    ellipse(
                        stack.peek().coordX + 120f + xOff,
                        stack.peek().coordY - 200f + yOff,
                        value + 30,
                        value + 30
                    )
                    a += 75
                }
                fill(125)
                ellipse(20f, 10f, 2f, 2f)
                endShape()

                return Node(nodeValue, stack.peek().coordX + 120f, stack.peek().coordY - 200f)
            }
            LeafDirection.CENTER -> Node(
                nodeValue,
                stack.peek().coordX,
                stack.peek().coordY - 200f
            )
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
                vertex(node.coordX + 120f, node.coordY - 200f, 0f, 0f)
                vertex(node.coordX + 107f, node.coordY - 194f, 0f, 0f)
                endShape()
                //line(node.coordX, node.coordY, node.coordX + 120f, node.coordY - 200f)

            }
            LeafDirection.LEFT -> {
                beginShape()
                noStroke()
                texture(img)
                vertex(node.coordX + 12, node.coordY + 12, 0f, 0f)
                vertex(node.coordX - 12, node.coordY - 12, 0f, 0f)
                vertex(node.coordX - 136f, node.coordY - 206f, 0f, 0f)
                vertex(node.coordX - 124f, node.coordY - 194f, 0f, 0f)
                endShape()
                //line(node.coordX, node.coordY, node.coordX + 120f, node.coordY - 200f)

                //line(node.coordX, node.coordY, node.coordX - 120f, node.coordY - 200f)
            }
            LeafDirection.CENTER -> line(node.coordX, node.coordY, node.coordX, node.coordY - 200f)
            LeafDirection.SOURCE -> {
            }
        }
    }


}
