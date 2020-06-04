package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextZipcode: EditText = et_zip_code
        val buttonSubmit: Button = bu_submit

        buttonSubmit.setOnClickListener {
            val zipcode: String = editTextZipcode.text.toString()

            if (zipcode.length != 5) {
                Toast.makeText(this, getString(R.string.zipcode_error_message), Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, zipcode, Toast.LENGTH_SHORT).show()
                forecastRepository.loadForecast(zipcode)
            }
        }

        val forecastList: RecyclerView = rv_forecast_list
        forecastList.layoutManager = LinearLayoutManager(this)
        val dailyForecastAdapter = DailyForecastAdapter() { forecastItem ->
            val msg = getString(R.string.forescast_clicked_format, forecastItem.temp, forecastItem.description)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            // update list
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)

    }
}