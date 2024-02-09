package thorny.grasscutters.Raids.commands;

import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.command.Command.TargetRequirement;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.Command;

import static emu.grasscutter.command.CommandHelpers.*;
import emu.grasscutter.game.entity.EntityMonster;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.scripts.data.SceneGroup;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import lombok.Setter;
import thorny.grasscutters.Raids.MobSpawner;
import thorny.grasscutters.Raids.Raids;
import thorny.grasscutters.Raids.utils.Bosses;
import thorny.grasscutters.Raids.utils.Config;

import java.util.*;

// Command usage
@Command(label = "raids", aliases = "rd", usage = "start|new <mobId> x[amount] lv[level] hp[hp] atk[attack] def[defense] maxhp[maxhp] hx[hp multiplier]"
        +
        "\n\n ALL FIELDS OPTIONAL - Do not include any that you do not wish to set custom values for! \n Level will default to 200, the rest to the monster's default values.", targetRequirement = TargetRequirement.PLAYER)
public class RaidsCommand implements CommandHandler {

    // Config
    public static final Config config = Raids.getInstance().config.getConfig();

    // Patterns
    public static final Pattern timesHPRegex = Pattern.compile("hx(\\d+)");

    // Lists
    List<EntityMonster> monsters = new ArrayList<EntityMonster>(); // Mob entities
    public static SpawnParameters param = new SpawnParameters(); // Custom parameters
    public static SceneGroup mobSG = new SceneGroup(); // Mobs scenegroup

    // Defaults
    public static int areaId = 0;

    // Taken from SpawnCommand.java with edits made to match 'create'
    private static final Map<Pattern, BiConsumer<SpawnParameters, Integer>> intCommandHandlers = Map.ofEntries(
            Map.entry(lvlRegex, SpawnParameters::setLvl),
            Map.entry(amountRegex, SpawnParameters::setAmount),
            Map.entry(maxHPRegex, SpawnParameters::setMaxHP),
            Map.entry(hpRegex, SpawnParameters::setHp),
            Map.entry(timesHPRegex, SpawnParameters::setTiHP), // Times hp
            Map.entry(defRegex, SpawnParameters::setDef),
            Map.entry(atkRegex, SpawnParameters::setAtk));

    @Override
    public void execute(Player sender, Player targetPlayer, List<String> args) {
        if (args.isEmpty()) {
            sendUsageMessage(targetPlayer);
            return;
        }
        String state = args.get(0);
        // Wave and mob default settings
        param = new SpawnParameters();
        mobSG.id = 800815; // Scenegroup id
        int bossId = -1;

        switch (state) {
            case ("start") ->
                MobSpawner.spawnMobEntity(targetPlayer);
            case ("new") -> {
                // Get user params
                parseIntParameters(args, param, intCommandHandlers);
                var scene = targetPlayer.getScene();

                int cLevel = param.lvl;
                // Make sure valid arguments
                if (cLevel < 0 || cLevel > 200) {
                    param.lvl = 200;
                } // if

                if (config.getBosses() != null) {
                    try {
                        bossId = Integer.parseInt(args.get(1));
                    } catch (NumberFormatException e) {
                        this.sendUsageMessage(targetPlayer);
                    }
                    for (Bosses boss : config.getBosses()) {
                        if (boss.getId() == bossId) {

                            // Save stats
                            boss.setParam(param);
                            boss.setArea(targetPlayer.getAreaId());
                            boss.setGroups(scene.getPlayerActiveGroups(targetPlayer));
                            var pos = targetPlayer.getPosition().toString();
                            var rot = targetPlayer.getRotation().toString();
                            boss.setPos(pos.substring(1, pos.length() - 1));
                            boss.setRot(rot.substring(1, rot.length() - 1));

                            if (updateConfig()) {
                                CommandHandler.sendMessage(targetPlayer, bossId + " updated!");
                                return;
                            }
                        }
                    }
                }

                Bosses newBoss = new Bosses();
                newBoss.setId(bossId);
                newBoss.setArea(targetPlayer.getAreaId());
                newBoss.setGroups(scene.getPlayerActiveGroups(targetPlayer));
                newBoss.setParam(param);
                var pos = targetPlayer.getPosition().toString();
                var rot = targetPlayer.getRotation().toString();
                newBoss.setPos(pos.substring(1, pos.length() - 1));
                newBoss.setRot(rot.substring(1, rot.length() - 1));

                config.getBosses().add(newBoss);

                // Update config file
                if (updateConfig()) {
                    CommandHandler.sendMessage(targetPlayer, "Saved new boss " + bossId);
                } else {
                    CommandHandler.sendMessage(targetPlayer, "Failed to save boss!! :(");
                }
                MobSpawner.spawnMobEntity(targetPlayer);
            }
            default -> this.sendUsageMessage(targetPlayer);
        }
    }

    // Return for event listener
    public static int getMobSceneGroup() {
        return 800815;
    } // getMobSceneGroup

    // Taken from SpawnCommand.java
    public static class SpawnParameters {
        @Setter
        public int lvl = 200;
        @Setter
        public int amount = 1;
        @Setter
        public int hp = -1;
        @Setter
        public int maxHP = -1;
        @Setter
        public int tiHP = -1;
        @Setter
        public int atk = -1;
        @Setter
        public int def = -1;
    }

    // Taken from SpawnCommand.java
    public static void applyCommonParameters(EntityMonster entity, SpawnParameters param) {
        if (param.maxHP != -1) {
            entity.setFightProperty(FightProperty.FIGHT_PROP_MAX_HP, param.maxHP);
            entity.setFightProperty(FightProperty.FIGHT_PROP_BASE_HP, param.maxHP);
        }
        if (param.hp != -1) {
            entity.setFightProperty(FightProperty.FIGHT_PROP_CUR_HP, param.hp == 0 ? Float.MAX_VALUE : param.hp);
        }
        if (param.atk != -1) {
            entity.setFightProperty(FightProperty.FIGHT_PROP_ATTACK, param.atk);
            entity.setFightProperty(FightProperty.FIGHT_PROP_CUR_ATTACK, param.atk);
        }
        if (param.def != -1) {
            entity.setFightProperty(FightProperty.FIGHT_PROP_DEFENSE, param.def);
            entity.setFightProperty(FightProperty.FIGHT_PROP_CUR_DEFENSE, param.def);
        }
        if (param.tiHP != -1) {
            // Increase hp by provided multiplier
            entity.setFightProperty(
                    FightProperty.FIGHT_PROP_CUR_HP,
                    (entity.getFightProperty(FightProperty.FIGHT_PROP_CUR_HP) * param.tiHP));
            entity.setFightProperty(
                    FightProperty.FIGHT_PROP_MAX_HP,
                    (entity.getFightProperty(FightProperty.FIGHT_PROP_MAX_HP) * param.tiHP));
        }
    }

    // Update config file, from Grasscutter config writer
    public boolean updateConfig() {
        if ( // Save configuration & reload.
        Raids.getInstance().reloadConfig(config)) {
            return true;
        } else {
            Grasscutter.getLogger().warn("Failed to update config!!");
            return false;
        }
    }
} // RaidCommand
