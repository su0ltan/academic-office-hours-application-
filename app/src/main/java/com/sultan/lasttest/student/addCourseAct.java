package com.sultan.lasttest.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.database.Teacher;
import com.sultan.lasttest.database.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class addCourseAct extends AppCompatActivity  {

   List<Course> course = new ArrayList<>();
   SpinnerDialog spinnerDialog , spnSections;
   TextView txtsearchable , txtsections ;



    Button btn ;
    Teacher teacher;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String id;
    Student student;
    public String courseidChoosen ,t;
    final String TAG="addCourseAct";
    boolean checkStudnet = true;
    String doc = auth.getUid();
    AutoCompleteTextView multiAutoCompleteTextView;
    ArrayList<String> courseName = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_course);
        btn =(Button)findViewById(R.id.btnaddcourse) ;

        //recept courses that sent from anoter class
        course = (List<Course>) getIntent().getSerializableExtra("c");

        //getting courses from database
       db.collection("student").document(doc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

               if(task.isSuccessful()){
                   DocumentSnapshot d = task.getResult();
                   student = d.toObject(Student.class);
                   id = d.get("studentID").toString();
               }
            }
        });




       //getting course name
       for (int i=0;i<course.size();i++){
           courseName.add(course.get(i).courseName);

       }

        txtsections = (TextView)findViewById(R.id.spnsection);
        txtsearchable = findViewById(R.id.txtsearchable);
        spinnerDialog = new SpinnerDialog(addCourseAct.this, courseName,"اختر مقرر");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                txtsearchable.setText(item);
                //x is item that selected from drop down menu
                String x=item;


                for (int i =0;i<course.size();i++){
                    String temp = course.get(i).courseName;


                    if(temp.equals(x)){

                        final String courseID = course.get(i).courseID;
                        final TextView courseName = findViewById(R.id.txtCoursename);
                        final TextView courseCode = findViewById(R.id.txtCoursecode);
                        final TextView t =(TextView)findViewById(R.id.txtTeachername);


                        //dialog for sections
                        spnSections = new SpinnerDialog(addCourseAct.this, (ArrayList<String>) course.get(i).sections,"اختر الشعبة");
                        final int finalI = i;
                        spnSections.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                txtsections.setText(item);
                               db.collection("section").document(item)
                                       .get()
                                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if(task.isSuccessful()){
                                                   DocumentSnapshot d = task.getResult();
                                                   section s = d.toObject(section.class);
                                                   //getting teacher that teach the section
                                                   db.collection("teacher").document(s.teacherUID)
                                                           .get()
                                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                  if(task.isSuccessful()){
                                                                      DocumentSnapshot d = task.getResult();
                                                                      teacher = d.toObject(Teacher.class);
                                                                      t.setText(" " +teacher.name + " "+teacher.lastName);
                                                                      courseName.setText(" " +course.get(finalI).courseName+" ");
                                                                      courseCode.setText(" " +course.get(finalI).courseCode+" ");
                                                                      courseidChoosen = course.get(finalI).courseID;

                                                                      btn.setOnClickListener(new View.OnClickListener() {
                                                                          @Override
                                                                          public void onClick(View view) {
                                                                              String x = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                              db.collection("student").document(x)
                                                                                      .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                  @Override
                                                                                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                      if(task.isSuccessful()){
                                                                                          DocumentSnapshot d = task.getResult();
                                                                                          Student student = d.toObject(Student.class);

                                                                                          for (int i=1;i<student.course.size();i++){
                                                                                              if(student.course.get(i).equals(courseidChoosen)){

                                                                                                  Toasty.info(getApplicationContext(),"انت مسجل بالمقرر").show();
                                                                                                  checkStudnet = false;
                                                                                              }


                                                                                          }
                                                                                          if(checkStudnet){
                                                                                              //if student does not have same course already with add course for student
                                                                                              Map<String, Object> add = new HashMap<>();
                                                                                              add.put("studentUID", FieldValue.arrayUnion(id));
                                                                                              db.collection("section").document(txtsections.getText().toString())
                                                                                                      .update(add);
                                                                                              Map<String, Object> add1 = new HashMap<>();
                                                                                              add1.put("course", FieldValue.arrayUnion(courseidChoosen));

                                                                                              db.collection("student").document(doc)
                                                                                                      .update(add1);
                                                                                              Toasty.success(getApplicationContext(),"تم اضافة المقرر بنجاح",Toast.LENGTH_LONG).show();
                                                                                          }
                                                                                      }
                                                                                  }
                                                                              });
                                                                          }
                                                                      });

                                                                  }
                                                               }
                                                           });
                                               }
                                           }
                                       });
                            }
                        });




                    }
                }
            }
        });

        txtsearchable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show courses dialog
                spinnerDialog.showSpinerDialog();
            }
        });
        txtsections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show sections dialog
                spnSections.showSpinerDialog();
            }
        });


    }







}
