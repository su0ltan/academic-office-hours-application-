package com.sultan.lasttest.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Teacher;

public class teacher_info extends AppCompatActivity {

    TextView txtName , txtDept , txtEmail , txtfloor , txtbuilding , txtoffice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        txtName =(TextView)findViewById(R.id.txtteacherName);
        txtEmail =(TextView)findViewById(R.id.txtteacherEmail);
        txtDept =(TextView)findViewById(R.id.txtteacherDepartment);
        txtfloor =(TextView)findViewById(R.id.txtteacherfloor);
        txtbuilding =(TextView)findViewById(R.id.txtteacherBuilding);
        txtoffice =(TextView)findViewById(R.id.txtteacheroffice);
        Teacher teacher =(Teacher) getIntent().getSerializableExtra("teacher");
        txtName.setText(teacher.name +" "+teacher.lastName);
        txtEmail.setText(teacher.email);
        txtbuilding.setText(teacher.buildingNO);
        txtfloor.setText(teacher.floorNO);
        txtoffice.setText(teacher.officeNO);
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        db.collection("department").document(teacher.deptID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   txtDept.setText(task.getResult().getString("dptName"));
                    }
                });

    }
}
