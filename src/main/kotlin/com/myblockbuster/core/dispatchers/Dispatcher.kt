package com.myblockbuster.core.dispatchers

/**
 * Dispatcher on charge of routing our client request to the correct function
 * @param K Key type
 * @param T Return object type
 */
interface Dispatcher<in K, out T> {

    /**
     * Function that finds and executes a specific function given a {@link K} unique key
     * returning a {@link T} result object
     * @param key Unique key to find and execute a function
     * @return T
     */
    fun locate(key: K): T?
}