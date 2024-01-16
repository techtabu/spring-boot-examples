package techtabu.postgres.jsonb.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TechTabu
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String title;
    private String author;
    private String libraryNumber;
}
