package com.kamikaguya.ash_of_sin.server.commands;

import com.kamikaguya.ash_of_sin.config.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;

public class AshOfSinConfigReloadCommand {

    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("reload")
                                        .executes(context -> reloadConfig(context.getSource()))
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("Main")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadMainConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomAntiEnchantmentEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomAntiEnchantmentEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomAntiSeatEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomAntiSeatEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomAntiItemEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomAntiItemEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomEntityAntiEffect")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomEntityAntiEffectConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomEntityEffect")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomEntityEffectConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomEntityItem")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomEntityItemConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomEntityAttackEffect")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomEntityAttackEffectConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("AntiHighLevelEnchantment")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadAntiHighLevelEnchantmentConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("EternalEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadEternalEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("SoulLikeBossBattle")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadSoulLikeBossBattleConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CusomAntiHighATKEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomAntiHighATKEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("BetterAI")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadBetterAIConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("CustomAntiTrapCageEntity")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadCustomAntiTrapCageEntityConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("AntiSameModifier")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadAntiSameModifierConfig(context.getSource()))
                                        )
                                )
                        )
        );

        event.getDispatcher().register(
                Commands.literal("ashofsin")
                        .then(Commands.literal("config")
                                .then(Commands.literal("AdventureDimension")
                                        .then(Commands.literal("reload")
                                                .executes(context -> reloadAdventureDimensionConfig(context.getSource()))
                                        )
                                )
                        )
        );

    }

    private static final AshOfSinConfig ashOfSinConfig = new AshOfSinConfig();
    private static final CustomAntiEnchantmentEntityConfig customAntiEnchantmentEntityConfig = new CustomAntiEnchantmentEntityConfig();
    private static final CustomAntiSeatEntityConfig customAntiSeatEntityConfig = new CustomAntiSeatEntityConfig();
    private static final CustomAntiItemEntityConfig customAntiItemEntityConfig = new CustomAntiItemEntityConfig();
    private static final CustomEntityAntiEffectConfig customEntityAntiEffectConfig = new CustomEntityAntiEffectConfig();
    private static final CustomAntiHighATKEntityConfig customAntiHighATKEntityConfig = new CustomAntiHighATKEntityConfig();
    private static final CustomAntiTrapCageEntityConfig customAntiTrapCageEntityConfig = new CustomAntiTrapCageEntityConfig();
    private static final CustomEntityEffectConfigManager customEntityEffectConfigManager = new CustomEntityEffectConfigManager();
    private static final CustomEntityItemConfigManager customEntityItemConfigManager = new CustomEntityItemConfigManager();
    private static final CustomEntityAttackEffectConfig customEntityAttackEffectConfig = new CustomEntityAttackEffectConfig();
    private static final AntiHighLevelEnchantmentConfig antiHighLevelEnchantmentConfig = new AntiHighLevelEnchantmentConfig();
    private static final EternalEntityConfig eternalEntityConfig = new EternalEntityConfig();
    private static final SoulLikeBossBattleConfig soulLikeBossBattleConfig = new SoulLikeBossBattleConfig();
    private static final BetterAIConfig betterAIConfig = new BetterAIConfig();
    private static final AntiSameModifierConfig antiSameModifierConfig = new AntiSameModifierConfig();
    private static final AdventureDimensionConfig adventureDimensionConfig = new AdventureDimensionConfig();

    private static int reloadConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            ashOfSinConfig.loadConfig();
            customAntiEnchantmentEntityConfig.loadConfig();
            customAntiSeatEntityConfig.loadConfig();
            customAntiItemEntityConfig.loadConfig();
            customEntityAntiEffectConfig.loadConfig();
            customAntiHighATKEntityConfig.loadConfig();
            customAntiTrapCageEntityConfig.loadConfig();
            customEntityEffectConfigManager.loadConfig();
            customEntityItemConfigManager.loadConfig();
            customEntityAttackEffectConfig.loadConfig();
            antiHighLevelEnchantmentConfig.loadConfig();
            eternalEntityConfig.loadConfig();
            soulLikeBossBattleConfig.loadConfig();
            betterAIConfig.loadConfig();
            antiSameModifierConfig.loadConfig();
            adventureDimensionConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadMainConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            ashOfSinConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬主配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomAntiEnchantmentEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customAntiEnchantmentEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义反附魔实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomAntiSeatEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customAntiSeatEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义反坐垫实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomAntiItemEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customAntiItemEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义反物品实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomEntityAntiEffectConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customEntityAntiEffectConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义实体反状态效果配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomEntityEffectConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customEntityEffectConfigManager.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义实体状态效果配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomEntityItemConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customEntityItemConfigManager.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义实体物品配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomEntityAttackEffectConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customEntityAttackEffectConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义实体攻击状态效果配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadAntiHighLevelEnchantmentConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            antiHighLevelEnchantmentConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬反高等级附魔配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadEternalEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            eternalEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬永恒实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadSoulLikeBossBattleConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            soulLikeBossBattleConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬类魂BOSS战配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomAntiHighATKEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customAntiHighATKEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬反高攻击力实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadBetterAIConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            betterAIConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬更好的AI配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadCustomAntiTrapCageEntityConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            customAntiTrapCageEntityConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬自定义反捕捉笼实体配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadAntiSameModifierConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            antiSameModifierConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬反重复修饰符配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }

    private static int reloadAdventureDimensionConfig( CommandSourceStack source) {
        if (source.hasPermission(4)) {
            adventureDimensionConfig.loadConfig();
            source.sendSuccess(new TextComponent("罪业余烬冒险维度配置文件已重新加载。"), false);
            return 1;
        } else {
            source.sendFailure(new TextComponent("你没有权限执行此命令。"));
            return 0;
        }
    }
}
