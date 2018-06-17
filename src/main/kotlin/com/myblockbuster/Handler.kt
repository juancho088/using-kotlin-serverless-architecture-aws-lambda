package com.myblockbuster

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.myblockbuster.ApiGatewayResponse.Companion.LOG
import com.myblockbuster.core.*
import com.myblockbuster.core.dispatchers.RequestDispatcher

open class Handler: RequestHandler<Map<String, Any>, ApiGatewayResponse> {

  var requestDispatcher: RequestDispatcher = RequestDispatcher()

  override fun handleRequest(input:Map<String, Any>, context: Context): ApiGatewayResponse {

    var status = 500
    var body: Any? = EmptyModel()

    try {
      body = requestDispatcher.locate(ApiGatewayRequest(input, context)) ?: EmptyModel()

      status = when (body is EmptyModel) {
        true -> 204
        false -> 200
      }
    }
    catch (e: MyException) {
      LOG.error(e.message, e)
      status = e.code
      body = ErrorModel(e.message)
    }
    catch (e: Throwable) {
      LOG.error(e.message, e)
      status = 500
      body = ErrorModel("Internal server error")
    }
    finally {
      return ApiGatewayResponse.build {
        statusCode = status
        if (body is Model)
          objectBody = body as Model?
        else
          listBody = body as List<Any>
        headers = mapOf("X-Powered-By" to "AWS Lambda & Serverless")
      }
    }
  }
}