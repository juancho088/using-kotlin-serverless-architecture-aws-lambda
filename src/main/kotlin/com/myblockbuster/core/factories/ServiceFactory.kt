package com.myblockbuster.core.factories

import com.myblockbuster.core.User
import com.myblockbuster.core.services.Service
import com.myblockbuster.movies.Movie
import com.myblockbuster.movies.services.MovieService

/**
 * Creates a Service according to the Entity type to manage
 */
class ServiceFactory<T>: Factory {

    fun <T : Any> getService(type: Class<T>): Service<T, User> {
        return when (type) {
            Movie::class.java -> MovieService() as Service<T, User>
            else             -> throw IllegalArgumentException()
        }
    }
}