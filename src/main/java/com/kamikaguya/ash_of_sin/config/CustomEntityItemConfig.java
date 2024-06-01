package com.kamikaguya.ash_of_sin.config;

import java.util.List;

public class CustomEntityItemConfig {
    private List<String> entity;
    private CustomItemConfig item;

    public List<String> getEntity() {
        return entity;
    }

    public void setEntity(List<String> entity) {
        this.entity = entity;
    }

    public CustomItemConfig getItem() {
        return item;
    }

    public void setItem(CustomItemConfig item) {
        this.item = item;
    }
}