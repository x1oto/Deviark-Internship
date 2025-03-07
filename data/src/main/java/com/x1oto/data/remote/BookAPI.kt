package com.x1oto.data.remote

import com.x1oto.data.model.BookDTO
import com.x1oto.domain.model.Book
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface BookAPI {
    @GET("books/{id}")
    suspend fun getFirstBook(@Path("id") id: Int = 1): BookDTO

    @GET("books/list")
    suspend fun getBooksDesc(@Query("sort") sort: String)

    @POST("books")
    suspend fun sendRandomBook(@Body bookDTO: BookDTO)
}