package com.kamikaguya.ash_of_sin.config;

import java.util.List;

public class CustomEntityItemConfig {
    public List<String> entity;
    public CustomItemConfig item;

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