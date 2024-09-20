package com.vidhika.todolist.Model.Utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vidhika.todolist.Model.ToDoModel

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, NAME, null, VERSION) {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TODO_TABLE")
        // Create tables again
        onCreate(db)
    }

    fun openDatabase() {
        db = writableDatabase
    }

    fun insertTask(task: ToDoModel) {
        val cv = ContentValues().apply {
            put(USER_ID, task.userId)
            put(TASK, task.task)
            put(STATUS, 0)
        }
        db.insert(TODO_TABLE, null, cv)
    }

    fun getAllTasks(currentUserId: String?): List<ToDoModel> {
        val taskList = mutableListOf<ToDoModel>()
        var cur: Cursor? = null
        db.beginTransaction()
        try {
//            onUpgrade(db, 1, 2)
            cur = db.query(TODO_TABLE, null, "userId = '$currentUserId'", null, null, null, null, null)
            if (cur != null && cur.moveToFirst()) {
                do {
                    val task = ToDoModel().apply {
                        id = cur.getInt(cur.getColumnIndexOrThrow(ID))
                        task = cur.getString(cur.getColumnIndexOrThrow(TASK))
                        status = cur.getInt(cur.getColumnIndexOrThrow(STATUS))
                        userId = cur.getString(cur.getColumnIndexOrThrow(USER_ID))
                    }
                    taskList.add(task)
                } while (cur.moveToNext())
            }
        } catch (e: Exception){
            println(e)
        } finally {
            db.endTransaction()
            cur?.close()
        }
        return taskList
    }

    fun updateStatus(id: Int, status: Int) {
        val cv = ContentValues().apply {
            put(STATUS, status)
        }
        db.update(TODO_TABLE, cv, "$ID = ?", arrayOf(id.toString()))
    }

    fun updateTask(id: Int, task: String) {
        val cv = ContentValues().apply {
            put(TASK, task)
        }
        db.update(TODO_TABLE, cv, "$ID = ?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Int) {
        db.delete(TODO_TABLE, "$ID = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val VERSION = 1
        private const val NAME = "toDoListDatabase"
        private const val TODO_TABLE = "todo"
        private const val ID = "id"
        private const val USER_ID = "userId"
        private const val TASK = "task"
        private const val STATUS = "status"
        private const val CREATE_TODO_TABLE = "CREATE TABLE $TODO_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID TEXT, $TASK TEXT, $STATUS INTEGER)"
    }
}
