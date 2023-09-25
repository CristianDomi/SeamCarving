package seamcarving

import seamcarving.ImageOperations.resize
import seamcarving.ImageUtils.getMaxEnergy
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Se deben proporcionar los siguientes argumentos (Sin "<>").
 *
 * -in <archivo-original.extension> -out <archivo-procesado.extension> -width <ancho> -height <alto>
 */
fun main(args: Array<String>) {

    var inputImageName = ""
    var outputImageName = ""
    var width = 0
    var height = 0

    for (arg in args.indices) {
        when (args[arg]) {
            ArgumentOption.INPUT.command -> {
                inputImageName = args[arg + 1]
            }

            ArgumentOption.OUTPUT.command -> {
                outputImageName = args[arg + 1]
            }

            ArgumentOption.WIDTH.command -> {
                width = args[arg + 1].toInt()
            }

            ArgumentOption.HEIGHT.command -> {
                height = args[arg + 1].toInt()
            }
        }
    }

    var image = ImageIO.read(getFile(inputImageName))

    repeat(width) { image = resizeImage(image, SeamOrientation.VERTICAL) }
    repeat(height) { image = resizeImage(image, SeamOrientation.HORIZONTAL) }

    saveFile(image, outputImageName)
}

fun resizeImage(image: BufferedImage,orientation: SeamOrientation): BufferedImage {
    val imageEnergy = getMaxEnergy(image)
    val map = getMinimalMap(imageEnergy.arrayEnergy, orientation)
    val path = shortestPathToBottom(map)
    return resize(image, path, orientation)
}

fun crossLineRectangle() {
    val rectangleWidth = readInt("Enter rectangle width: ")
    val rectangleHeight = readInt("Enter rectangle height: ")
    val outputName = OutputName(readString("Enter output image name: "))

    val bufferedImage = BufferedImage(rectangleWidth, rectangleHeight, BufferedImage.TYPE_INT_RGB)
    drawBlackRectangleWithRedCrossingLines(bufferedImage)
    ImageIO.write(bufferedImage, outputName.extension, File(outputName.fullName))
}

fun drawBlackRectangleWithRedCrossingLines(bufferedImage: BufferedImage) {
    val graphics = bufferedImage.graphics
    graphics.color = Color.red
    graphics.drawLine(0, 0, bufferedImage.width - 1, bufferedImage.height - 1)
    graphics.drawLine(0, bufferedImage.height - 1, bufferedImage.width - 1, 0)
    graphics.dispose()
}

fun readInt(message: String): Int {
    println(message)
    return readln().toInt()
}

fun readString(message: String): String {
    println(message)
    return readln()
}