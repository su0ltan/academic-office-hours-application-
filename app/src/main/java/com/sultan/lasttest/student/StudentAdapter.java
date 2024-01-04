package com.sultan.lasttest.student;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Course;
import com.sultan.lasttest.database.request;


import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    private List<request> mDataset;

    public final String TAG = "StudentAdapter";
    String  status;
    String  reason , date , student;

    final String reqstat = "حالة الطلب: " ;
    final String reqdate = "تاريخ الطلب: " ;
    final String reqsreson = "سبب الرفض: " ;
    final String reqscancel = "سبب الإلغاء: " ;
    final String reqstudent = "معرف الطالب: " ;
    final String cousenem = "اسم المقرر: ";
    Course c;






    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public CardView textView;
        public TextView coursename , reqdate;
        Button review;
        public MyViewHolder(CardView v,TextView coursename , TextView reqdate , Button review) {
            super(v);
            textView = v;
            this.coursename = coursename;
            this.reqdate =reqdate;
            this.review=review;


        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentAdapter(List<request> myDataset ) {
       // this.mDataset1 = mDataset1;

        mDataset = myDataset;


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_status, parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView reqcourse = (TextView) v.findViewById(R.id.requestcousename);
        TextView reqdate = (TextView) v.findViewById(R.id.requestdate1);
        Button review = (Button) v.findViewById(R.id.btnReview1);
        MyViewHolder vh = new MyViewHolder(v,reqcourse,reqdate,review);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element



        String stats = mDataset.get(position).status;
        if(stats.equals("0"))
            holder.reqdate.setTextColor(Color.parseColor("#666633"));
        else if(stats.equals("2") || stats.equals("5"))
            holder.reqdate.setTextColor(Color.parseColor("#ff0000"));
        else if (stats.equals("3")){}
        else holder.reqdate.setTextColor(Color.parseColor("#00b300"));




     /*   switch (mDataset.get(position).status) {
            case "0":
                holder.reqdate.setTextColor(Color.parseColor("#666633"));
                break;
            case "2":
                holder.reqdate.setTextColor(Color.parseColor("#ff0000"));
                break;
            case "3":
                break;
            case "5":
                holder.reqdate.setTextColor(Color.parseColor("#ff0000"));

            default:
                holder.reqdate.setTextColor(Color.parseColor("#00b300"));
                break;
        }
        */

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("course").document(mDataset.get(position).courseID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    c = new Course();
                    c=task.getResult().toObject(Course.class);
                    holder.coursename.setText(c.courseName);
                    holder.reqdate.setText(mDataset.get(position).date);



                }
            }
        });



        holder.review.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                date=mDataset.get(position).date;

//                Toasty.success(view.getContext(),mDataset1.get(position).courseName).show();



                switch (mDataset.get(position).status) {
                    case "0":
                        status = "قيد الانتظار";
                        holder.reqdate.setTextColor(Color.parseColor("#666633"));
                        break;
                    case "2":
                        status = "تم الرفض";
                        holder.reqdate.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case "3":
                        status = "ماضي";
                        break;
                    case "5":
                        status = "تم إلغاء الموعد";
                        break;
                    default:
                        holder.reqdate.setTextColor(Color.parseColor("#00b300"));
                        status = "تمت الموافقه";
                        break;
                }
                if(mDataset.get(position).status.equals("2"))
                {
                    if(mDataset.get(position).reason!=null)
                    {
                        reason = mDataset.get(position).reason;
                    }

                    else
                    {
                        reason = "لا يوجد";
                    }
                }
                if(mDataset.get(position).status.equals("5"))
                {
                    if(mDataset.get(position).reason!=null)
                    {
                        reason = mDataset.get(position).reason;
                    }

                    else
                    {
                        reason = "لا يوجد";
                    }
                }


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                View itemView = LayoutInflater.from(view.getContext()).inflate(R.layout.requests_status_view, null);
                TextView r = (TextView)itemView.findViewById(R.id.txtrequestsStatus) ;




                if(mDataset.get(position).status.equals("2"))
                    r.setText(cousenem + c.courseName +"\n"+reqdate + date +"\n"+reqstat +status +"\n"+ reqsreson +reason );
                else if(mDataset.get(position).status.equals("5"))
                r.setText(cousenem + c.courseName +"\n"+reqdate + date +"\n"+reqstat +status +"\n"+ reqscancel +reason );
                else
                    r.setText( cousenem +c.courseName +"\n"+reqdate + date+"\n"+reqstat +status );
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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






    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}