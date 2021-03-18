package com.illopen.agent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChartView
import com.illopen.agent.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.activity_dashboard.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class Dashboard : AppCompatActivity() {

    lateinit var chartView : AnyChartView

    private lateinit var binding: ActivityDashboardBinding

//    private val TAG = "Dashboard"
//
//    private var jobs = arrayListOf("New Job","Ongoing Job","Completed Job")
//    private var data = arrayListOf(8,2,5)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initPieChart()
    }

//    private fun initPieChart() {
//        val pie : Pie = AnyChart.pie()
//        val list : ArrayList<DataEntry> = ArrayList()
//
////        for (i in jobs){
////            list[ValueDataEntry[jobs[i])]
////        }
//
//        // TODO: 16-02-2021
//
//        pie.data(list)
//        binding.pieChart.setChart(pie)
//    }
}