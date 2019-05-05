package MyAdapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactapp.R;
import com.example.contactapp.contactProfile_clicked;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model_list.ListItem_contacts;

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder>{
    private List<ListItem_contacts> listContacts;

    private Context context;

    public Myadapter(List<ListItem_contacts> listContacts, Context context) {
        this.listContacts = listContacts;
        this.context = context;
    }

    @NonNull
    @Override




    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_recycle,parent,false);
        return new ViewHolder(v,context,listContacts);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myadapter.ViewHolder holder, int position) {
        ListItem_contacts listItem_contacts = listContacts.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/CONSOLAB.TTF");
        Typeface typeface_gothic = Typeface.createFromAsset(context.getAssets(), "fonts/Century Gothic.ttf");
        Typeface typeface_roboto = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.textView_contact_name.setTypeface(typeface_roboto);


        holder.textView_contact_name.setText(listItem_contacts.getName());

        holder.donor_dp.setImageURI(listItem_contacts.getImg_uri());

            if(holder.donor_dp.getDrawable()==null)
            {
                holder.donor_dp.setImageResource(R.drawable.man);
            }

    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }




            public static class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView_contact_name;
        private CircleImageView donor_dp;


        List<ListItem_contacts> listContacts = new ArrayList<ListItem_contacts>();
        Context ctx;

        public ViewHolder(View itemView, Context ctx, List<ListItem_contacts> listItems) {
            super(itemView);
            this.listContacts=listItems;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            textView_contact_name=(TextView)itemView.findViewById(R.id.contact_name);
            donor_dp = itemView.findViewById(R.id.contact_dp);



        }

        



        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ListItem_contacts listItem= this.listContacts.get(position);
            Intent i = new Intent(this.ctx, contactProfile_clicked.class);
            i.putExtra("name",listItem.getName());
            i.putExtra("id",listItem.getID());
            i.putExtra("photo",listItem.getImg_uri().toString());

            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(this.ctx, R.anim.slide_in_right, R.anim.slide_out_left);
            this.ctx.startActivity(i, options.toBundle());





        }


    }
}

