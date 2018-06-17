package com.myblockbuster.movies.controllers

import com.myblockbuster.core.Request
import com.myblockbuster.core.User
import com.myblockbuster.core.controllers.Controller
import com.myblockbuster.core.factories.ServiceFactory
import com.myblockbuster.core.services.Service
import com.myblockbuster.movies.Movie

class MovieController: Controller<Movie> {

    fun movie(request: Request?) : Any {
        val service: Service<Movie, User> = ServiceFactory<Movie>().getService(Movie::class.java)
        return defaultRouting(Movie::class.java, request!!, service)
    }
}