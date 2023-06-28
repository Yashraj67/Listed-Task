package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.todo.api.DashboardApi
import com.example.todo.api.RetrofitHelper
import com.example.todo.repository.DashboardRepository
import com.example.todo.viewmodels.MainViewModel
import com.example.todo.viewmodels.MainViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = LinkFragment()
        fragmentTransaction.add(R.id.frame_layout, fragment)
        fragmentTransaction.commit()




    }
}