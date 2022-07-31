package techtabu.json.mapkey;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author TechTabu
 */
public class PersonKeyWithJSerializer extends JsonSerializer<PersonKeyWithJ> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(PersonKeyWithJ value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value.convertToKey());
        jsonGenerator.writeFieldName(writer.toString());
    }
}
