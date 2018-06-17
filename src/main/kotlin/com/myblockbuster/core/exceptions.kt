package com.myblockbuster.core

/**
 * Custom exception for our project
 * @property code HTTP code that the client is going to receive
 * @property message Exception message
 * @constructor By default it will reply with a 500 HTTP method
 */
abstract class MyException(var code: Int, override var message: String): Exception(message) {
    constructor() : this(500, "Internal MyBlockbuster Exception")
}

/**
 * Exception thrown when the body doesn't contain all the required fields
 * @property body Input body
 */
class InvalidArguments(private var body: String) :
        MyException(400, "The entity $body doesn't contain all the required fields")

/**
 * Router Exception (request-dispatcher)
 * @property resource The route/resource that failed
 * @constructor The default exception is 404 not found exception
 */
class RouterException(private var resource: String) :
        MyException(404, "The route/resource $resource doesn't exist")