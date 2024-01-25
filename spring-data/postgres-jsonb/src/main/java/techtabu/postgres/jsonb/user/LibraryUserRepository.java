package techtabu.postgres.jsonb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author TechTabu
 */

@Repository
public interface LibraryUserRepository extends JpaRepository<LibraryUser, UUID> {


    @Query(value = "select l.*\n" +
            "from library_user l\n" +
            "where exists (\n" +
            "    select 1\n" +
            "    from jsonb_array_elements(l.lib_books) books \n" +
            "    where books ->> 'author' =  :author \n" +
            ")", nativeQuery = true)
    List<LibraryUser> findByLibBooksAuthor(@Param("author") String authorName);

    @Query(value = "SELECT l.first_name, l.id, l.last_name, l.lib_books, " +
            "json_build_object('city', l.address -> 'city', 'state', l.address -> 'state', 'zipcode', l.address -> 'zipcode') AS address \n" +
            "FROM library_user l\n" +
            "WHERE l.address ->> 'city' = :city", nativeQuery = true)
    List<LibraryUser> getOnlySelectedFields(@Param("city") String city);
}
