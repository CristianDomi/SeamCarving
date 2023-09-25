package seamcarving

import seamcarving.ArrayUtils.getColumnsFromBiArray
import seamcarving.ArrayUtils.getSafeRange
import seamcarving.ArrayUtils.lowestValueIndex

fun getMinimalMap(graph: Array<Array<Double>>, orientation: SeamOrientation): Array<Array<Double>> {
    val matrix = if (orientation == SeamOrientation.VERTICAL) { graph } else {
        ArrayUtils.transposeArray(graph)
    }
    val minimalMap = Array(matrix.size) { Array(matrix.first().size) { 0.0 } }
    val lastColumn = getColumnsFromBiArray(matrix, matrix.first().lastIndex)
    for (lastRowIndex in lastColumn.indices) {
        minimalMap[lastRowIndex][matrix.first().lastIndex] = lastColumn[lastRowIndex]
    }
    for (rowIndex in minimalMap.first().lastIndex - 1 downTo 0) {
        val nextRow = rowIndex + 1
        for (columnIndex in minimalMap.lastIndex downTo 0) {
            val adjacentRange = getSafeRange(minimalMap.size, columnIndex)
            var minValue = minimalMap[adjacentRange.first][nextRow]
            for (adjacentIndex in adjacentRange) {
              if (minimalMap[adjacentIndex][nextRow] < minValue) minValue = minimalMap[adjacentIndex][nextRow]
            }
            minimalMap[columnIndex][rowIndex] = minValue + matrix[columnIndex][rowIndex]
        }
    }
    return minimalMap
}

fun shortestPathToBottom(graph: Array<Array<Double>>): List<Node> {
    val nodesToRemove = mutableListOf<Node>()
    var lowestIndex = lowestValueIndex(graph, 0)
    var currentRow = 0
    nodesToRemove.add(Node(lowestIndex, 0))
    while (currentRow <= graph.first().lastIndex - 1) {
        val nextRow = currentRow + 1
        val adjacentRange = getSafeRange(graph.size, lowestIndex)
        var minValue = graph[adjacentRange.first][nextRow]
        var minValueIndex = adjacentRange.first
        for (adjacentIndex in adjacentRange) {
            if (graph[adjacentIndex][nextRow] < minValue) {
                minValue = graph[adjacentIndex][nextRow]
                minValueIndex = adjacentIndex
            }
        }
        nodesToRemove.add(Node(minValueIndex, nextRow))
        currentRow++
        lowestIndex = minValueIndex
    }
    return nodesToRemove
}
