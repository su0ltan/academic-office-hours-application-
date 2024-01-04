package com.sultan.lasttest.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.lasttest.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class sign_up extends AppCompatActivity {
    Spinner spn;
    EditText firstname , lastname , email , pasword ;
    TextView txtmail;
    CardView cardView;
    String strLastName , strFirsname ,ff , strPass ,strEmail;
    String[] typeOfRegist = new String[]{"Teacher","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtmail = findViewById(R.id.signUpmailType);
        cardView = findViewById(R.id.SignupCard);
        spn =(Spinner)findViewById(R.id.signUpSpinner);
        firstname =(EditText)findViewById(R.id.signUpFirstName);
        lastname =(EditText)findViewById(R.id.signUplastName);
        email =(EditText)findViewById(R.id.signUpemail);
        pasword=(EditText)findViewById(R.id.signUppassword);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this , R.layout.support_simple_spinner_dropdown_item,typeOfRegist);
        final FirebaseFirestore db= FirebaseFirestore.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        spn.setAdapter(arrayAdapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long l) {
                String x = spn.getSelectedItem().toString();
                Toasty.success(getApplicationContext(),x).show();
                if(x.equals("Teacher")){
                    txtmail.setText("@KSU.EDU.SA");
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            strEmail = email.getText().toString();
                            strPass = pasword.getText().toString();
                            ff = txtmail.getText().toString();
                            strEmail = strEmail + ff;
                            strFirsname = firstname.getText().toString();
                            strLastName = lastname.getText().toString();



                            auth.createUserWithEmailAndPassword(strEmail,strPass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){
                                                /*Toasty.success(view.getContext(),auth.getUid()).show();*/
                                            /*    *//*List<Integer> time = new ArrayList<>();
                                                List<String> course = new ArrayList<>();
                                                for(int i =0;i<10;i++){
                                                    time.add(0);
                                                }*//*
                                                course.add("0");*/
                                                String floor ="0",building="0",office="0";
                                                Map<String, Object> docData = new HashMap<>();
                                                docData.put("buildingNO",building);
                                                docData.put("floorNO",floor);
                                                docData.put("officeNO",office);
                                                docData.put("course", Arrays.asList());
                                                docData.put("timeAvailable", Arrays.asList(0,0,0,0,0,0,0,0,0,0));
                                                docData.put("email",strEmail);
                                                docData.put("name" , strFirsname);
                                                docData.put("lastName",strLastName);
                                                docData.put("deptID","0");
                                                String id = auth.getUid();
                                                db.collection("teacher").document(id).set(docData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toasty.success(view.getContext(),"تم تسجيلك بنجاح, الرجاء تأكيدة حسابك بالايميل").show();

                                                                        email.setText("");pasword.setText("");firstname.setText("");lastname.setText("");
                                                                    }


                                                                }
                                                            });
                                                        }
                                                        else

                                                            Toasty.error(view.getContext(),task.getException().getMessage()).show();
                                                    }
                                                });


                                            }
                                            else {
                                                Toasty.error(view.getContext(),task.getException().getMessage()).show();
                                            }
                                        }
                                    });

                        }
                    });




                }else{
                    txtmail.setText("@STUDENT.KSU.EDU.SA");
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            strEmail = email.getText().toString();
                            strPass = pasword.getText().toString();
                            ff = txtmail.getText().toString();
                            strEmail = strEmail + ff;
                            strFirsname = firstname.getText().toString();
                            strLastName = lastname.getText().toString();

                            auth.createUserWithEmailAndPassword(strEmail , strPass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){


                                                Map<String, Object> docData = new HashMap<>();
                                                docData.put("studentID" , email.getText().toString());
                                                docData.put("email" , strEmail);
                                                docData.put("name",strFirsname);
                                                docData.put("lastName" , strLastName);
                                                docData.put("course",Arrays.asList());
                                                docData.put("deptID","0");
                                                String id = auth.getUid();
                                                db.collection("student").document(id).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                           /* auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){

                                                                    }

                                                                }
                                                            });*/
                                                            Toasty.success(view.getContext(),"تم تسجيلك بنجاح, الرجاء تأكيدة حسابك بالايميل").show();
                                                            email.setText("");pasword.setText("");firstname.setText("");lastname.setText("");

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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
