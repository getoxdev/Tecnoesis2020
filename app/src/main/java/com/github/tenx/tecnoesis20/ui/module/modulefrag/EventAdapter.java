package com.github.tenx.tecnoesis20.ui.module.modulefrag;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.EventBody;
import com.github.tenx.tecnoesis20.data.models.ModuleBody;
import com.google.android.material.button.MaterialButton;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 110;
    private static final int TYPE_ITEM = 111;



    private List<EventBody> listEvents;
    private Context context;
    private ModuleBody moduleBody;
    private EventClickHandler clickHandler;


    public EventAdapter(Context context, ModuleBody moduleBody) {
        this.listEvents = moduleBody.getEvents();
        this.context = context;
        this.moduleBody = moduleBody;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_ITEM)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_event_item, parent, false);
            return new CustomViewHolder(v);
        }
            else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_module_header, parent, false);
            return new CustomHeaderViewHolder(v);
        }

    }

    public void setClickHandler(EventClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(position == 0){
//            0 is header

            CustomHeaderViewHolder headerHolder = (CustomHeaderViewHolder) holder;



            headerHolder.tvModuleDescription.setText(moduleBody.getDescription());
            headerHolder.tvModuleName.setText(moduleBody.getName());
            Glide.with(context).load(moduleBody.getImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    headerHolder.ivModuleImage.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    headerHolder.tvModuleName.setVisibility(View.GONE);
                    return false;
                }
            }).into(headerHolder.ivModuleImage);
            headerHolder.btnWebsite.setOnClickListener(v-> {
                openUrl(moduleBody.getWebsite(), context);
            });
            headerHolder.ivModuleImage.setOnClickListener(v -> {
                List<String> images= new ArrayList<>();
                images.add(moduleBody.getImage());
                View overlayView  = LayoutInflater.from(context).inflate(R.layout.overlay_image, null, false);
                TextView tvTitle = overlayView.findViewById(R.id.tv_overlay_title);
                TextView tvDesc = overlayView.findViewById(R.id.tv_overlay_description);
               tvTitle.setText(moduleBody.getName());
               tvDesc.setText(moduleBody.getDescription());

                GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                        .setFailureImage(R.drawable.placeholder_image)
                        .setProgressBarImage(R.drawable.placeholder_image)
                        .setPlaceholderImage(R.drawable.placeholder_image);
                new ImageViewer.Builder(context, images).setOverlayView(overlayView)
                        .setStartPosition(0).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                        .show();
            });
        }else if(listEvents != null){
           EventBody current = listEvents.get(position-1);
            CustomViewHolder itemholder =  (CustomViewHolder) holder;

            itemholder.tvEventDateLoc.setText(current.getDate());
            itemholder.tvEventDesc.setText(current.getDescription());
            itemholder.tvEventName.setText(current.getName());
            Glide.with(context).load(current.getImage()).into(itemholder.ivEventImage);

            ((CustomViewHolder) holder).llParent.setOnClickListener(v -> {
                clickHandler.onEventClick(position-1);
            });
        }

    }

    @Override
    public int getItemCount() {
        return listEvents == null ? 0 : listEvents.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private void openUrl(String url, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_event_image)
        ImageView ivEventImage;
        @BindView(R.id.tv_event_name)
        TextView tvEventName;
        @BindView(R.id.tv_event_date_loc)
        TextView tvEventDateLoc;
        @BindView(R.id.tv_event_desc)
        TextView tvEventDesc;



        @BindView(R.id.llParent)
        LinearLayout llParent;



        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CustomHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_module_image)
        ImageView ivModuleImage;
        @BindView(R.id.tv_module_name)
        TextView tvModuleName;
        @BindView(R.id.tv_module_description)
        TextView tvModuleDescription;
        @BindView(R.id.btn_module_website)
        MaterialButton btnWebsite;

        public CustomHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
