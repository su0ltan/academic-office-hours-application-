package com.sultan.lasttest.student;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sultan.lasttest.database.section;
import com.sultan.lasttest.main.LogIn;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.main.activity_appointment;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.request;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name,studentId;
    public static ArrayList<Course> courses ;
    Student student;
    public static ArrayList<Course>courseName;
    public final String TAG = "MainPage";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtdate;
    ArrayList<Course> courses2;
    String nextAppointment;
    int che=0;




    ArrayList<request> requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_page);

        txtdate=(TextView)findViewById(R.id.txtmydayTecher);
        name = (TextView) findViewById(R.id.name);
        courses = new ArrayList<>();
        studentId = (TextView) findViewById(R.id.txtStdid);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in
        }
        Intent intent = getIntent();
        student = (Student)intent.getSerializableExtra("student");
        name.setText(student.name+" "+student.lastName);
        studentId.setText(student.studentID);






         DocumentReference docRef;
        int i = 0;
        for( i = 0;i<student.course.size();i++) {
            if(!student.course.get(i).equals("")) {
               db.collection("course").whereArrayContains("sections",student.course.get(i))
                       .get().addOnCompleteListener(MainPage.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot q : task.getResult()){
                                courses.add(q.toObject(Course.class));
                            }
                        } else {
                            ///task is not succsesful
                            Log.d(TAG, "get Course failed with ", task.getException());

                        }
                    }

                });
            }
        }







      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming soon!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

/*        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);*/



        requests = new ArrayList<>();







        db.collection("request")
                .whereEqualTo("studentID",student.studentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                //getting the requestest for the login student
                                request r = new request();
                                r.courseID = document.get("courseID").toString();
                                r.date=document.get("date").toString();
                                r.status=document.get("status").toString();
                                try {
                                    Date s = new SimpleDateFormat("dd/MM/yyyy").parse(document.get("date").toString());

                                    if (s.after(new Date()) && r.status.equals("1")&& che==0){
                                        che++;

                                        String x ="EEE yyyy/MM/dd";
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(x);
                                        String date = simpleDateFormat.format(s);
                                        nextAppointment ="التاريخ: " +date + "      " + "الوقت: "+document.get("time").toString();

                                        db.collection("teacher").document(document.get("teacherID").toString()).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                            nextAppointment += "\n"+"المبنى: "+ documentSnapshot.get("buildingNO").toString()
                                                                    +"\n"+"الدور: "+documentSnapshot.get("floorNO").toString() +"\n"+"المكتب: "+documentSnapshot.get("officeNO").toString();


                                                            txtdate.setTextColor(Color.parseColor("#ff3333"));
                                                            txtdate.setTextSize(20);
                                                            txtdate.setText(nextAppointment);



                                                        }
                                                    }
                                                });



                                        }

                                    if(s.before(new Date()) && r.status.equals("0")){
                                        db.collection("request").document(document.getId()).update("status","3");
                                    }else if (s.before(new Date()) && r.status.equals("2")){
                                        db.collection("request").document(document.getId()).update("status","4");

                                    }



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                requests.add(r);


                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
/*        courseName= new ArrayList<>();
        CollectionReference courseref = db.collection("course");


        courseref.whereArrayContains("studentUID",student.StudentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Course course = new Course();
                                course = document.toObject(Course.class);
                                courseName.add(course);

                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
      courses2 = new ArrayList<>();

       db.collection("course").whereEqualTo("dptID" , student.deptID).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               courses2.add(document.toObject(Course.class));

                           }
                       } else {
                           Log.d(TAG, "Error getting documents: ", task.getException());
                       }


                   }
               });




    }

 /*   @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.btn_log_out){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // User is logged in
                updatToken("student",auth.getUid());
            }
            auth.signOut();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainPage.this, LogIn.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

private void updatToken(String collection,String uid) {

    db.collection(collection).document(uid).update("token" , "")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });



}

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void opencalnder(View v){
        Intent intent = new Intent(MainPage.this, activity_appointment.class);
        startActivity(intent);
    }
    public void openSendAct(View v){
        //Open Send request page

        Intent intent = new Intent(MainPage.this, sendRequestAct.class);
        intent.putExtra("s",student);
        startActivity(intent);





    }
    public void openCoursesAct(View v){

        Intent intent = new Intent(getApplicationContext(), Courses.class);
        intent.putExtra("studentid", student.studentID);

        intent.putExtra("f",courses2);


        startActivity(intent);



    }


    public void openRecentReqsAct(View v){
        //Open Recent requests page
       /* System.out.println(courseName.size());*/

        Intent intent = new Intent(MainPage.this, Actrequests.class);
        intent.putExtra("r",requests);
        intent.putExtra("c",courses);
        intent.putExtra("student",student.studentID);
        startActivity(intent);

        //requests = new ArrayList<>();
        //courseName = new ArrayList<>();


    }
    public void openStudentInfo(View v){
        Intent intent= new Intent(MainPage.this, studentInfo.class);
        intent.putExtra("student",student);
        startActivity(intent);

    }


}
