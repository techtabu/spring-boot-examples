package techtabu.json.mapkey

import lombok.Builder
import lombok.Data

/**
 * @author TechTabu
 */
@Data
@Builder
class Person(firstName: String, email: String, salary: Int) {
    var firstName: String? = firstName
    var email: String? = email
    var salary = salary
}