package com.epam.esm.entity;

/**
 * This interface specifies that an object of a class implements this interface has an id field
 *
 * @author Alexey Bliznichenko
 */
public interface Identifiable {

    /**
     * Returns the id of the object
     *
     * @return long
     */
    long getId();

}
