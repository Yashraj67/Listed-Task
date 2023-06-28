package com.example.todo.api

import com.example.todo.models.DashboardList
import retrofit2.Response
import retrofit2.http.GET

interface DashboardApi {

    @GET("/api/v1/dashboardNew")
    suspend fun getAll() : Response<DashboardList>


}