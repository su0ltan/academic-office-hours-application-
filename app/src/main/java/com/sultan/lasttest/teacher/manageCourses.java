package com.sultan.lasttest.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Teacher;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.section;

import java.util.ArrayList;
import java.util.List;

public class manageCourses extends AppCompatActivity {

    public List<Course>mData = new ArrayList<>();
    Teacher teacher;


    ArrayList<section>sections = new ArrayList<>();
    String teacherid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);

        //get logined teacher information
        db.collection("teacher").document(teacherid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            teacher = task.getResult().toObject(Teacher.class);

                                //get course infromation

                        }
                    }

                });
        db.collection("section").whereEqualTo("teacherUID",teacherid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot q : task.getResult()) {
                        section s = q.toObject(section.class);
                        s.ID = q.getId();
                        sections.add(s);

                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.TeachercourseRecyclerView);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(manageCourses.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        teacher_courses_adapter mAdapter = new teacher_courses_adapter( sections);
                        mRecyclerView.setAdapter(mAdapter);
                    }



                }


                }


        });


      /*  db.collection("course").whereEqualTo("teacherUID",teacherid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot q : task.getResult()){
                        mData.add(q.toObject(Course.class));
                    }

                }

            }
        });*/





    }
    public void openAddNewCourseAct(View v){
        Intent intent = new Intent(manageCourses.this , add_new_section_for_teacher.class);
        intent.putExtra("teacher",teacher);
        startActivity(intent);
    }
}
