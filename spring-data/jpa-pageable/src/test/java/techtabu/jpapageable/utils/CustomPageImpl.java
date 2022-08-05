package techtabu.jpapageable.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;

/**
 * @author TechTabu
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomPageImpl<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(@JsonProperty("content") List<T> content,
                          @JsonProperty("pageable") JsonNode pageable,
                          @JsonProperty("number") int page,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }
}
