package thorny.grasscutters.Raids;

import java.util.ArrayList;
import java.util.Collections;
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
import emu.grasscutter.net.proto.VisionTypeOuterClass.VisionType;
import emu.grasscutter.scripts.data.SceneBossChest;
import emu.grasscutter.scripts.data.SceneGadget;
import emu.grasscutter.scripts.data.SceneMonster;
import thorny.grasscutters.Raids.commands.RaidsCommand;
import thorny.grasscutters.Raids.utils.Bosses;

public class MobSpawner {

    // Defaults
    static List<EntityMonster> newMonsters = new ArrayList<>();
    public static List<GameEntity> activeMonsters = new ArrayList<>(); // Current mobs

    // Set groupId for new monsters
    public static void setMonsters(List<EntityMonster> monsters) {
        MobSpawner.activeMonsters.addAll(monsters);
        for (EntityMonster monster : monsters) {
            monster.setGroupId(800815);
        } // for
    } // setMonsters

    // Spawn the monsters
    public static void spawnMobEntity(Player targetPlayer) {
        Bosses bossToSpawn = new Bosses();
        newMonsters.clear();
        var scene = targetPlayer.getScene();

        // Get current location
        for (Bosses boss : RaidsCommand.config.getBosses()) {
            if (boss.getArea() == targetPlayer.getAreaId()) {
                    // Currently causes increasing numbers to spawn, have to investigate
                    // || !Collections.disjoint(boss.getGroups(), scene.getPlayerActiveGroups(targetPlayer))) {
                // If boss is already active, don't respawn it
                var acList = activeMonsters.stream().filter((e) -> e.getConfigId() == boss.getId()).toList();
                if (acList.size() > 0) {
                    // Check if boss still exists
                    for (GameEntity gameEntity : acList) {
                        try {
                            if (gameEntity.isAlive()) {
                                return;
                            } else {
                                activeMonsters.remove(gameEntity);
                                gameEntity.getScene().removeEntity(gameEntity);
                            }
                        } catch (Exception e) {
                            // Doesn't exist, continue
                            activeMonsters.remove(gameEntity);
                        }
                    }
                }
                bossToSpawn = boss;
                Position pos = new Position(boss.getPos());
                Position rot = new Position(boss.getRot());

                MonsterData monsterData = GameData.getMonsterDataMap().get(boss.getId());
                EntityMonster entity = new EntityMonster(scene, monsterData, pos, rot,
                        bossToSpawn.getParam().lvl);
                RaidsCommand.applyCommonParameters(entity, bossToSpawn.getParam());
                entity.setGroupId(800815);
                entity.setAiId(27001005);

                // Set config id
                entity.setConfigId(entity.getMonsterData().getId());

                // Add to scene
                for (int i = 0; i < boss.getParam().amount; i++) {
                    scene.addEntity(entity);
                    newMonsters.add(entity);
                }
            }
        }
        setMonsters(newMonsters);
        newMonsters.clear();
    } // if

    public static void raidReward(GameEntity monster) {
        // Remove boss from active list
        removeBossFromList(monster);

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

    private static void removeBossFromList(GameEntity monster) {
        activeMonsters.removeIf(m -> m.getConfigId() == monster.getConfigId());
    }

    public static void resetBossList() {
        activeMonsters.forEach(m -> m.getScene().removeEntity(m, VisionType.VISION_TYPE_REMOVE));
        activeMonsters.clear();
    }
} // spawnMobEntity
