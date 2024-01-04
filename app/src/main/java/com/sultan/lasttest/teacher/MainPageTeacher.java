package com.sultan.lasttest.teacher;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sultan.lasttest.main.LogIn;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Teacher;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class MainPageTeacher extends AppCompatActivity
        {
    TextView name, txtdate , txtEmail;
    public final String TAG = "MainPageTeacher";
    public static ArrayList<Course> courses ;
    public int i ;
    Teacher teacher;
    public static ArrayList<request>requests;
    CardView cardMangecourses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_page);
        courses = new ArrayList<>();

        cardMangecourses = findViewById(R.id.cardMangecourses);
        txtdate=(TextView)findViewById(R.id.txtmydayTecher) ;
        txtEmail=(TextView)findViewById(R.id.txtTeacherEmail);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        name = (TextView) findViewById(R.id.name);

        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in
        }
        Intent intent = getIntent();
        teacher = (Teacher)intent.getSerializableExtra("teacher");
        name.setText(teacher.name+" "+teacher.lastName);
       // txtEmail.setText(teacher.email.toLowerCase());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

       /* i = 0;
        for( i = 0;i<teacher.section.size();i++) {
            db.collection("course").whereArrayContains("sections", teacher.section.get(i))
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot q : task.getResult()){
                            courses.add(q.toObject(Course.class));
                            Toasty.success(getApplicationContext(),courses.get(0).courseName).show();

                        }

                    }

                }
            });
        }*/

/*        for( i = 0;i<teacher.course.size();i++) {

            docRef = db.collection("course").document(teacher.course.get(i));

            docRef.get().addOnCompleteListener(MainPageTeacher.this, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            System.out.println(document.get("courseCode").toString()+" "+ document.get("courseName").toString()+" "+document.get("teacherUID").toString()+" "+document.get("studentUID").toString());
                            courses.add(document.toObject(Course.class));

                        } else {
                            ///document doesnt exist
                            Log.d(TAG, "No such Course");

                        }
                    } else {
                        ///task is not succsesful
                        Log.d(TAG, "get Course failed with ", task.getException());

                    }
                }

            });
        }*/
        //System.out.println(courses.get(0).courseName);
        //course1.setText(courses.get(0).courseCode);
        //course2.setText(courses.get(1).courseCode);




      /*  NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        /*navigationView.setNavigationItemSelectedListener(this);*/







        ///////////for geting requests to array
        requests = new ArrayList<>();

        final FirebaseFirestore dd = FirebaseFirestore.getInstance();

        dd.collection("request")
                .whereEqualTo("teacherID", auth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                 /*String CourseID = (String)document.get("CourseID"), reqID = (String)document.get("reqID"),StudentID =(String)document.get("StudentID"), TeacherID=(String)document.get("TeacherID"),Time=(String)document.get("Time"), status=(String)document.get("status"), Date=(String)document.get("Date"), problem = (String) document.get("problem");
                                 request r = new request(StudentID,TeacherID,Time,status,CourseID,reqID,Date,problem);*/

                                 request r = new request();
                                 r.reqID=document.get("reqID").toString();
                                 r.courseID=document.get("courseID").toString();
                                 r.date=document.get("date").toString();
                                 r.problem = document.get("problem").toString();
                                r.reason=document.get("reason").toString();
                                 r.studentID=document.get("studentID").toString();
                                 r.status=document.get("status").toString();
                                 r.teacherID=document.get("teacherID").toString();
                                 r.time=document.get("time").toString();
                                 r.ID=document.getId();
                                requests.add(r);

                                try {
                                    String status = document.get("status").toString();
                                    Date s = new SimpleDateFormat("dd/MM/yyyy").parse(document.get("Date").toString());

                                    if (s.after(new Date()) && status.equals("1")){

                                        String x ="EEE yyyy/MM/dd";
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(x);
                                        String date = simpleDateFormat.format(s);
                                        txtdate.setText("التاريخ: " +date + "      " + "الوقت: "+document.get("time").toString()+"\n"+"رقم الطالب: "+document.get("sccccccccccctudentID").toString());
                                        
                                        txtdate.setTextSize(20);

                                    }

                                    if(s.before(new Date()) && status.equals("0")){
                                        dd.collection("request").document(document.getId()).update("status","3");
                                    }else if (s.before(new Date()) && status.equals("2")){
                                        dd.collection("request").document(document.getId()).update("status","4");

                                    }



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }




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
        getMenuInflater().inflate(R.menu.main_page_teacher, menu);
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
                updatToken("teacher" ,auth.getUid());
            }
            auth.signOut();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainPageTeacher.this, LogIn.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

   /* @SuppressWarnings("StatementWithEmptyBody")
   *//* @Override
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

        }*//*

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    public void saveCourse(){

    }
private void updatToken(String collection,String uid) {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection(collection).document(uid).update("token" , "")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });



}
    public void openRecentRequstsAct(View view){
        ArrayList<request> c = new ArrayList<>();

        for(int i=0 ; i<requests.size();i++){
            if (requests.get(i).status.equals("0")){
                c.add(requests.get(i));

            }
        }



        //open requests activity with sending the requests that cames from database
            Intent intent = new Intent(MainPageTeacher.this, actReceivedRequest.class);
            intent.putExtra("r",c);
            startActivity(intent);
            c =new ArrayList<>();





    }
    public void openAllRequstsAct(View view){




        //open requests activity with sending the requests that cames from database
        Intent intent = new Intent(MainPageTeacher.this, ActTeacherRequests.class);
        intent.putExtra("r",requests);
        startActivity(intent);





    }
    public void openOfficehoursAct(View view){
        Intent intent = new Intent(getApplicationContext(), officeHoursAct.class);
        startActivity(intent);
    }
    public void openManageCoursesAct(View view){
        Intent intent = new Intent(MainPageTeacher.this , manageCourses.class);
        startActivity(intent);

    }
    public void openteacherInfo(View v){
        Intent intent = new Intent(MainPageTeacher.this, teacher_info.class);
        intent.putExtra("teacher",teacher);
        startActivity(intent);
    }


}
