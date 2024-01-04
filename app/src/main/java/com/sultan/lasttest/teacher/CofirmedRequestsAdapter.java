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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.request;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CofirmedRequestsAdapter extends RecyclerView.Adapter<CofirmedRequestsAdapter.MyViewHolder> {
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
        public CardView  cancel;
        public MyViewHolder(CardView v,TextView s,TextView t , TextView d ,CardView re ) {
            super(v);

            this.studentID = s;
            this.date = d;
            this.time=t;
            textView = v;

            cancel=re;

        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CofirmedRequestsAdapter(List<request> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CofirmedRequestsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancle_request,parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView studentid = (TextView) v.findViewById(R.id.txtstudntid);
        TextView time = (TextView) v.findViewById(R.id.txttime1 );
        TextView date = (TextView) v.findViewById(R.id.txtdate1);

        CardView cancel = (CardView) v.findViewById(R.id.buttonCancel) ;


        MyViewHolder vh = new MyViewHolder(v,studentid,time,date,cancel);



        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element




        FirebaseFirestore dd = FirebaseFirestore.getInstance();
        final DocumentReference updateRequest = dd.collection("request").document(mDataset.get(position).ID);


        try {
            Date s = new SimpleDateFormat("dd/MM/yyyy").parse(mDataset.get(position).date);
            String x ="EEE yyyy/MM/dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(x);
            String date = simpleDateFormat.format(s);
            holder.date.setText("التاريخ: "+date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.studentID.setText("معرف الطالب: " + mDataset.get(position).studentID);
        holder.time.setText("الوقت: " + mDataset.get(position).time);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                builder.setMessage("هل يوجد سبب (اختياري)");
                reason = new EditText(view.getContext());
                builder.setView(reason);
                builder.setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> docData = new HashMap<>();
                        String txt  = reason.getText().toString();
                        updateRequest.update("status","5");
                        docData.put("reason",txt);
                        updateRequest.set(docData,SetOptions.merge());
                        Toast.makeText(view.getContext(),"تم إلغاء الموعد",Toast.LENGTH_LONG).show();
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position);

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