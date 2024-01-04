package com.sultan.lasttest.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class add_new_section_for_teacher extends AppCompatActivity {
    EditText courseName , courseSection , courseCode ;
    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView techerId;
    CardView addCourse;
    TextView spnChooseCourse;
    SpinnerDialog spn;
    ArrayList<String>g;
    String idc;
    Teacher teacher;
    ArrayList<Course>allCourses;
    String section ,name , code;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean checkCourse=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        spnChooseCourse = (TextView)findViewById(R.id.spnAddCoursesearchable);
        courseCode = findViewById(R.id.edttxtCourseCode);
        courseName = findViewById(R.id.edttxtCourseName);
        courseSection=findViewById(R.id.edttxtCourseSecion);
        techerId = findViewById(R.id.txtTeacherUID_addCourses);
        addCourse = findViewById(R.id.crdAddNewCourse);
        teacher = (Teacher)getIntent().getSerializableExtra("teacher");

        techerId.setText(teacher.name +" "+teacher.lastName);

       /* db.collection("teacher").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot d = task.getResult();
                            String f = task.getResult().get("name").toString();
                            String fd = task.getResult().get("lastName").toString();
                            techerId.setText(f + " "+fd);
                            teacher = d.toObject(Teacher.class);
                        }

                    }
                });*/
        allCourses= new ArrayList<>();
        g = new ArrayList<>();
        db.collection("course").whereEqualTo("dptID",teacher.deptID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot q : task.getResult()){
                                allCourses.add(q.toObject(Course.class));
                            }
                            for (Course c : allCourses){
                                g.add(c.courseName);
                            }
                        }
                    }
                });
        spnChooseCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn.showSpinerDialog();
            }
        });
        name = courseName.getText().toString();
        code = courseCode.getText().toString();
        section = courseSection.getText().toString();
        spn = new SpinnerDialog(add_new_section_for_teacher.this, g , "اختر مقرر");

        spn.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                for (int i =0;i<allCourses.size();i++){
                    if(allCourses.get(i).courseName.equals(item)){
                        courseCode.setText(allCourses.get(i).courseCode);
                        courseName.setText(allCourses.get(i).courseName);
                        spnChooseCourse.setText(allCourses.get(i).courseName);
                        idc = allCourses.get(i).courseID;



                    }
                }
            }
        });


        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section = courseSection.getText().toString();
                db.collection("section").document(section).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        Toasty.error(getApplicationContext(),"خطأ,يوجد شعبه بنفس هذا الرقم").show();

                                    }else{

                                        Map<String, Object> addDecomunt= new HashMap<>();

                                        addDecomunt.put("sections" ,FieldValue.arrayUnion(section));
                                        db.collection("course").document(idc).update(addDecomunt);

                                        addDecomunt= new HashMap<>();
                                        addDecomunt.put("courseID",idc);
                                        addDecomunt.put("studentUID",FieldValue.arrayUnion());
                                        addDecomunt.put("teacherUID", id);
                                        db.collection("section").document(section).set(addDecomunt);

                                        addDecomunt = new HashMap<>();
                                        addDecomunt.put("course",FieldValue.arrayUnion(idc));
                                        db.collection("teacher").document(id).update(addDecomunt);

                                        Toasty.success(getApplicationContext(),"تمت اضافة المقرر بنجاح").show();

                                        courseCode.setText("");
                                        courseName.setText("");
                                        spnChooseCourse.setText("اختر مقرر");
                                        courseSection.setText("");



                                        return;
                                    }
                                }
                            }
                        });




/*

                db.collection("course").document(section).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                          if(task.getResult().exists()){


                          }
                          else {



                          }

                        }
                        else {
                            Toasty.error(getApplicationContext(),"الرجاء المحاولة فيه وقت لاحق");

                        }


                    }
                });
*/
            }
        });





    }
}
