package com.sultan.lasttest.student;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.database.Teacher;
import com.sultan.lasttest.database.section;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import es.dmoral.toasty.Toasty;

import static com.google.firebase.firestore.FieldValue.arrayRemove;
import static com.google.firebase.firestore.FieldValue.arrayUnion;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<Course> mDataset;
    public List<section> myDataset1;
    public List<Teacher>teachers;
    String uid;

    final String coursenameAr = "اسم المقرر: ";
    final String sectionArabic = "الشعبه: ";
    final String mailArabic = "الايميل: ";
    final String buildingArabic = "المبنى: ";
    final String floorArabic = "الطابق: ";
    final String teachernameAr = "اسم دكتور: ";
    final String officeArabic = "المكتب: ";




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView textView , crdViewDeleteCourseStudent;
        public TextView corseinfo,coursename;

        public MyViewHolder(CardView v,TextView c,TextView f  , CardView crdViewDeleteCourseStudent) {
            super(v);
            textView = v;
            this.corseinfo = c;
            this.coursename = f;
            this.crdViewDeleteCourseStudent =crdViewDeleteCourseStudent;


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Course> myDataset , List<section> myDataset1 , List<Teacher>teachers) {
        mDataset = myDataset;
        this.myDataset1 = myDataset1;
        this.teachers = teachers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
         CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_courses, parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView corseinfo = (TextView) v.findViewById(R.id._course_info);
        TextView coursename = (TextView) v.findViewById(R.id.course_code_name);
        CardView crdViewDeleteCourseStudent = (CardView)v.findViewById(R.id.crdViewDeleteCourseStudent);

        MyViewHolder vh = new MyViewHolder(v,corseinfo,coursename, crdViewDeleteCourseStudent);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String section = null;

        for(int i=0;i<myDataset1.size();i++){
            if(myDataset1.get(i).courseID.equals(mDataset.get(position).courseID)){
                section = myDataset1.get(i).ID;
            }
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                View itemView = LayoutInflater.from(view.getContext()).inflate(R.layout.requests_status_view, null);
                TextView r = (TextView)itemView.findViewById(R.id.txtrequestsStatus) ;

                for(int i=0;i<myDataset1.size();i++){
                    if(myDataset1.get(i).courseID.equals(mDataset.get(position).courseID)){


                        for(int x1=0;x1<teachers.size();x1++){
                            if(teachers.get(x1).ID.equals(myDataset1.get(i).teacherUID)){
                                Teacher teacher = teachers.get(x1);
                                r.setText("  "+teachernameAr + teacher.name +" "+teacher.lastName +"\n"+"  " + mailArabic + teacher.email.toLowerCase() +"\n"+"  "+buildingArabic
                                        + teacher.buildingNO+"     " + floorArabic + teacher.floorNO +"\n"+"  "+officeArabic +teacher.officeNO);



                            }

                        }




                    }
                }
                builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setView(itemView);


                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



 holder.corseinfo.setText(coursenameAr +mDataset.get(position).courseName+ "\n"+sectionArabic + section);
        holder.coursename.setText(mDataset.get(position).courseCode);
        holder.crdViewDeleteCourseStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                //builder.setTitle("...");
                builder.setMessage("هل انت متأكد");
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       FirebaseFirestore db = FirebaseFirestore.getInstance();
                       db.collection("student").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if(task.isSuccessful()){
                                   Student student = task.getResult().toObject(Student.class);
                                   uid = student.studentID;


                                   for(int i=0 ;i<myDataset1.size();i++){
                                       if(myDataset1.get(i).courseID.equals(mDataset.get(position).courseID)){
                                           section section1 = myDataset1.get(i);

                                           db.collection("section").document(section1.ID).update("studentUID",arrayRemove(uid));
                                           db.collection("student").document(task.getResult().getId()).update("course",arrayRemove(section1.courseID));
                                           myDataset1.remove(i);
                                           mDataset.remove(position);
                                           notifyItemRemoved(position);
                                           notifyItemChanged(position);
                                           Toast.makeText(view.getContext(), "تم حذف المقرر بنجاح", Toast.LENGTH_SHORT).show();
                                           return;
                                       }
                                   }

                               }

                           }
                       });



                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        //holder.textView.setText(mDataset.get(position).courseName);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}