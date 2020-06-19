package com.github.tenx.tecnoesis20.ui.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.SponsorBody;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.ImageViewHOlder> {

    private Context context;
    private List<SponsorBody> hlist;

    public SponsorsAdapter(Context tcontext) {
        this.context = tcontext;
        this.hlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sponsors_item,parent,false);
        return new ImageViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHOlder holder, final int position) {
       Glide.with(context).load(hlist.get(position).getImage()).placeholder(R.drawable.placeholder_image).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {

            View overlayView  = LayoutInflater.from(context).inflate(R.layout.overlay_image, null, false);
            TextView tvTitle = overlayView.findViewById(R.id.tv_overlay_title);
            TextView tvDesc = overlayView.findViewById(R.id.tv_overlay_description);

            tvTitle.setText("OUR PARTNERS");


            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, hlist).setFormatter(o -> ((SponsorBody) o).getImage()).setImageChangeListener(pos -> {
                tvDesc.setText(hlist.get(pos).getDescription());
            })
                    .setStartPosition(position).setCustomDraweeHierarchyBuilder(hierarchyBuilder).setOverlayView(overlayView)
                    .show();
        });

    }

    @Override
    public int getItemCount()
    {
        return hlist == null ? 0 :  hlist.size();
    }

    public void setHlist(List<SponsorBody> hlist) {
        this.hlist = hlist;
        notifyDataSetChanged();
    }

    public  class ImageViewHOlder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_sponsors_home_events)
        ImageView imageView;


        public ImageViewHOlder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
