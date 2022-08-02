package techtabu.json.mapkey

import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author TechTabu
 */
@Slf4j
@WebMvcTest
class MapKeyControllerTest {

    private val log = KotlinLogging.logger{}

    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    @Throws(Exception::class)
    fun testGetAll() {
        log.info("Testing testGetAll")
        mockMvc!!.perform(MockMvcRequestBuilders.get("/mapkey/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllBySerializer() {
        log.info("Testing testGetAllBySerializer")
        mockMvc!!.perform(MockMvcRequestBuilders.get("/mapkey/byserializer"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}