package com.sultan.lasttest.teacher;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sultan.lasttest.MainActivity;
import com.sultan.lasttest.R;
import com.sultan.lasttest.database.request;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> implements Filterable {
    private List<request> mDataset;
    private List<request> fullmDataset;
    public final String TAG = "TeacherAdapter";
    String  status , reason , date , student;

    final String reqstat = "حالة الطلب: " ;
    final String reqdate = "تاريخ الطلب: " ;
    final String reqsreson = "سبب الرفض: " ;
    final String reqstudent = "معرف الطالب: " ;





    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public CardView textView;
        public TextView studentID , reqdate ;
        Button review;
        public MyViewHolder(CardView v,TextView studentID , TextView reqdate ,Button review) {
            super(v);
            textView = v;
            this.studentID = studentID;
            this.reqdate =reqdate;
            this.review=review;


        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public TeacherAdapter(List<request> myDataset ) {

        mDataset = myDataset;
        fullmDataset = new ArrayList<>(myDataset);


    }

    // Create new views (invoked by the layout manager)
    @Override
    public TeacherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_status_teacher, parent, false);
        //TextView corseinfo = (TextView) findViewById(R.id._course_info);
        TextView studentID = (TextView) v.findViewById(R.id.studentidrequest);
        TextView reqdate = (TextView) v.findViewById(R.id.requestdate);
        Button review = (Button) v.findViewById(R.id.btnReview);
        MyViewHolder vh = new MyViewHolder(v,studentID,reqdate,review);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = mDataset.get(position).status;
                reason =mDataset.get(position).reason;
                date=mDataset.get(position).date;
                student = mDataset.get(position).studentID;


                if(mDataset.get(position).status.equals("2"))
                {
                    if(!mDataset.get(position).reason.equals(""))
                    {
                        reason = mDataset.get(position).reason;
                    }

                    else {
                        reason = "لا يوجد";

                    }

                }


                else
                    reason ="";


                if(mDataset.get(position).equals("0"))
                    status ="قيد الانتظار";
                else if (mDataset.get(position).status.equals("2"))
                    status ="تم الرفض";
                else if(mDataset.get(position).status.equals("3"))
                    status ="ماضي";
                else
                    status="تمت الموافقه";
                 androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("...");
                View itemView = LayoutInflater.from(view.getContext()).inflate(R.layout.requests_status_view, null);
                TextView r = (TextView)itemView.findViewById(R.id.txtrequestsStatus) ;

                if(mDataset.get(position).status.equals("2"))
                    r.setText(reqstudent +  student + "\n"+reqdate + date +"\n"+reqstat +status +"\n"+ reqsreson +reason );
                else
                    r.setText(reqstudent +  student + "\n"+reqdate + date +"\n"+reqstat +status );
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





        holder.studentID.setText(mDataset.get(position).studentID);


        holder.reqdate.setText(mDataset.get(position).date);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public Filter getFilter(){
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<request> filterredlist = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filterredlist.addAll(fullmDataset);
            }else{
                String fliterpattern = constraint.toString().toLowerCase().trim();
                for(request r : fullmDataset){
                    if(r.studentID.toLowerCase().contains(fliterpattern)){
                        filterredlist.add(r);
                    }
                }
            }
            FilterResults result = new FilterResults();
            result.values =filterredlist;
            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mDataset.clear();
            mDataset.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
}