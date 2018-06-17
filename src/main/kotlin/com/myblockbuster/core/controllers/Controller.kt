package com.myblockbuster.core.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.myblockbuster.core.*
import com.myblockbuster.core.services.Service
import kotlin.math.ceil

/**
 * Controller that receives a request and reply with a message of type {@link M}
 * @param M Model type
 */
interface Controller<M> {

    companion object {
        // HTTP constants
        const val HTTP_METHOD: String = "httpMethod"
        const val HTTP_GET: String = "get"
        const val HTTP_POST: String = "post"
        const val HTTP_PUT: String = "put"
        const val HTTP_DELETE: String = "delete"
        const val HTTP_PATCH: String = "patch"

        // Pagination constants
        const val LIMIT: Int = 50
        const val MAX_LIMIT: Int = 100
        const val OFFSET: Int = 0
        const val MIN_LIMIT: Int = 0
    }

    fun anyToInt(value: Any, valueType: String = "page"): Int {
        return when (value) {
            is String -> value.toIntOrNull() ?: throw Exception("$valueType must be a number")
            is Int -> value
            else -> throw Exception("$valueType must be a number")
        }
    }

    fun getStringAnyMap(request: Request, key: String): Map<String, Any> {
        return if (request.input.containsKey(key) && request.input[key] != null)
            request.input[key] as Map<String, Any>
        else
            emptyMap()
    }

    fun getResource(request: Request): String {
        return request.input["resource"] as String
    }

    fun getHeaders(request: Request) : Map<String, Any> {
        return getStringAnyMap(request, "headers")
    }

    fun getPathParameters(request: Request) : Map<String, Any> {
        return getStringAnyMap(request, "pathParameters")
    }

    fun getQueryStringParameters(request: Request) : Map<String, Any> {
        return getStringAnyMap(request, "queryStringParameters")
    }

    fun getRawBody(request: Request) : Any {
        if (request.input["body"] != null)
            return if (request.input["body"] is String) request.input["body"] as String else request.input["body"] as Map<String, Any>
        else
            throw InvalidArguments("body")
    }

    fun <T : Model> getEntity(rawBody: Any, cls: Class<T>): T {
        val objectMapper = ObjectMapper()

        return if (rawBody is String) objectMapper.readValue(rawBody, cls) else objectMapper.convertValue(rawBody, cls)
    }

    fun getPagination(queryParameters: Map<String, Any>): Pagination {
        var page: Int = ceil(OFFSET.toDouble() / LIMIT).toInt()
        var size: Int = LIMIT
        val pagination = Pagination(page, size)

        if (queryParameters.containsKey("page")) {
            page = anyToInt(queryParameters["page"]!!)
            pagination.page = if (page >= MIN_LIMIT + 1) page - 1 else MIN_LIMIT
        }

        if (queryParameters.containsKey("size")) {
            size = anyToInt(queryParameters["size"]!!, "size")
            pagination.size = if (size < MAX_LIMIT) size else MAX_LIMIT
        }

        return pagination

    }

    /**
     * Http router, it receives a class type to return, a request and service to automatically execute and return a response
     * @param cls Class return type
     * @param request Http Client request
     * @param service CRUD service to execute
     */
    fun <T : Model> defaultRouting(cls: Class<T>, request: Request, service: Service<T, User>): Any {
        val resource: String = getResource(request)
        val headers: Map<String, Any> = getHeaders(request)
        val pathParameters: Map<String, Any> = getPathParameters(request)
        val queryParameters: Map<String, Any> = getQueryStringParameters(request)

        return when((request.input[HTTP_METHOD] as String).toLowerCase()) {
            HTTP_GET -> {
                when {
                    resource.endsWith("findOne", true) -> service.findOne(AnonymousUser(), queryParameters)
                    pathParameters.containsKey("id") -> {
                        val id = pathParameters["id"]

                        when (id) {
                            is Int -> service.findOne(AnonymousUser(), id)
                            else -> throw Exception("Id must be an integer")
                        }
                    }
                    else -> {
                        return service.findAll(AnonymousUser(), queryParameters, getPagination(queryParameters))
                    }
                }
            }
            HTTP_POST -> {
                service.create(AnonymousUser(), getEntity(getRawBody(request), cls))
            }
            HTTP_PUT -> {
                service.update(AnonymousUser(), getEntity(getRawBody(request), cls))
            }
            HTTP_DELETE -> {
                service.delete(AnonymousUser(), getEntity(getRawBody(request), cls).id!!)
            }
            HTTP_PATCH -> {
                service.update(AnonymousUser(), getEntity(getRawBody(request), cls))
            }

            else -> {
                throw Exception()
            }
        }
    }
}