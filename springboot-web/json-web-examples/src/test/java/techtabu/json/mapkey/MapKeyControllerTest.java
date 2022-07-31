package techtabu.json.mapkey;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author TechTabu
 */

@Slf4j
@WebMvcTest
public class MapKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        log.info("Testing testGetAll");
        this.mockMvc.perform(get("/mapkey/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllBySerializer() throws Exception {
        log.info("Testing testGetAllBySerializer");
        this.mockMvc.perform(get("/mapkey/byserializer"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
