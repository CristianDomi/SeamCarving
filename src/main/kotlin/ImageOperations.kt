package seamcarving

import seamcarving.ArrayUtils.nodeListToHashSet
import java.awt.Color
import java.awt.image.BufferedImage

object ImageOperations {
    fun makeNegative(bufferedImage: BufferedImage) {
        for (row in 0 until bufferedImage.width) {
            for (column in 0 until bufferedImage.height) {
                val currentPixelColor = Color(bufferedImage.getRGB(row, column))
                val negativePixelColor = ImageUtils.toNegativeRGB(currentPixelColor)
                bufferedImage.setRGB(row, column, negativePixelColor.rgb)
            }
        }
    }

    fun displayEnergyAsGrayScale(bufferedImage: BufferedImage, imageEnergy: ImageEnergy) {
        for (row in 0 until bufferedImage.width) {
            for (column in 0 until bufferedImage.height) {
                val intensity = (255.0 * imageEnergy.arrayEnergy[row][column] / imageEnergy.maxEnergy).toInt()
                val currentPixelColor = Color(intensity, intensity, intensity)
                bufferedImage.setRGB(row, column, currentPixelColor.rgb)
            }
        }
    }

    fun drawSeam(bufferedImage: BufferedImage, graph: List<Node>, orientation: SeamOrientation) {
        val redColor = Color(255, 0, 0).rgb
        for (node in graph) {
            when (orientation) {
                SeamOrientation.VERTICAL -> bufferedImage.setRGB(node.column, node.row, redColor)
                SeamOrientation.HORIZONTAL -> bufferedImage.setRGB(node.row, node.column, redColor)
            }
        }
    }

    fun resize(originalImage: BufferedImage, seamGraph: List<Node>, orientation: SeamOrientation): BufferedImage {
        val width = with(originalImage.width) { if (orientation == SeamOrientation.VERTICAL) this -1 else this }
        val height =  with(originalImage.height) { if (orientation == SeamOrientation.HORIZONTAL) this -1 else this }
        val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val ignoreMap = nodeListToHashSet(seamGraph, orientation)
        val shiftedRows = HashMap<Int, Int>()
        val shiftedColumns = HashMap<Int, Int>()
        for (column in 0 until newImage.width) {
            var yPixelShift = shiftedColumns.getOrDefault(column, 0)
            for (row in 0 until newImage.height) {
                var xPixelShift = shiftedRows.getOrDefault(row, 0)
                when (orientation) {
                    SeamOrientation.VERTICAL -> {
                        if (ignoreMap.contains("$column - $row")) {
                            xPixelShift = 1
                            shiftedRows[row] = 1
                        }
                    }
                    SeamOrientation.HORIZONTAL -> {
                        if (ignoreMap.contains("$column - $row")) {
                            yPixelShift = 1
                            shiftedColumns[column] = 1
                        }
                    }
                }
                newImage.setRGB(column, row, originalImage.getRGB(column + xPixelShift, row + yPixelShift))
            }
        }
        return newImage
    }

}