package seamcarving

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun getFile(path: String): File {
    return File("${getPath()}/$path")
}

fun saveFile(image: BufferedImage, fileName: String) {
    ImageIO.write(image, "png", File("${getPath()}/$fileName"))
}

fun getPath() = "${System.getProperty("user.dir")}/src/main/resources/"

