package com.sultan.lasttest.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.section;

import java.util.List;

import static com.google.firebase.firestore.FieldValue.arrayRemove;

public class teacher_courses_adapter extends RecyclerView.Adapter<teacher_courses_adapter.MyViewHolder> {
    private List<section> mDataset1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int s;
    Course c;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView textView;
        public TextView corseinfo,coursename;
        CardView delete ;
        public MyViewHolder(CardView v,TextView c,TextView f , CardView d ) {
            super(v);
            textView = v;
            this.corseinfo = c;
            this.delete = d;

            this.coursename = f;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public teacher_courses_adapter( List<section> myDataset1) {

        this.mDataset1 = myDataset1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public teacher_courses_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_manage_courses, parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView corseinfo = (TextView) v.findViewById(R.id._course_info);
        TextView coursename = (TextView) v.findViewById(R.id.course_code_name);
        CardView  delete = (CardView) v.findViewById(R.id.crdViewDeleteCourse);

        MyViewHolder vh = new MyViewHolder(v,corseinfo,coursename, delete);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //final String teacher= mDataset.get(position).teacherUID;
      //  final String courseid = mDataset.get(position).courseID;




        db.collection("course").document(mDataset1.get(position).courseID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            c = task.getResult().toObject(Course.class);
                            holder.corseinfo.setText("اسم المقرر: "+c.courseName+ "\n"+"الشعبة: "+mDataset1.get(position).ID);
                            holder.coursename.setText(c.courseCode);
                        }

                    }
                });
        holder.delete.setOnClickListener(new View.OnClickListener() {
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
                        db.collection("course").document(mDataset1.get(position).courseID)
                                .update("sections" , arrayRemove(mDataset1.get(position).ID));
                        if(!mDataset1.get(position).studentUID.isEmpty()){
                            for(int in=0;in<mDataset1.get(position).studentUID.size();in++){
                                db.collection("student").whereEqualTo("studentID" , mDataset1.get(position).studentUID.get(in))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot q : task.getResult()){
                                                db.collection("student").document(q.getId()).update("course" , arrayRemove(mDataset1.get(position).courseID));
                                            }
                                        }

                                    }
                                });
                            }


                        }
                        db.collection("section").document(mDataset1.get(position).ID).delete();
                        mDataset1.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position);
                        Toast.makeText(view.getContext(), "تم حذف الشعبه بنجاح", Toast.LENGTH_SHORT).show();

                    }

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset1.size();
    }
}