package com.example.todo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.models.DashboardList
import com.example.todo.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DashboardRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll()
        }
    }

    val dashboard : LiveData<DashboardList>
    get() = repository.dashboard


}