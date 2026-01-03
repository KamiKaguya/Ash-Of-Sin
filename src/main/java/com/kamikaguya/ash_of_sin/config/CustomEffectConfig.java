package com.kamikaguya.ash_of_sin.config;

public class CustomEffectConfig {
    public final String effect;
    public final String duration;
    public final int amplifier;

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