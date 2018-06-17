package com.myblockbuster.core.services

import com.myblockbuster.core.Page
import com.myblockbuster.core.Pagination

/**
 * Service that exposes the capabilities of a {@link T} element
 * @param <K> Natural Key type
 * @param <T> Element type
 * @param <F> Filter type
 * @param <U> User permissions
 */
interface Service<T, in U> {

    /**
     * Creates a [T] in the system
     * @param user [U] User who is requesting (to verify permissions)
     * @param element [T] that is going to be saved
     * @return element with the saved status
     */
    fun create(user: U, element: T): T

    /**
     * Find a set of [T] by a given set of optional parameters
     * @param user [U] User who is requesting (to verify permissions)
     * @param filters Optional parameters
     * @param pagination How to paginate the result
     * @return list of [T]
     */
    fun findAll(user: U, filters: Map<String, Any> = mapOf( "order" to "creationDate" ),
                pagination: Pagination = Pagination(0, 50)): Page<T>

    /**
     * Finds one [T] by the unique ID
     * @param user [U] User who is requesting (to verify permissions)
     * @param id Unique id
     * @return [T] that has that ID
     */
    fun findOne(user: U, id: Int): T

    /**
     * Finds one [T] by an unique natural [K] key
     * @param user [U] User who is requesting (to verify permissions)
     * @param filters Set of filters that will return a unique value
     * @return [T] that has that [Any] natural key
     */
    fun findOne(user: U, filters: Map<String, Any> = emptyMap()): T

    /**
     * Updates a [T]
     * @param user [U] User who is requesting (to verify permissions)
     * @param element [T] that is going to be updated
     * @return updated element
     */
    fun update(user: U, element: T): T

    /**
     * Deletes a [T] given a unique ID
     * @param user [U] User who is requesting (to verify permissions)
     * @param id Unique ID of the [T]
     */
    fun delete(user: U, id: Int)

    /**
     * Returns the amount or [T] entities in the system
     * @param user [U] User who is requesting (to verify permissions)
     * @param filters Set of filters
     * @return list of [T]
     */
    fun count(user: U, filters: Map<String, Any> = emptyMap()): Int

    /**
     * Verifies if a entity with a specific ID exists
     * @param user [U] User who is requesting (to verify permissions)
     * @param id Unique id
     * @return [Boolean] value indicating true if exists or false if not
     */
    fun exists(user: U, id: Int): Boolean
}