package com.dicoding.courseschedule.ui.add


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private var startTime = "start_time"
    private var endTime = "end_time"

    private lateinit var viewModel : AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory  = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                val edCourseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val spinner = findViewById<Spinner>(R.id.spinner_day).selectedItem.toString()
                val spinnerDay = getDayNumber(spinner)
                val edLecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val edNote = findViewById<TextInputEditText>(R.id.ed_note).text.toString()

                when {
                    edCourseName.isEmpty() -> false
                    startTime.isEmpty() ->  false
                    endTime.isEmpty() -> false
                    spinnerDay == -1 -> false
                    edLecturer.isEmpty() ->  false
                    edNote.isEmpty() -> false
                    else ->
                        viewModel.insertCourse(
                            edCourseName,
                            spinnerDay,
                            startTime,
                            endTime,
                            edLecturer,
                            edNote
                        )
                }
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTimePicker(view: View) {
        val pick = when(view.id) {
            R.id.ib_start_time -> "start_time"
            R.id.ib_end_time -> "end_time"
            else -> "default"
        }

        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, pick)
    }


    private fun getDayNumber(day: String):Int {
    val days =  resources.getStringArray(R.array.day)
    return  days.indexOf(day)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == "start_time") {
            findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
            startTime = timeFormat.format(calendar.time)
        } else if (tag == "end_time") {
            findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
            endTime = timeFormat.format(calendar.time)
        }
    }


}