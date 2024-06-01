package com.kamikaguya.ash_of_sin.config;

public class CustomEffectConfig {
    private String effect;
    private String duration;
    private int amplifier;

    public CustomEffectConfig(String effect, String duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public String getEffect() {
        return effect;
    }

    public String getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
