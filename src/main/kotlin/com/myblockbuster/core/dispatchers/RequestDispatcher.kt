package com.myblockbuster.core.dispatchers


import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.myblockbuster.core.ApiGatewayRequest
import com.myblockbuster.core.RouterException
import com.myblockbuster.core.Routes
import kotlin.reflect.full.createInstance

/**
 * Request Dispatcher implementation
 */
open class RequestDispatcher: Dispatcher<ApiGatewayRequest, Any> {

    @Throws(RouterException::class)
    override fun locate(key: ApiGatewayRequest): Any? {
        val path = key.input["path"]

        var response: Any? = null
        var found: Boolean = false

        for ((regex, function, cls) in ROUTER.routes) {
            val match = Regex(regex).matchEntire(path as CharSequence)

            if (match != null) {
                // Finds the class based on the absolute class name and runs the function
                val kClass = Class.forName(cls).kotlin
                val func = kClass.members.find { it.name == function }
                response = func?.call(kClass.createInstance(), key)
                found = true

                break
            }
        }

        if (!found)
            throw RouterException(path as? String ?: "")

        return response
    }

    /**
     * Singleton that loads the routes once and keep them on memory
     */
    companion object BackendRouter {
        private val FILE = File(javaClass.classLoader.getResource("routes.yml")!!.file)
        val ROUTER: Routes = ObjectMapper(YAMLFactory()).readValue(FILE, Routes::class.java)
    }

}