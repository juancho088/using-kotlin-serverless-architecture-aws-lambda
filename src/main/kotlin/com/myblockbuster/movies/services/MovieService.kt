package com.myblockbuster.movies.services

import com.myblockbuster.core.Page
import com.myblockbuster.core.Pagination
import com.myblockbuster.core.User
import com.myblockbuster.core.services.Service
import com.myblockbuster.movies.Movie
import com.myblockbuster.movies.MovieAlreadyExistsException
import com.myblockbuster.movies.MovieNotExistsException
import kotlin.math.min

class MovieService: Service<Movie, User> {

    // This is because we don't have a persistence layer yet
    // Part 6 will explain that
    val elements: MutableList<Movie> = ArrayList()

    override fun create(user: User, element: Movie): Movie {
        if (elements.find { it.code == element.code } == null)
            elements.add(element)
        else
            throw MovieAlreadyExistsException()

        return element
    }

    override fun findAll(user: User, filters: Map<String, Any>, pagination: Pagination): Page<Movie> {
        val offset = min(pagination.page * pagination.size, elements.size - 1)
        val toIndex = min(offset + pagination.size, elements.size - 1)
        return Page(filters, pagination.size, offset, elements.size,
                if (elements.isEmpty()) elements else elements.subList(offset, toIndex))
    }

    override fun findOne(user: User, id: Int): Movie {
        val movie: Movie? = elements.find { it.id == id}
        if (movie != null)
            return movie
        else
            throw MovieNotExistsException()
    }

    override fun findOne(user: User, filters: Map<String, Any>): Movie {
        TODO("not implemented")
    }

    override fun update(user: User, element: Movie): Movie {
        val movie = findOne(user, element.id!!)
        movie.cast = element.cast
        movie.code = element.code
        movie.director = element.director
        movie.language = element.language
        movie.rate = element.rate
        movie.title = element.title

        return movie
    }

    override fun delete(user: User, id: Int) {
        val movie = findOne(user, id)
        elements.remove(movie)
    }

    override fun count(user: User, filters: Map<String, Any>): Int {
        return elements.size
    }

    override fun exists(user: User, id: Int): Boolean {
        return elements.find { it.id == id} != null
    }

}