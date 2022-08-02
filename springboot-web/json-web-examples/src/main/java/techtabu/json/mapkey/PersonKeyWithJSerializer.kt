package techtabu.json.mapkey

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.io.StringWriter

/**
 * @author TechTabu
 */
class PersonKeyWithJSerializer : JsonSerializer<PersonKeyWithJ?>() {
    private val mapper = ObjectMapper()
    @Throws(IOException::class)
    override fun serialize(
        value: PersonKeyWithJ?,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        val writer = StringWriter()
        mapper.writeValue(writer, value)
        jsonGenerator.writeFieldName(writer.toString())
    }
}