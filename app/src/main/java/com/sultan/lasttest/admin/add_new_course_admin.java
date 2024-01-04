package com.sultan.lasttest.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.lasttest.R;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class add_new_course_admin extends AppCompatActivity {

    EditText IcourseName  ,     IcourseCode , Idapartment;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course_admin);
        IcourseCode=(EditText)findViewById(R.id.txtcourseCodeAdmin);
        IcourseName=(EditText)findViewById(R.id.txtcourseNameAdmin);
        Idapartment=(EditText)findViewById(R.id.txtdepatment);
        save=(Button)findViewById(R.id.btnaddNewCourseAdmin);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String code= IcourseCode.getText().toString();
               String name = IcourseName.getText().toString();
               String department = Idapartment.getText().toString();
                Map<String, Object> course = new HashMap<>();

                course.put("courseName" , name);
                course.put("courseCode",code );
                course.put("courseID",code );
                course.put("sections" , FieldValue.arrayUnion());
                course.put("dptID" , department);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("course").document(code).set(course)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toasty.success(getApplicationContext(),"تم اضافة المقرر بنجاح").show();
                                    IcourseCode.setText("");
                                    Idapartment.setText("");
                                    IcourseName.setText("");
                                }
                            }
                        });


            }
        });
    }
}
