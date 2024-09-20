package com.vidhika.todolist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vidhika.todolist.Adapter.ToDoAdapter
import com.vidhika.todolist.Model.ToDoModel
import com.vidhika.todolist.Model.Utils.DataBaseHandler
import java.util.*


class MainActivity : AppCompatActivity(), DialogCloseListener {

    private lateinit var db: DataBaseHandler
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var textView: TextView
    private var taskList: MutableList<ToDoModel> = mutableListOf()

    val RC_SIGN_IN: Int = 9001
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "MainActivity created") // Log statement

        supportActionBar?.hide()  // Hides the action bar

        db = DataBaseHandler(this)
        db.openDatabase()

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = ToDoAdapter(db, this)
        tasksRecyclerView.adapter = tasksAdapter

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        fab = findViewById(R.id.fab)

        taskList = db.getAllTasks(getCurrentUser()).toMutableList()
        taskList.reverse()

        tasksAdapter.setTasks(taskList)

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
        textView = findViewById(R.id.tasksText)

        val btnSignOut = findViewById<Button>(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            signOut()
        }

    }


    override fun handleDialogClose(dialog: DialogInterface) {
        taskList = db.getAllTasks(getCurrentUser()).toMutableList()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyDataSetChanged()
    }

    private fun getCurrentUser() = GoogleSignIn.getLastSignedInAccount(this)?.email

    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
//            .requestIdToken("507033656119-a5kdr6kbmk2o54ibkem5nlo03drel53t.apps.googleusercontent.com")
            .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient?.signOut()

        val intent = Intent(this, google_signin::class.java)
        startActivity(intent)
    }

}