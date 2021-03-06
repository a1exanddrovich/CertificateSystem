package com.epam.esm.dao;

import com.epam.esm.entity.Identifiable;
import java.util.Optional;

/**
 * This interface represents Data Access Object pattern as a part of MVC pattern and provides general
 * operations for access database. Based on the data obtained from {@link org.springframework.jdbc.core.JdbcTemplate} objects are
 * mapped for further processing
 *
 * @author Alexey Bliznichenko
 * @param <T> has to implement {@link Identifiable} interface
 * @see Identifiable
 */
public interface EntityDao<T extends Identifiable> {

    /**
     * Inserts a new record in the table if the id of the given entity does not exist int the table or update an existing
     * record if it the given id exists int the table
     *
     * @param entity {@link Identifiable} object
     */
    long create(T entity);

    /**
     * Deletes a record in the database by id of an {@link Identifiable} object
     *
     * @param id {@link Identifiable}
     */
    void deleteById(long id);

    /**
     *  Returns an Optional object contains an {@link Identifiable} object found in the database. If nothing has been found then
     *  an empty object would have been returned
     *
     * @param id {@link Identifiable}
     * @return Optional of {@link Identifiable} object
     */
    Optional<T> findById(long id);

}
