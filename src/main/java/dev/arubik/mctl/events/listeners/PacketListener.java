package dev.arubik.mctl.events.listeners;

import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Sets;

import dev.arubik.mctl.MComesToLife;
import dev.arubik.mctl.entity.CustomVillager;
import dev.arubik.mctl.events.Listener;
import dev.arubik.mctl.holders.Methods.DataMethods;
import me.libraryaddict.disguise.DisguiseAPI;

public class PacketListener extends Listener {
    static {
        type = "PROTOCOL";
    }

    private final static int RENDER_DISTANCE = 32;
    private final static Set<PacketType> MOVEMENT_PACKETS = Sets.newHashSet(
            PacketType.Play.Server.ENTITY_VELOCITY,
            PacketType.Play.Server.REL_ENTITY_MOVE,
            PacketType.Play.Server.ENTITY_LOOK,
            PacketType.Play.Server.ENTITY_TELEPORT,
            PacketType.Play.Server.ENTITY_HEAD_ROTATION,
            PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);

    @Override
    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(MComesToLife.getPlugin());
    }

    @Override
    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MComesToLife.getPlugin(),
                ListenerPriority.HIGHEST,
                PacketType.Play.Server.SPAWN_ENTITY,
                PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB,
                PacketType.Play.Server.NAMED_ENTITY_SPAWN,
                PacketType.Play.Server.ANIMATION,
                PacketType.Play.Server.BLOCK_BREAK_ANIMATION,
                PacketType.Play.Server.ENTITY_STATUS,
                PacketType.Play.Server.REL_ENTITY_MOVE,
                PacketType.Play.Server.REL_ENTITY_MOVE_LOOK,
                PacketType.Play.Server.ENTITY_LOOK,
                PacketType.Play.Server.ENTITY_HEAD_ROTATION,
                PacketType.Play.Server.CAMERA,
                PacketType.Play.Server.ENTITY_METADATA,
                PacketType.Play.Server.ATTACH_ENTITY,
                PacketType.Play.Server.ENTITY_VELOCITY,
                PacketType.Play.Server.ENTITY_EQUIPMENT,
                PacketType.Play.Server.MOUNT,
                PacketType.Play.Server.ENTITY_SOUND,
                PacketType.Play.Server.COLLECT,
                PacketType.Play.Server.ENTITY_TELEPORT,
                PacketType.Play.Server.UPDATE_ATTRIBUTES,
                PacketType.Play.Server.ENTITY_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType type = event.getPacketType();

                Player player = event.getPlayer();
                Entity entity = event.getPacket().getEntityModifier(player.getWorld()).readSafely(0);
                if (entity == null)
                    return;
                if (!DataMethods.isCustom(entity))
                    return;

                if (type.toString().toUpperCase().contains("SPAWN")) {
                    // event.setCancelled(true);
                    return;
                }
                if (DisguiseAPI.getDisguise(player, entity) == null) {
                    CustomVillager vil = new CustomVillager((LivingEntity) entity);
                    vil.loadVillager(false);
                    vil.Disguise(player);
                }
            }
        });
    }

}
