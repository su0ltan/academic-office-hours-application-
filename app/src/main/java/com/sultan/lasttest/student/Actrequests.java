package com.sultan.lasttest.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Actrequests extends AppCompatActivity {

    public final String TAG = "Actrequests";
    List<Course> myDataset1 = new ArrayList<>();

    request r;





    String id ;


    List<request> mdate = new ArrayList<>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  myDataset1 = (List<Course>) getIntent().getSerializableExtra("c");
        setContentView(R.layout.activity_actrequests);
        mdate = new ArrayList<>();
        myDataset1=new ArrayList<>();
        //getting student id to use to get student requests
        id = getIntent().getSerializableExtra("student").toString();

        //gettins student's requests
        db.collection("request").whereEqualTo("studentID",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot q: task.getResult()){

                                     r = new request();
                                    r.reqID=q.get("reqID").toString();
                                    r.reason = q.get("reason").toString();
                                    r.courseID=q.get("courseID").toString();
                                    r.date=q.get("date").toString();
                                    r.problem = q.get("problem").toString();
                                    r.studentID=q.get("studentID").toString();
                                    r.status=q.get("status").toString();
                                    r.teacherID=q.get("teacherID").toString();
                                    r.time=q.get("time").toString();
                                  //  Toasty.error(getApplicationContext(),r.ID).show();




                                // add requests to arraList
                                mdate.add(r);

                            }

                            //open recycler view to display requests

                            Collections.sort(mdate, Comparator.comparing(request::getDate));
                            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.requestStudentRecyclerView);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(Actrequests.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            StudentAdapter mAdapter = new StudentAdapter(mdate );
                            mRecyclerView.setAdapter(mAdapter);



                        }


                    }

                });
    }
}

