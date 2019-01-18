package com.lpiem.ptut_limit_ecran.limitecran

import android.graphics.Color
import android.os.Environment
import android.util.Log
import com.lpiem.melkius.testprocessing.LeafDirection
import com.lpiem.melkius.testprocessing.Node
import processing.core.PApplet
import java.text.SimpleDateFormat
import java.util.*


class Sketch(private var gram: String, private var toSave: Boolean) : PApplet() {

    /*lateinit var manager: SensorManager
    lateinit var sensor: Sensor
    lateinit var listener: SensorListener

    var offset = -1
    var tsteps = 0
    var psteps = 0
    var steps = 0
    var phour = 0
    var stepInc = 0f
    var stepGoal = 10000
    var stepScale: Float = stepGoal / 300.0f

    var branches = ArrayList<Branch>()
    var canvas = PGraphics()

    var bloomColor = color(230, 80, 120, 120)


    override fun setup() {
        //fullScreen()
        noStroke()
        textFont(createFont("SansSerif-Bold", 28 * displayDensity))
        branches.add(Branch())
        initCanvas()
        initCounter()
    }

    override fun draw() {
        background(0)
        val str: String = "${hour()}" + ":" + nfs(minute(), 2) + ":" + nfs(second(), 2) + "\n" +
                tsteps + " steps"
        if (true) {
            growTree()
            if (stepGoal <= steps) clearTree()
            image(canvas, 0f, 0f)
            textAlign(CENTER, BOTTOM)
            textSize(20 * displayDensity)
            fill(0f, 80f)
        } else {
            textAlign(CENTER, CENTER)
            textSize(28 * displayDensity)
            fill(200f, 255f)
            //var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            var rect = Rect(0, 100, 100, 0)
            translate(0f, rect.bottom/2f)
        }
        text(str, 0f, 0f, width.toFloat(), height.toFloat())
    }

    fun growTree() {
        if (true) {
            canvas.beginDraw()
            for (i in 0 until branches.size) {
                val branch = branches[i]
                branch.update()
                branch.display()
                branch.bloom()
            }
            canvas.endDraw()
            stepInc--
        }
    }

    fun updateSteps(value: Int) {
        if (hour() < phour) tsteps = steps;
        if (offset == -1) offset = value;
        steps = value - offset;
        tsteps += steps - psteps;
        stepInc += (steps - psteps) / stepScale;
        psteps = steps
        phour = hour()
    }

    fun clearTree() {
        canvas.beginDraw()
        canvas.background(155f, 211f, 247f)
        canvas.endDraw()
        branches.clear()
        branches.add(Branch())
        offset = -1
        steps = 0
        psteps = 0
        bloomColor = color(random(255f), random(255f), random(255f), 120f)
    }

    fun initCanvas() {
        canvas = createGraphics(width, height)
        canvas.beginDraw()
        canvas.background(155f, 211f, 247f)
        canvas.noStroke()
        canvas.endDraw()
    }

    inner class Branch {
        var position: PVector
        var velocity: PVector
        var diameter: Float = 0f

        constructor() {
            position = PVector(width/2.toFloat(), height.toFloat())
            velocity =  PVector(0f, -1f)
            diameter = width/15.0f
        }

        constructor(parent: Branch) {
            position = parent.position.copy()
            velocity = parent.velocity.copy()
            diameter = parent.diameter / 1.4142f
            parent.diameter = diameter
        }

        fun update() {
            if (1 < diameter) {
                position.add(velocity);
                val opening = map(diameter, 1f, width/15.0f, 1f, 0f)
                val angle = Random.nextDouble((PI - opening * HALF_PI).toDouble(),(TWO_PI + opening * HALF_PI).toDouble()).toFloat()
                val shake = PVector.fromAngle(angle)
                shake.mult(0.1f)
                velocity.add(shake)
                velocity.normalize()
                if (Random.nextFloat() < 0.04f) branches.add(Branch(this))
            }
        }
        fun display() {
            if (1 < diameter) {
                canvas.fill(175f, 108f, 44f, 50f)
                canvas.ellipse(position.x, position.y, diameter, diameter);
            }
        }
        fun bloom() {
            if (0.85 * stepGoal < steps && Random.nextFloat() < 0.001f) {
                val x:Float = position.x + (-10..10).random()
                val y:Float = position.y + (-10..10).random()
                val r:Float = (5..20).random().toFloat()
                canvas.fill(bloomColor)
                canvas.ellipse(x, y, r, r)
            }
        }
    }

    fun initCounter() {
        manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        listener = SensorListener()
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    inner class SensorListener: SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            updateSteps((event?.values!![0]).toInt())
        }

    }
*/
    //private lateinit var saveImage: SaveImage
    //private var toSave = true
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
        size(1200, 1900, P3D)
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

        //smallOffset = (120..150).random().toFloat()
        //bigOffset = (160..200).random().toFloat()
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
                vertex(node.coordX - smallOffset , node.coordY - bigOffset - 10f)
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
