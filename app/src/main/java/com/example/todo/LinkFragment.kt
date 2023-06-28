package com.example.todo

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.todo.api.DashboardApi
import com.example.todo.api.RetrofitHelper
import com.example.todo.models.RecentLink
import com.example.todo.models.TopLink
import com.example.todo.repository.DashboardRepository
import com.example.todo.viewmodels.MainViewModel
import com.example.todo.viewmodels.MainViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*


class LinkFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var lineChart: LineChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val frag = inflater.inflate(R.layout.fragment_link, container, false)

        val dashboardApi = RetrofitHelper.getInstance().create(DashboardApi::class.java)
        val repository = DashboardRepository(dashboardApi)

        val tvGreeting = frag.findViewById<TextView>(R.id.tvGreeting)
        tvGreeting.setText(getGreetingMessage())

        lineChart = frag.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
        configureLineChart()


        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository)
        ).get(MainViewModel::class.java)
        val tvtodaysClicks = frag.findViewById<TextView>(R.id.tvTodaysClicks)
        val tvtopLocation = frag.findViewById<TextView>(R.id.tvTopLocation)
        val tvtopSource = frag.findViewById<TextView>(R.id.tvTopSource)
        val tvtotalClicks = frag.findViewById<TextView>(R.id.tvTotalClicks)

        mainViewModel.dashboard.observe(viewLifecycleOwner){
            val todaysClicks = it.today_clicks.toString()
            val topLocation = it.top_location.toString()
            val topSource = it.top_source.toString()
            val totalClicks = it.total_clicks.toString()

            tvtodaysClicks.setText(todaysClicks)
            tvtopLocation.setText(topLocation)
            tvtopSource.setText(topSource)
            tvtotalClicks.setText(totalClicks)
        }

        mainViewModel.dashboard.observe(viewLifecycleOwner) {

            val recent_links = it.data.top_links
            val dataList = mutableListOf<TopLink>()
            dataList.addAll(recent_links)

            val listView = frag.findViewById<ListView>(R.id.listview)
            val adapter = context?.let { it1 -> TopLinkAdapter(it1, dataList) }
            listView.adapter = adapter

        }

        val recent_link = frag.findViewById<TextView>(R.id.btRecentLinks)
        val top_link = frag.findViewById<TextView>(R.id.btTopLinks)

        recent_link.setOnClickListener{
                top_link.setBackgroundResource(0)
                top_link.setBackgroundColor(Color.TRANSPARENT)
                recent_link.setBackgroundResource(R.drawable.btn_shape)
                recent_link.setTextColor(Color.WHITE)
                top_link.setTextColor(Color.GRAY)
            mainViewModel.dashboard.observe(viewLifecycleOwner) {

                val recent_links = it.data.recent_links
                val dataList = mutableListOf<RecentLink>()
                dataList.addAll(recent_links)

                val listView = frag.findViewById<ListView>(R.id.listview)
                val adapter = context?.let { it1 -> RecentLinkAdapter(it1, dataList) }
                listView.adapter = adapter
            }

        }


        top_link.setOnClickListener{
            recent_link.setBackgroundResource(0)
            recent_link.setBackgroundColor(Color.TRANSPARENT)
            top_link.setBackgroundResource(R.drawable.btn_shape)
            top_link.setTextColor(Color.WHITE)
            recent_link.setTextColor(Color.GRAY)
            mainViewModel.dashboard.observe(viewLifecycleOwner) {

                val recent_links = it.data.top_links
                val dataList = mutableListOf<TopLink>()
                dataList.addAll(recent_links)

                val listView = frag.findViewById<ListView>(R.id.listview)
                val adapter = context?.let { it1 -> TopLinkAdapter(it1, dataList) }
                listView.adapter = adapter
            }
        }

        return frag
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchDataAndUpdateChart()
    }
    private fun getGreetingMessage(): String {
        val currentTime = Calendar.getInstance().time
        val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val hour = hourFormat.format(currentTime).toInt()

        return when (hour) {
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun configureLineChart() {
        lineChart.apply {
            setNoDataText("No data available")
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            setDrawGridBackground(false)

            val xAxis: XAxis = xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = MonthValueFormatter()

            val yAxis: YAxis = axisLeft
            yAxis.axisMinimum = 0f

            axisRight.isEnabled = false
        }
    }

    private fun prepareData(callback: (Map<String, Int>) -> Unit){
        // Map of date to clicks count
        val dashboardApi = RetrofitHelper.getInstance().create(DashboardApi::class.java)
        val repository = DashboardRepository(dashboardApi)

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository)
        ).get(MainViewModel::class.java)

        var data = mutableMapOf<String, Int>()
        mainViewModel.dashboard.observe(viewLifecycleOwner){
            val jsonobject  = it.data.overall_url_chart

            for(key in jsonobject.keySet()){

                val d = jsonobject.get(key)
                data[key] = d.toString().toInt()

            }

            val monthlyData = mutableMapOf<String, Int>()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            data?.forEach { (dateStr, clicks) ->
                val date = dateFormat.parse(dateStr)
                val month = SimpleDateFormat("MMM", Locale.getDefault()).format(date)
                val currentClicks = monthlyData[month] ?: 0
                monthlyData[month] = currentClicks + clicks

            }

            callback(monthlyData)
        }


    }

    private fun updateLineChart(data: Map<String, Int>) {
        val lineData = generateLineData(data)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun fetchDataAndUpdateChart() {
        prepareData { monthlyData ->
            updateLineChart(monthlyData)
        }
    }

    private fun generateLineData(data: Map<String, Int>): LineData {
        val entries: MutableList<Entry> = ArrayList()

        var index = 0f
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        for ((index, month) in months.withIndex()) {
            val clicks = data[month] ?: 0
            val entry = Entry(index.toFloat(), clicks.toFloat())
            entries.add(entry)
        }

        val dataSet = LineDataSet(entries, "Clicks")
        dataSet.color = Color.BLUE
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.BLUE
        dataSet.fillAlpha = 100
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.setDrawVerticalHighlightIndicator(false)
        dataSet.setDrawHighlightIndicators(false)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)
        return lineData
    }

    inner class MonthValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM", Locale.getDefault())

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val monthIndex = value.toInt()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, monthIndex)
            val date = calendar.time
            return dateFormat.format(date)
        }
    }

}

