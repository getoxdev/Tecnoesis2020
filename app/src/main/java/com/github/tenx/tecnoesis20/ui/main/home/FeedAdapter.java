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
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.FeedBody;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends FirebaseRecyclerPagingAdapter<FeedBody, FeedAdapter.CustomViewHolder> {


    private Context context;
    private View overlayView;



    public FeedAdapter(@NonNull DatabasePagingOptions<FeedBody> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull FeedBody model) {

        holder.tvFeedText.setText(model.getText());
        Glide.with(context).load(model.getImage()).placeholder(R.drawable.placeholder_image).into(holder.ivFeedImage);

        LayoutInflater inflater;
        ViewGroup root;
        holder.ivFeedImage.setOnClickListener(v -> {
            overlayView  = LayoutInflater.from(context).inflate(R.layout.overlay_image, null, false);

            TextView tvDesc = overlayView.findViewById(R.id.tv_overlay_description);


            tvDesc.setText(model.getText());

            List<String> images= new ArrayList<>();
            images.add(model.getImage());
            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, images).setOverlayView(overlayView)
                    .setStartPosition(0).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                    .show();
        });
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_feed, parent, false);
         return new CustomViewHolder(v);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_feed_image)
        ImageView ivFeedImage;
        @BindView(R.id.tv_feed_text)
        TextView tvFeedText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
