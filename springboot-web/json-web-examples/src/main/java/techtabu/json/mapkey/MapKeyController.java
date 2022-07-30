package techtabu.json.mapkey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/mapkey")
public class MapKeyController {


    @GetMapping("/")
    public Map<PersonKey, Person> getAllPersons() {

        return Map.of(new PersonKey("Tabu", "Dev"), new Person("Tabu", "tabu@email.com", 45000),
                new PersonKey("Neel", "Tabu"), new Person("Neel", "neel@email.com", 43000));
    }
}
