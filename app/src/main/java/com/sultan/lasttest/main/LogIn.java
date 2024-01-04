package com.sultan.lasttest.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sultan.lasttest.MainActivity;
import com.sultan.lasttest.R;
import com.sultan.lasttest.admin.add_new_course_admin;
import com.sultan.lasttest.database.department;
import com.sultan.lasttest.student.studentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class LogIn extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private Button x;
    String deptid;
    Button btnLogin , check;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        check = findViewById(R.id.sentNotify);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    db.collection("notification").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot q : task.getResult()){

                                            String id = q.getId();
                                            db.collection("notification").document(id).delete();
                                        }
                                    }
                                }
                            });
            }
        });

      //  x=(Button)findViewById(R.id.openAddCourseActAdmin);
        final View vi = findViewById(R.id.activity_main_id);

    ///c


        //import views
        inputEmail = (EditText) findViewById(R.id.userName);
        inputPassword = (EditText) findViewById(R.id.pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnResetPassword = (Button) findViewById(R.id.btn_reset_password);



        //go to reset possword activity
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, ResetPasswordActivity.class));
            }
        });

        //login form
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v){
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();


            //is mail empty
                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar
                            .make(v, "الرجاء كتابة الايميل", Snackbar.LENGTH_LONG);
                    snackbar.show();
                return;
            }

                //is pass empty
                if (TextUtils.isEmpty(password)) {
                    Snackbar snackbar = Snackbar
                            .make(v, "الرجاء كتابة الرقم السري", Snackbar.LENGTH_LONG);
                    snackbar.show();
                return;
            }

                progressBar.setVisibility(View.VISIBLE);

            //authenticate user

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        // there was an error
                        if (password.length() < 6) {
                            inputPassword.setError(getString(R.string.invalid_password));
                        } else {

                            Snackbar snackbar = Snackbar
                                    .make(v, "خطأ في الايميل او الرقم السري", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } else {

                       
                        //go to mainActivity clas
                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        
                        
                    }
                }
            });
        }
        });
    }



    ///disable back button while logged out
    @Override
    public void onBackPressed() {


    }
    public void openSignUpAct(View v){
       startActivity(new Intent(LogIn.this, sign_up.class));
    }
    /*public void openAddCourseActAdmin(View v){
        startActivity(new Intent(LogIn.this,add_new_course_admin.class));
    }*/

}
