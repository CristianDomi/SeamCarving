package seamcarving

class PixelPosition(
    imageWidth: Int,
    imageHeight: Int,
    var xPosition: Int = 0,
    var yPosition: Int = 0,
    shiftPosition: PixelAxis? = null,
) {

    private val borderType: BorderType

    init {
        borderType = ImageUtils.getBorderType(xPosition, yPosition, imageWidth, imageHeight)
        when (borderType) {
            BorderType.RIGHT_UPPER_CORNER -> {
                when (shiftPosition) {
                    PixelAxis.X -> yPosition++
                    PixelAxis.Y -> xPosition++
                    null -> {}
                }
            }

            BorderType.LEFT_UPPER_CORNER -> {
                when (shiftPosition) {
                    PixelAxis.X -> yPosition--
                    PixelAxis.Y -> xPosition++
                    null -> {}
                }
            }

            BorderType.RIGHT_LOWER_CORNER -> {
                when (shiftPosition) {
                    PixelAxis.X -> yPosition++
                    PixelAxis.Y -> xPosition--
                    null -> {}
                }
            }

            BorderType.LEFT_LOWER_CORNER -> {
                when (shiftPosition) {
                    PixelAxis.X -> yPosition--
                    PixelAxis.Y -> xPosition--
                    null -> {}
                }
            }

            BorderType.UPPER_Y -> {
                if (shiftPosition == PixelAxis.Y) {
                    xPosition++
                }
            }

            BorderType.LOWER_Y -> {
                if (shiftPosition == PixelAxis.Y) {
                    xPosition--
                }
            }

            BorderType.LEFT_X -> {
                if (shiftPosition == PixelAxis.X) {
                    yPosition++
                }
            }

            BorderType.RIGHT_X -> {
                if (shiftPosition == PixelAxis.X) {
                    yPosition--
                }
            }

            BorderType.NONE -> {}
        }
    }

}