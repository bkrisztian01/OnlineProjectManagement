package hu.bme.aut.android.projectmanagerapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}