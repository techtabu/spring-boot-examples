package techtabu.json.mapkey;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author TechTabu
 */

@RestController
@RequestMapping("/mapkey")
@Slf4j
@CrossOrigin("*")
public class MapKeyController {

    @JsonSerialize(keyUsing = PersonKeyWithJSerializer.class)
    Map<PersonKeyWithJ, Person> map;

    @GetMapping("/")
    public Map<PersonKey, Person> getAllPersons() {

        return Map.of(new PersonKey("Tabu", "Dev"), new Person("Tabu", "tabu@email.com", 45000),
                new PersonKey("Neel", "Tabu"), new Person("Neel", "neel@email.com", 43000));
    }

    @GetMapping("/byserializer")
    public Map<PersonKeyWithJ, Person> getAllPersonsByPersonKeyWithJ() {


        map = Map.of(new PersonKeyWithJ("Shalu", "Tabu"), new Person("Shalu", "shalu@email.com", 45000),
                new PersonKeyWithJ("Inara", "Tabu"), new Person("Inara", "inara@email.com", 43000));
        return map;
    }
}
