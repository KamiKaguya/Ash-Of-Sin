package com.kamikaguya.ash_of_sin.config;

import java.util.List;

public class CustomEntityEffectConfig {
    private String entity;
    private List<CustomEffectConfig> effect;

    public CustomEntityEffectConfig(String entity, List<CustomEffectConfig> effect) {
        this.entity = entity;
        this.effect = effect;
    }

    public String getEntity() {
        return this.entity;
    }

    public List<CustomEffectConfig> getEffect() {
        return this.effect;
    }
}