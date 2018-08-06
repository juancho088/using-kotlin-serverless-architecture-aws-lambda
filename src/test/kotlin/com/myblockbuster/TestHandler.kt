package com.myblockbuster

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.mockito.Mockito
import com.amazonaws.services.lambda.runtime.Context
import com.myblockbuster.core.EmptyModel
import com.myblockbuster.core.RouterException
import com.myblockbuster.core.dispatchers.RequestDispatcher

class TestHandler: Spek({
    given("a new event") {
        var handler: Handler? = null
        var context: Context? = null
        var dispatcher: RequestDispatcher? = null
        var map: Map<String, Any>? = null

        beforeEachTest {
            handler = Handler()
            context = Mockito.mock(Context::class.java)
            map = mapOf<String, Any>("path" to "test")
            dispatcher = Mockito.mock(RequestDispatcher::class.java)
            handler?.requestDispatcher = dispatcher!!

        }
        on("correct path") {
            it("should return a status code of 204 if the response body is empty") {
                whenever(dispatcher?.locate(any())).thenReturn(EmptyModel())
                val response = context?.let { handler?.handleRequest(map as Map<String, Any>, it) }
                assertEquals(204, response?.statusCode)
            }
            it("should return a status code of 200 if the response body is not empty") {
                whenever(dispatcher?.locate(any())).thenReturn(TestModel())
                val response = context?.let { handler?.handleRequest(map as Map<String, Any>, it) }
                assertEquals(200, response?.statusCode)
            }

        }
        on("non-existent path") {
            it("should return a status code of 404") {
                whenever(dispatcher?.locate(any())).thenThrow(RouterException(""))
                val response = context?.let { handler?.handleRequest(map as Map<String, Any>, it) }
                assertEquals(404, response?.statusCode)
            }
        }
    }
})