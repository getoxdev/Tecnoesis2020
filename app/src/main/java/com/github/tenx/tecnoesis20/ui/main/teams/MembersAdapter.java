package com.github.tenx.tecnoesis20.ui.main.teams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.TeamBody;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.CustomViewHolder> {

    private Context context;
    private List<TeamBody.MemberBody> list;

    public MembersAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_member, parent, false);

        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        TeamBody.MemberBody currentData = list.get(position);

        Glide.with(context).load(currentData.getImage()).placeholder(R.drawable.placeholder_image).into(holder.ivTeamMemberImage);
        holder.ivTeamMemberImage.setOnClickListener(v -> {
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

        holder.tvTeamMemberDesignation.setText(currentData.getDesignation());
        holder.tvTeamMemberName.setText(currentData.getName());

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<TeamBody.MemberBody> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_team_member_image)
        CircleImageView ivTeamMemberImage;
        @BindView(R.id.tv_team_member_name)
        TextView tvTeamMemberName;
        @BindView(R.id.tv_team_member_designation)
        TextView tvTeamMemberDesignation;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
