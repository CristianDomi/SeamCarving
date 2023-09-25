package seamcarving

object ArrayUtils {

    fun getSafeRange(arraySize: Int, previousIndex: Int): IntRange {
        return when (previousIndex) {
            0 -> 0..1
            arraySize - 1 -> previousIndex - 1 until arraySize
            else -> previousIndex - 1..previousIndex + 1
        }
    }

    fun getColumnsFromBiArray(array: Array<Array<Double>>, row: Int): Array<Double> {
        val columns = mutableListOf<Double>()
        for(i in array.indices) {
            columns.add(array[i][row])
        }
        return columns.toTypedArray()
    }

    fun lowestValueIndex(array: Array<Array<Double>>, row: Int): Int {
        var lowestValue = Double.MAX_VALUE
        var lowestValueIndex = Int.MIN_VALUE
        for(i in array.indices) {
            if (array[i][row] < lowestValue) {
                lowestValue = array[i][row]
                lowestValueIndex = i
            }
        }
        return lowestValueIndex
    }

    fun transposeArray(array: Array<Array<Double>>): Array<Array<Double>> {
        val newArray =  Array(array.first().size) { Array(array.size) { 0.0 } }
        for (column in array.indices) {
            for (row in array[column].indices) {
                newArray[row][column] = array[column][row]
            }
        }
        return newArray
    }

      fun nodeListToHashSet(list: List<Node>, orientation: SeamOrientation): HashSet<String> {
          val hashSet = hashSetOf<String>()
          for (node in list) {
             when (orientation) {
                 SeamOrientation.VERTICAL -> hashSet.add("${node.column} - ${node.row}")
                 SeamOrientation.HORIZONTAL -> hashSet.add("${node.row} - ${node.column}")
             }
         }
          return hashSet
    }

}