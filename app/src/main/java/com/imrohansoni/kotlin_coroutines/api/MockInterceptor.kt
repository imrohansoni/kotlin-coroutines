package com.imrohansoni.kotlin_coroutines.api

import com.google.gson.Gson
import com.imrohansoni.kotlin_coroutines.models.Category
import com.imrohansoni.kotlin_coroutines.models.Product
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.TimeUnit


object StatusCode {
    const val OK = 200
    const val NOT_FOUND_ERROR = 404
    const val BAD_REQUEST_ERROR = 400
}

val categories = listOf(
    Category(id = 1, name = "electronics"),
    Category(id = 2, name = "clothing"),
    Category(id = 3, name = "accessories"),
    Category(id = 4, name = "groceries"),
    Category(id = 5, name = "footwear"),
)

val products = listOf(
    Product(1, "smartphone", 57999.0, 1, true),
    Product(2, "laptop", 79999.0, 1, false),
    Product(3, "refrigerator", 64999.0, 1, true),
    Product(4, "microwave Oven", 11999.0, 1, false),

    Product(5, "t-shirt", 1499.0, 2, true),
    Product(6, "jeans", 3999.0, 2, true),
    Product(7, "jacket", 5999.0, 2, true),
    Product(8, "dress", 4799.0, 2, false),

    Product(9, "wireless earbuds", 10999.0, 3, true),
    Product(10, "smartwatch", 15999.0, 3, true),
    Product(11, "keyboard", 999.0, 3, false),
    Product(12, "mouse", 599.0, 3, true),

    Product(13, "apples (1 kg)", 299.0, 4, true),
    Product(14, "rice (5 kg)", 999.0, 4, true),
    Product(15, "milk (1 liter)", 119.0, 4, false),
    Product(16, "bread (1 loaf)", 239.0, 4, true),

    Product(17, "sport shoes", 7199.0, 5, true),
    Product(18, "sandals", 2399.0, 5, true),
    Product(19, "boots", 8799.0, 5, false),
    Product(20, "sneakers", 5599.0, 5, true),
)

fun getProductsByCategoryId(categoryId: Int): List<Product> {
    return products.filter { it.categoryId == categoryId }
}

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // delay for one second
        TimeUnit.SECONDS.sleep(1)
        val endpoint = request.url.encodedPath

        return when {
            endpoint.contains("/categories") -> {
                val gson = Gson()
                val categoriesJson = gson.toJson(categories)
                createSuccessResponse(request, categoriesJson)
            }

            endpoint.contains("/products") -> {
                val gson = Gson()

                if (endpoint.matches(Regex("^/products/\\d+\$"))) {
                    val productId = endpoint.substringAfterLast("/").toInt()
                    val product = products.find { it.id == productId }

                    if (product == null) {
                        return createErrorResponse(
                            request,
                            StatusCode.BAD_REQUEST_ERROR,
                            "product with $productId not found"
                        )
                    }

                    val productJson = gson.toJson(product)
                    return createSuccessResponse(request, productJson)
                }

                val categoryId = request.url.queryParameter("categoryId")?.toInt()

                if (categoryId != null) {
                    val category = categories.find { it.id == categoryId }
                    if (category == null) {
                        return createErrorResponse(
                            request,
                            StatusCode.BAD_REQUEST_ERROR,
                            "product with category id of $categoryId not found"
                        )
                    }

                    val products = getProductsByCategoryId(categoryId)
                    val categoriesJson = gson.toJson(products)
                    return createSuccessResponse(request, categoriesJson)
                }

                val categoriesJson = gson.toJson(products)
                createSuccessResponse(request, categoriesJson)
            }

            else -> {
                createErrorResponse(request, StatusCode.NOT_FOUND_ERROR, "$endpoint is defined")
            }
        }
    }
}

fun createSuccessResponse(request: Request, body: String): Response {
    return Response.Builder().code(StatusCode.OK).protocol(Protocol.HTTP_1_1).message("OK")
        .request(request).body(body.toResponseBody("application/json".toMediaType())).build()
}

fun createErrorResponse(request: Request, statusCode: Int, message: String): Response {
    return Response.Builder().code(statusCode).protocol(Protocol.HTTP_1_1).message("Error")
        .request(request).body(message.toResponseBody("application/json".toMediaType())).build()
}