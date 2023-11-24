package net.penguincoders.doit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.Adapters.ToDoAdapter;
import net.penguincoders.doit.Model.ToDoModel;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private DatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // إخفاء شريط العنوان
        Objects.requireNonNull(getSupportActionBar()).hide();

        // تهيئة قاعدة بيانات المهام وعرضها في واجهة المستخدم
        setupTasksDatabaseAndView();

        // إضافة زر عائم لإضافة مهمة جديدة
        setupFloatingActionButton();
    }

    // يقوم بإعادة تحميل البيانات عند إغلاق نافذة الإضافة أو التعديل
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        refreshTaskList();
    }

    // تهيئة قاعدة بيانات المهام وعرضها في واجهة المستخدم
    private void setupTasksDatabaseAndView() {
        db = new DatabaseHandler(this);
        db.openDatabase();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        // عرض المهام بعد تحميلها من قاعدة البيانات
        loadAndDisplayTasks();
    }

    // إعادة تحميل القائمة من قاعدة البيانات وعرض التغييرات
    private void refreshTaskList() {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    // عرض المهام بعد تحميلها من قاعدة البيانات
    private void loadAndDisplayTasks() {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
    }

    // تهيئة زر الإضافة العائم
    private void setupFloatingActionButton() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // عرض نافذة إضافة مهمة جديدة
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }
}
