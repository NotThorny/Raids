package thorny.grasscutters.Raids;

import emu.grasscutter.game.entity.GameEntity;
import emu.grasscutter.game.props.EntityType;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.entity.EntityDeathEvent;
import emu.grasscutter.server.event.player.*;
import thorny.grasscutters.Raids.commands.RaidsCommand;


/**
 * A class containing all event handlers.
 * Syntax in event handler methods are similar to CraftBukkit.
 * To register an event handler, create a new instance of {@link EventHandler}.
 * Pass through the event class you want to handle. (ex. `new EventHandler<>(PlayerJoinEvent.class);`)
 * You can change the point at which the handler method is invoked with {@link EventHandler#priority(HandlerPriority)}.
 * You can set whether the handler method should be invoked when another plugin cancels the event with {@link EventHandler#ignore(boolean)}.
 */
public final class EventListener {
    public static void EntityDeathEvent(EntityDeathEvent event) {
        if (RaidsCommand.getMobSceneGroup() == event.getEntity().getGroupId())
            try {
                GameEntity monster = event.getEntity();
                var entType = event.getEntity().getEntityType();
                if (EntityType.Monster.getValue() == entType.getValue()){
                    MobSpawner.raidReward(monster);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    public static void PlayerEnterAreaEvent(PlayerEnterAreaEvent event) {
        MobSpawner.spawnMobEntity(event.getPlayer());
    }

    public static void PlayerQuitEvent(PlayerQuitEvent event) {
        MobSpawner.resetBossList();
    }

    public static void PlayerTpEvent(PlayerTeleportEvent event) {
        MobSpawner.resetBossList();
    }
}
