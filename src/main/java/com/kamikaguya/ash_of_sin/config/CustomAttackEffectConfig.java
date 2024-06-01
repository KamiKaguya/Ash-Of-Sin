package com.kamikaguya.ash_of_sin.config;

public class CustomAttackEffectConfig {
    private String effect;
    private int duration;
    private int amplifier;

    public CustomAttackEffectConfig(String effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public String getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
