package com.kamikaguya.ash_of_sin.client;

import com.kamikaguya.ash_of_sin.CommonProxy;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AshOfSin.MODID)
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void clientInit() {
        super.clientInit();
        AshOfSinClientSetup.clientInit();
    }
}
