package com.github.tenx.tecnoesis20.ui.main.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.TeamBody;
import com.github.tenx.tecnoesis20.ui.main.about.models.BaseItem;
import com.github.tenx.tecnoesis20.ui.main.about.models.HeaderItem;
import com.github.tenx.tecnoesis20.ui.main.about.models.MemberItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {





    public static final int TYPE_HEADER = 0;
    public static final int TYPE_MEMBER = 1;

    private Context context;
    private List<BaseItem> baseList;

    public void setData(List<TeamBody> data) {
        this.baseList.clear();
        for(TeamBody m : data){
            this.baseList.add(new HeaderItem(m.getName()));
            this.baseList.add(new MemberItem(m.getMembers()));
        }

        notifyDataSetChanged();
    }



    public TeamsAdapter(Context context) {
        this.context = context;
        baseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_HEADER)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_team_header, parent, false);
            return new HeaderViewHolder(v);
        }

        else
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_team_baseitem, parent, false);
            return  new MembersViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position) == TYPE_HEADER){
                HeaderViewHolder headerVh = (HeaderViewHolder) holder;
                HeaderItem currentHeader = (HeaderItem) baseList.get(position);
                headerVh.tvTeamHeader.setText(currentHeader.getName());
             }else {
                MembersViewHolder membersVh = (MembersViewHolder) holder;
                MemberItem currentMembers = (MemberItem) baseList.get(position);
                MembersAdapter adapter = new MembersAdapter(context);
                adapter.setList(currentMembers.getMembers());

                if(currentMembers.getMembers().size() == 1){
//                    if single member center the content horizontally
                    membersVh.recyclerMembers.setLayoutManager(new LinearLayoutManager(context));
                }else {
                    membersVh.recyclerMembers.setLayoutManager(new GridLayoutManager(context, 2));
                }


                membersVh.recyclerMembers.setAdapter(adapter);

            }
    }

    @Override
    public int getItemCount() {
        return baseList == null ? 0 : baseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return baseList.get(position).getType();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_team_header)
        TextView tvTeamHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MembersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_team_member_grid)
        RecyclerView recyclerMembers;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
