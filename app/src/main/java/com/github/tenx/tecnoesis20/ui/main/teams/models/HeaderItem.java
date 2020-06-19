package com.github.tenx.tecnoesis20.ui.main.teams.models;

import com.github.tenx.tecnoesis20.ui.main.teams.TeamsAdapter;

public class HeaderItem  extends BaseItem{

    private String name;

    public HeaderItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
   public int getType() {
        return TeamsAdapter.TYPE_HEADER;
    }
}
