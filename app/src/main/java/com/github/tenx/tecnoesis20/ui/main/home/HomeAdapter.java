package com.github.tenx.tecnoesis20.ui.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.github.tenx.tecnoesis20.data.models.HomeEventBody;
import com.google.android.material.button.MaterialButton;
import com.stfalcon.frescoimageviewer.ImageViewer;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ImageViewHOlder> {

    private Context context;
    private List<HomeEventBody> hlist;

    private View overlayView;

    public HomeAdapter(Context tcontext) {
        this.context = tcontext;
        this.hlist =  new ArrayList<>();
    }


    public void setHlist(List<HomeEventBody> hlist) {
        this.hlist = hlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_home_events,parent,false);

        return new ImageViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHOlder holder, final int position) {
        HomeEventBody currentData = hlist.get(position);

        holder.tvName.setText(currentData.getName());
        holder.tvDescription.setText(currentData.getDescription());
        Glide.with(context).load(currentData.getImage()).placeholder(R.drawable.placeholder_image).into(holder.ivImage);
        holder.ivImage.setOnClickListener(v -> {

            overlayView  = LayoutInflater.from(context).inflate(R.layout.overlay_image, null, false);

            TextView tvOverlayTitle = overlayView.findViewById(R.id.tv_overlay_title);
            TextView tvOverlayDesc = overlayView.findViewById(R.id.tv_overlay_description);
            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, hlist).setFormatter((ImageViewer.Formatter<HomeEventBody>) HomeEventBody::getImage).setOverlayView(overlayView).setImageChangeListener(pos -> {
                tvOverlayDesc.setText(hlist.get(pos).getDescription());
                tvOverlayTitle.setText(hlist.get(pos).getName());
            })
                    .setStartPosition(position).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                    .show();
        });

        String website = currentData.getWebsite();
        if(website.equals("")){
            holder.btnWebsite.setVisibility(View.GONE);
        }else {
            holder.btnWebsite.setOnClickListener(v-> {
//            go to url
                openUrl(website , context);
            });
        }



    }

    @Override
    public int getItemCount()
    {
        return hlist == null ? 0 : hlist.size();
    }

    public  class ImageViewHOlder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_home_event_image)
        ImageView ivImage;

        @BindView(R.id.tv_home_module_title)
        TextView tvName;
        @BindView(R.id.tv_home_description)
        TextView tvDescription;

        @BindView(R.id.btn_home_recycler_website)
        MaterialButton btnWebsite;

        public ImageViewHOlder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }


    private void openUrl(String url, Context context){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}



