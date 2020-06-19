package com.github.tenx.tecnoesis20.ui.main.teams.models;

import com.github.tenx.tecnoesis20.data.models.TeamBody;
import com.github.tenx.tecnoesis20.ui.main.teams.TeamsAdapter;

import java.util.List;

public class MemberItem extends BaseItem {

    private List<TeamBody.MemberBody> members;

    public MemberItem(List<TeamBody.MemberBody> members) {
        this.members = members;
    }

    public List<TeamBody.MemberBody> getMembers() {
        return members;
    }

    @Override
    public int getType() {
        return TeamsAdapter.TYPE_MEMBER;
    }
}
