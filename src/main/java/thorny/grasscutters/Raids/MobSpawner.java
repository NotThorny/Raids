package thorny.grasscutters.Raids;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import emu.grasscutter.data.GameData;
import emu.grasscutter.data.excels.monster.MonsterData;
import emu.grasscutter.game.entity.EntityGadget;
import emu.grasscutter.game.entity.EntityMonster;
import emu.grasscutter.game.entity.GameEntity;
import emu.grasscutter.game.entity.gadget.GadgetWorktop;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.game.world.Position;
import emu.grasscutter.scripts.data.SceneBossChest;
import emu.grasscutter.scripts.data.SceneGadget;
import emu.grasscutter.scripts.data.SceneMonster;
import thorny.grasscutters.Raids.commands.RaidsCommand;
import thorny.grasscutters.Raids.utils.Bosses;

public class MobSpawner {
    // Set groupId for new monsters
    public static void setMonsters(List<EntityMonster> monsters) {
        for (EntityMonster monster : monsters) {
            monster.setGroupId(800815);
        } // for
    } // setMonsters

    // Spawn the monsters
    public static void spawnMobEntity(Player targetPlayer) {
        //newMonsters.clear();
        var scene = targetPlayer.getScene();
        HashSet<Integer> activeGroups = new HashSet<Integer>(scene.getPlayerActiveGroups(targetPlayer));
        var acList = scene.getEntities().values().stream().filter((e) -> e.getGroupId() == 800815).toList();

        // Get current location
        for (Bosses boss : RaidsCommand.config.getBosses()) {
            boolean alreadySpawned = false;
            List<Integer> bossGroups = new LinkedList<>(boss.getGroups());
            bossGroups.retainAll(activeGroups);
            if (boss.getArea() == targetPlayer.getAreaId() || !bossGroups.isEmpty() && acList.size() > 0) {
                // Check if boss still exists
                for (GameEntity gameEntity : acList) {
                    if (gameEntity.getConfigId() == boss.getId() && gameEntity.isAlive()) {
                        alreadySpawned = true;
                        break;
                    }
                }

                if (alreadySpawned) {
                    continue;
                }

                Position pos = new Position(boss.getPos());
                Position rot = new Position(boss.getRot());

                MonsterData monsterData = GameData.getMonsterDataMap().get(boss.getId());

                // Add to scene
                for (int i = 0; i < boss.getParam().amount; i++) {
                    EntityMonster entity = new EntityMonster(scene, monsterData, pos, rot,
                        boss.getParam().lvl);
                    RaidsCommand.applyCommonParameters(entity, boss.getParam());
                    entity.setGroupId(800815);
                    entity.setAiId(27001005);

                    // Set config id
                    entity.setConfigId(entity.getMonsterData().getId());
                    scene.addEntity(entity);

                }
            }
        }
    } // if

    public static void raidReward(GameEntity monster) {
        // Create gadget
        EntityGadget gadget = new EntityGadget(monster.getScene(), 70360056, monster.getPosition(),
                monster.getRotation());

        // Setup scene monster
        SceneMonster boss2 = new SceneMonster();
        boss2.setMonster_id(monster.getId());

        // Init map
        Map<Integer, SceneMonster> map = Map.of(0, boss2);

        // Set up flower
        RaidsCommand.mobSG.setMonsters(map);
        gadget.buildContent();
        gadget.setState(201);
        GadgetWorktop gadgetWorktop = (GadgetWorktop) gadget.getContent();
        gadgetWorktop.addWorktopOptions(new int[] { 187 });
        EntityGadget rewardGadget = new EntityGadget(gadget.getScene(), 70210109, gadget.getPosition());
        SceneGadget metaGadget = new SceneGadget();
        metaGadget.boss_chest = new SceneBossChest();
        metaGadget.boss_chest.resin = 0;
        metaGadget.setGroup(RaidsCommand.mobSG);

        rewardGadget.setFightProperty(FightProperty.FIGHT_PROP_BASE_HP, Float.POSITIVE_INFINITY);
        rewardGadget.setFightProperty(FightProperty.FIGHT_PROP_CUR_HP, Float.POSITIVE_INFINITY);
        rewardGadget.setFightProperty(FightProperty.FIGHT_PROP_MAX_HP, Float.POSITIVE_INFINITY);
        rewardGadget.setMetaGadget(metaGadget);
        rewardGadget.setGroupId(69420); // Maybe redundant
        rewardGadget.setConfigId(monster.getConfigId()); // Maybe redundant
        rewardGadget.buildContent();
        EntityGadget chest = rewardGadget;
        chest.setGroupId(69420);
        chest.setConfigId(monster.getConfigId());

        // Spawn reward
        monster.getScene().addEntity(chest);
    }
} // spawnMobEntity
