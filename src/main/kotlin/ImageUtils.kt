package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

private const val MAX_RGB_VALUE = 255

object ImageUtils {

    fun toNegativeRGB(color: Color): Color {
        val red = MAX_RGB_VALUE - color.red
        val green = MAX_RGB_VALUE - color.green
        val blue = MAX_RGB_VALUE - color.blue
        return Color(red, green, blue)
    }

    fun getMaxEnergy(bufferedImage: BufferedImage): ImageEnergy {
        var maxEnergy = 0.0
        val energyArray = Array(bufferedImage.width) { Array(bufferedImage.height) { 0.0 } }
        for (row in 0 until bufferedImage.width) {
            for (column in 0 until bufferedImage.height) {

                var xGradient: Double
                var yGradient: Double

                if (getBorderType(row, column, bufferedImage.width, bufferedImage.height) != BorderType.NONE) {
                    val xShiftedPixel =
                        PixelPosition(bufferedImage.width, bufferedImage.height, row, column, PixelAxis.X)
                    val yShiftedPixel =
                        PixelPosition(bufferedImage.width, bufferedImage.height, row, column, PixelAxis.Y)
                    xGradient = getXGradientFromPixelPosition(xShiftedPixel, bufferedImage)
                    yGradient = getYGradientFromPixelPosition(yShiftedPixel, bufferedImage)
                } else {
                    val pixelToUse = PixelPosition(bufferedImage.width, bufferedImage.height, row, column)
                    xGradient = getXGradientFromPixelPosition(pixelToUse, bufferedImage)
                    yGradient = getYGradientFromPixelPosition(pixelToUse, bufferedImage)
                }

                val energy = sqrt(xGradient + yGradient)
                if (energy > maxEnergy) maxEnergy = energy
                energyArray[row][column] = energy
            }
        }
        return ImageEnergy(energyArray, maxEnergy)
    }

    private fun getXGradientFromPixelPosition(pixelPosition: PixelPosition, bufferedImage: BufferedImage): Double {
        return getGradient(
            bufferedImage.getRGB(pixelPosition.xPosition, pixelPosition.yPosition - 1),
            bufferedImage.getRGB(pixelPosition.xPosition, pixelPosition.yPosition + 1)
        )
    }

    private fun getYGradientFromPixelPosition(pixelPosition: PixelPosition, bufferedImage: BufferedImage): Double {
        return getGradient(
            bufferedImage.getRGB(pixelPosition.xPosition - 1, pixelPosition.yPosition),
            bufferedImage.getRGB(pixelPosition.xPosition + 1, pixelPosition.yPosition)
        )
    }

    fun getBorderType(row: Int, column: Int, imageWidth: Int, imageHeight: Int): BorderType {
        return when {
            row == 0 && column == 0 -> BorderType.RIGHT_UPPER_CORNER
            row == 0 && column == imageHeight - 1 -> BorderType.LEFT_UPPER_CORNER
            row == imageWidth - 1 && column == 0 -> BorderType.RIGHT_LOWER_CORNER
            row == imageWidth - 1 && column == imageHeight - 1 -> BorderType.LEFT_LOWER_CORNER
            row == 0 -> BorderType.UPPER_Y
            row == imageWidth - 1 -> BorderType.LOWER_Y
            column == 0 -> BorderType.LEFT_X
            column == imageHeight - 1 -> BorderType.RIGHT_X
            else -> BorderType.NONE
        }
    }

    private fun getGradient(firstRGB: Int, secondRGB: Int): Double {
        val firstPixelRGB = Color(firstRGB)
        val secondPixelRGB = Color(secondRGB)
        return (firstPixelRGB.red - secondPixelRGB.red).toDouble().pow(2) +
                (firstPixelRGB.green - secondPixelRGB.green).toDouble().pow(2) +
                (firstPixelRGB.blue - secondPixelRGB.blue).toDouble().pow(2)
    }


}

enum class BorderType {
    /**
     * (0,0)
     */
    RIGHT_UPPER_CORNER,

    /**
     * (0,MAX)
     */
    LEFT_UPPER_CORNER,

    /**
     * (MAX, 0)
     */
    RIGHT_LOWER_CORNER,

    /**
     * (MAX,MAX)
     */
    LEFT_LOWER_CORNER,

    /**
     * Frontera a la derecha con x.
     * Ej: (1,MAX)
     */
    RIGHT_X,

    /**
     * Frontera a la izquierda con x.
     * Ej: (1,0)
     */
    LEFT_X,

    /**
     * Frontera superior con Y.
     * Ej: (0,1)
     */
    UPPER_Y,

    /**
     * Frontera inferior con Y.
     * Ej: (MAX,1)
     */
    LOWER_Y,

    /**
     * Frontera con nada.
     * Ej: (1,1)
     */
    NONE
}

data class ImageEnergy(
    val arrayEnergy: Array<Array<Double>>,
    val maxEnergy: Double
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEnergy

        if (!arrayEnergy.contentDeepEquals(other.arrayEnergy)) return false
        if (maxEnergy != other.maxEnergy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = arrayEnergy.contentDeepHashCode()
        result = 31 * result + maxEnergy.hashCode()
        return result
    }

}

enum class PixelAxis { X, Y }