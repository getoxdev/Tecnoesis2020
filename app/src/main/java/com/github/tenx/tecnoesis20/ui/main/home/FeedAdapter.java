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
import com.github.tenx.tecnoesis20.data.models.FeedBody;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CustomViewHolder> {


    private Context context;
    private List<FeedBody> list;

    public FeedAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<FeedBody> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_feed, parent, false);

        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        FeedBody currentData = list.get(position);

        holder.tvFeedText.setText(currentData.getText());
        Glide.with(context).load(currentData.getImage()).placeholder(R.drawable.placeholder_image).into(holder.ivFeedImage);
        holder.ivFeedImage.setOnClickListener(v -> {
            List<String> images= new ArrayList<>();
            images.add(currentData.getImage());

            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, images)
                    .setStartPosition(0).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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
