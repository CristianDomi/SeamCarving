package seamcarving

class OutputName(outputName: String) {

    private val name: String
    val extension: String
    val fullName: String

    init {
        val splitOutputName = outputName.split(".")
        name = splitOutputName.first()
        extension = splitOutputName.last()
        fullName = outputName
    }

}