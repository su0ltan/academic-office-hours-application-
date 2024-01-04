package com.sultan.lasttest.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.request;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class actReceivedRequest extends AppCompatActivity {

    String teacherid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<request> myDataset = new ArrayList<>();
    List<request> myDatasetForCancel = new ArrayList<>();
    RelativeLayout relativeLayout , relativeLayout1 ;
    Date s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity requests for teacher
        setContentView(R.layout.activity_act_received_request);

        //Toasty.success(getApplicationContext(),teacherid).show();
        db.collection("request").whereEqualTo("teacherID",teacherid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot q : Objects.requireNonNull(task.getResult())){
                        try {
                             s = new SimpleDateFormat("dd/MM/yyyy").parse(q.get("date").toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if((q.get("status").toString().equals("0") && s.after(new Date()))){
                            request r = new request();
                            r.reqID=q.get("reqID").toString();

                            r.courseID=q.get("courseID").toString();
                            r.date=q.get("date").toString();
                            r.problem = q.get("problem").toString();
                            r.studentID=q.get("studentID").toString();
                            r.status=q.get("status").toString();
                            r.teacherID=q.get("teacherID").toString();
                            r.time=q.get("time").toString();
                            r.ID=q.getId();
                            myDataset.add(r);

                        }else if(q.get("status").toString().equals("1")){
                            request r = new request();
                            r.reqID=q.get("reqID").toString();
                            r.courseID=q.get("courseID").toString();
                            r.date=q.get("date").toString();
                            r.problem = q.get("problem").toString();
                            r.studentID=q.get("studentID").toString();
                            r.status=q.get("status").toString();
                            r.teacherID=q.get("teacherID").toString();
                            r.time=q.get("time").toString();
                            r.ID=q.getId();
                            myDatasetForCancel.add(r);

                        }


                    }
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.requestRecyclerView);
                    RecyclerView mRecyclerView1 = (RecyclerView) findViewById(R.id.requestRecyclerViewCalncel);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(actReceivedRequest.this);
                    LinearLayoutManager layoutManager1 = new LinearLayoutManager(actReceivedRequest.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView1.setLayoutManager(layoutManager1);
                    CofirmedRequestsAdapter adapter = new CofirmedRequestsAdapter(myDatasetForCancel);
                    MyRequestesAdapter mAdapter = new MyRequestesAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView1.setAdapter(adapter);
                    relativeLayout =(RelativeLayout) findViewById(R.id.txtisthereapoitnemt);
                    relativeLayout1=(RelativeLayout)findViewById(R.id.Risthereapoitnemt);
                    if(!myDatasetForCancel.isEmpty()){

                    relativeLayout.setVisibility(View.GONE);
                }
                else{
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                if(!myDataset.isEmpty()){
                    relativeLayout1.setVisibility(View.GONE);

                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);

                }

                }

            }
        });

    }

}
