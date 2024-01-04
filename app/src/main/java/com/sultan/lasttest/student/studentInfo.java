package com.sultan.lasttest.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.database.department;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.makeText;

public class studentInfo extends AppCompatActivity {

    Student student;
    TextView studentName , studentEmail , studentID, studentDepartment;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        student =(Student) getIntent().getSerializableExtra("student");
        studentName = (TextView)findViewById(R.id.txtstudentName);
        studentEmail = (TextView)findViewById(R.id.txtstudenEmail);
        studentID = (TextView)findViewById(R.id.txtstudentiid);
        studentDepartment = (TextView)findViewById(R.id.txtstudentDepartment);

        //btnChange = (Button)findViewById(R.id.btnchangeDepartment);



        studentID.setText(student.studentID);
        studentName.setText(student.name + " " + student.lastName);

        studentEmail.setText(student.email);

        db.collection("department").document(student.deptID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    department d = new department();
                    d = task.getResult().toObject(department.class);
                    studentDepartment.setText(d.dptName);

                }
            }
        });
        db.collection("department").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           ArrayList<department> fff = new ArrayList<>();
                           for(DocumentSnapshot d : task.getResult()){
                               fff.add(d.toObject(department.class));
                           }
                           //showRegisterDialog(fff, "student",FirebaseAuth.getInstance().getCurrentUser().getUid());
                       }
                    }
                });

        /*btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
    }


}
