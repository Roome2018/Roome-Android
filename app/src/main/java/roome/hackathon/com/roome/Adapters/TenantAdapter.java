package roome.hackathon.com.roome.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import roome.hackathon.com.roome.Models.Tenant;
import roome.hackathon.com.roome.R;


public class TenantAdapter extends RecyclerView.Adapter<TenantAdapter.MyViewHolder> {

    private final List<Tenant> tenantList;
    private final Activity context;
    private Tenant message;

    public TenantAdapter(Activity context, List<Tenant> tenantList) {
        this.tenantList = tenantList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (tenantList == null) {
            return;
        }
        if (tenantList.get(position).getUrlimage() != null) {
            holder.user_single_image.setImageURI(Uri.parse(tenantList.get(position).getUrlimage()));
        }
        holder.user_single_name.setText(tenantList.get(position).getName());
        holder.user_nati.setText(tenantList.get(position).getNationality() == null ? "" : tenantList.get(position).getNationality());
        holder.ic_arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tenantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView user_single_image;
        private final TextView user_single_name;
        private final TextView user_nati;
        private final ImageView ic_arrow_right;

        public MyViewHolder(View itemView) {
            super(itemView);
            user_single_image = itemView.findViewById(R.id.user_single_image);
            user_single_name = itemView.findViewById(R.id.user_single_name);
            user_nati = itemView.findViewById(R.id.user_nati);
            ic_arrow_right = itemView.findViewById(R.id.ic_arrow_right);
        }
    }
}
