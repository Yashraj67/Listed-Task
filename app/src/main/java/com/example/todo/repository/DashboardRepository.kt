package com.example.todo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.api.DashboardApi
import com.example.todo.models.DashboardList
import com.example.todo.models.RecentLink
import com.example.todo.models.TopLink

class DashboardRepository(private val DashboardApi : DashboardApi) {

    private val DashboardData  = MutableLiveData<DashboardList>()
    val dashboard : LiveData<DashboardList>
    get() = DashboardData

    suspend fun getAll(){
        val result = DashboardApi.getAll()
        if(result?.body() != null){
            DashboardData.postValue(result.body())
        }
    }

}