package com.sultan.lasttest.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.Student;
import com.sultan.lasttest.database.request;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRequestesAdapter extends RecyclerView.Adapter<MyRequestesAdapter.MyViewHolder> {
    private List<request> mDataset ;
    public final String TAG = "MyRequestesAdapter";
    EditText reason;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView textView;
        public TextView studentID ,time , date;

        public CardView review , accept , reject;
        public MyViewHolder(CardView v,TextView s,TextView t , TextView d , CardView r ,CardView a,CardView re ) {
            super(v);

            this.studentID = s;
            this.date = d;
            this.time=t;
            textView = v;
            this.review = r;
            this.accept =a;
            this.reject=re;

        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRequestesAdapter(List<request> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRequestesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receivedrequest,parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView studentid = (TextView) v.findViewById(R.id.txtstudntid);
        TextView time = (TextView) v.findViewById(R.id.txttime1 );
        TextView date = (TextView) v.findViewById(R.id.txtdate1);
        CardView accept = (CardView) v.findViewById(R.id.buttonaccpet) ;
        CardView reject = (CardView) v.findViewById(R.id.buttonreject) ;
        CardView review = (CardView) v.findViewById(R.id.buttonreview) ;

        MyViewHolder vh = new MyViewHolder(v,studentid,time,date,review,accept,reject);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String studentID = mDataset.get(position).studentID;

        FirebaseFirestore dd = FirebaseFirestore.getInstance();
        DocumentReference updateRequest = dd.collection("request").document(mDataset.get(position).ID);



        try {
            Date s = new SimpleDateFormat("dd/MM/yyyy").parse(mDataset.get(position).date);
            String x ="EEE yyyy/MM/dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(x);
            String date = simpleDateFormat.format(s);
            holder.date.setText("التاريخ: "+date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.studentID.setText("معرف الطالب: "+mDataset.get(position).studentID);
        
        holder.time.setText("الوقت: "+mDataset.get(position).time);


        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////making toast for student prolem
                Toast.makeText(view.getContext(),mDataset.get(position).problem,Toast.LENGTH_LONG).show();

            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ////rejected request by put in status atrbute #2
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                builder.setMessage("هل يوجد سبب (اختياري)");
                reason = new EditText(view.getContext());
                builder.setView(reason);
                builder.setPositiveButton("رفض", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> docData = new HashMap<>();
                        String txt  = reason.getText().toString();
                        updateRequest.update("status","2");
                        docData.put("reason",txt);
                        updateRequest.set(docData,SetOptions.merge());
                        Toast.makeText(view.getContext(),"تم رفض الطلب",Toast.LENGTH_LONG).show();
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position);
                        dd.collection("student").whereEqualTo("studentID" ,studentID)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot q : task.getResult()){
                                    Student student = q.toObject(Student.class);
                                    HashMap<String  , String>notify = new HashMap<>();
                                    notify.put("tokenid" , student.token);
                                    notify.put("title" , "تم الغاء طلبك");
                                    notify.put("body" , "تم الغاء طلب موعدك المرسل " );
                                    dd.collection("notification").add(notify);

                                }
                            }
                        });


                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ////accpted request by put in status atrbute #1
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                builder.setMessage("هل انت متأكد");
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateRequest.update("status","1");
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position);
                        Toast.makeText(view.getContext(), "تم قبول الطلب", Toast.LENGTH_SHORT).show();
                        dd.collection("student").whereEqualTo("studentID" , studentID)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot q : task.getResult()){
                                        Student student = q.toObject(Student.class);
                                        HashMap<String  , String>notify = new HashMap<>();
                                        notify.put("tokenid" , student.token);
                                        notify.put("title" , "تم قبول طلبك");
                                        notify.put("body" , "تم قبول طلب موعدك المرسل");
                                        dd.collection("notification").add(notify);

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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}