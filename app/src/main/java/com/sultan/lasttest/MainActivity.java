package com.sultan.lasttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.database.Teacher;
import com.sultan.lasttest.database.department;
import com.sultan.lasttest.main.LogIn;
import com.sultan.lasttest.student.MainPage;
import com.sultan.lasttest.teacher.MainPageTeacher;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user;
    public Student student;
    public Teacher teacher;
    String deptid;
    ArrayList<department> departments;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkCurrentUser(user);



    }
    private void checkCurrentUser(FirebaseUser user) {
        // [START check_current_user]

        //if user that came not null
        if (user != null) {
            //&& user.isEmailVerified()
            //splash screen is over go to main menu
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docid = auth.getUid();
            DocumentReference docRef;
            docRef = db.collection("student").document(docid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //if document is in student table(collection)then will open student page
                            student = document.toObject(Student.class);
                            Toast.makeText(getApplicationContext(),student.studentID, Toast.LENGTH_SHORT).show();

                            updatToken("student" , docid);
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            intent.putExtra("student",student);

                             showRegisterDialog(student.deptID,"student" , FirebaseAuth.getInstance().getCurrentUser().getUid() ,intent);




                            /*startActivity(intent);
                            finish();
*/







                            ///goto student main page

                        }else{
                            //if document is in teacher table(collection)then will open teacher page
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            String docid = auth.getUid();
                            DocumentReference docRef;
                            docRef = db.collection("teacher").document(docid);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {

                                            teacher = document.toObject(Teacher.class);
                                            updatToken("teacher" , docid);
                                            Toast.makeText(getApplicationContext(),teacher.email, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainPageTeacher.class);
                                            intent.putExtra("teacher",teacher);

                                            showRegisterDialog(teacher.deptID,"teacher" , FirebaseAuth.getInstance().getCurrentUser().getUid() ,intent);

                                          /*  startActivity(intent);
                                            finish();*/
                                        }
                                    } else {
                                        ///task is not succsesful

                                    }
                                }
                            });
                        }
                    } else {
                        ///task is not succsesful
                    }
                }
            });








            /*Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish();*/




        } else {
            // No user is signed in
            /*View v = findViewById(R.id.activity_main_id);
            Snackbar snackbar = Snackbar
                    .make(v, "logged out due to inactivity", Snackbar.LENGTH_LONG);
            snackbar.show();*/
            //Toast.makeText(getApplicationContext(),"logged out",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            finish();
        }
        // [END check_current_user]
    }
    private void updatToken(String collection,String uid) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){
                    String token = task.getResult().getToken();
                    db.collection(collection).document(uid).update("token" , token)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                }
            }
        });


    }
    private void showRegisterDialog( String dep , String collectionName ,  String UID , Intent intent ) {


            //if user not have department this function with display form to choose hir department
        if(dep.equals("0")) {
            departments = new ArrayList<>();

            db.collection("department").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot d : task.getResult()) {
                                    departments.add(d.toObject(department.class));
                                }

                                ArrayList<String> deptName = new ArrayList<>();
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("...");
                                builder.setMessage("الرجاء تعبيه المعلومات");
                                View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.choose_department_view, null);
                                Spinner spn = (Spinner) itemView.findViewById(R.id.spnDepartment);

                                for (department d : departments) {
                                    deptName.add(d.dptName);
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, deptName);
                                spn.setAdapter(arrayAdapter);
                                spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        String temp = adapterView.getSelectedItem().toString();

                                        for (int i1 = 0; i1 < departments.size(); i1++) {
                                            if (departments.get(i1).dptName.equals(temp)) {
                                                deptid = departments.get(i1).dptID;
                                            }

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                // dialog.setView(itemView);
                                builder.setNegativeButton("cancel ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        db.collection(collectionName).document(UID).update("deptID", deptid)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toasty.success(getApplicationContext(), "تم اختيار القسم الخاص فيك بنجاح").show();
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toasty.error(getApplicationContext(), "الرجاء المحاولة في وقت لاحق").show();
                                                        }

                                                    }
                                                });
                                    }
                                });
                                builder.setView(itemView);

                                androidx.appcompat.app.AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });

        }else
        {
            startActivity(intent);
            finish();

        }


    }


}
