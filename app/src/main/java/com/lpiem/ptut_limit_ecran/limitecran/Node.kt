package com.lpiem.melkius.testprocessing

import java.util.*

data class Node<T>(val value: T,
                   var coordX: Float,
                   var coordY: Float) {
    /*fun link(left: Node<T>?, right: Node<T>?) = this.apply {
        linkLeft(left).linkRight(right)
    }*/

    //fun linkLeft(left: Node<T>?) = this.apply { leftNode = left }

    //fun linkRight(right: Node<T>?) = this.apply { rightNode = right }

    //fun depth(value: Int) = this.apply { depth = value }

    /**
     * Nodes on the left are in yellow, and those on the right are blue.
     *//*
    override fun toString(): String {
        return StringBuffer().apply {
            append("{${value.toString()} Green ")
            if (leftNode != null)
                append(", ${leftNode.toString()} Yellow ")
            if (rightNode != null)
                append(", ${rightNode.toString()} Blue }")
        }.toString()
    }*/

    /*fun <T> breadthFirstTraversal(root: Node<T>): MutableList<Node<T>> {
        val queue = LinkedList<Node<T>>()
        val traversalList = mutableListOf<Node<T>>()

        // Add first node.
        queue.add(root)

        // Use queue to create breadth first traversal.
        while (queue.isNotEmpty()) {
            val currentNode = queue.poll()
            val depth = currentNode.depth

            // Add left node first.
            if (currentNode.leftNode != null)
                queue.add(currentNode.leftNode!!.depth(depth + 1))

            // Add right node next.
            if (currentNode.rightNode != null)
                queue.add(currentNode.rightNode!!.depth(depth + 1))

            // Add the node to the traversal list.
            traversalList.add(currentNode)
        }

        return traversalList
    }*/
}