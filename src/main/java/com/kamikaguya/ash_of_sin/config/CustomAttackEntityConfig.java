package com.kamikaguya.ash_of_sin.config;

import java.util.List;

public class CustomAttackEntityConfig {
    public final String entity;
    public final List<CustomAttackEffectConfig> effect;

    public CustomAttackEntityConfig(String entity, List<CustomAttackEffectConfig> effect) {
        this.entity = entity;
        this.effect = effect;
    }

    public String getEntity() {
        return this.entity;
    }

    public List<CustomAttackEffectConfig> getEffect() {
        return this.effect;
    }
}