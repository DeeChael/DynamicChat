package net.deechael.dynamichat.command;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.command.argument.*;
import net.deechael.dynamichat.util.Ref;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Argument helper is used to get argument in command
 */
public final class EzArgumentHelper {

    private final CommandContext<Object> commandContext;
    private final CommandSender commandSender;

    EzArgumentHelper(CommandContext<Object> commandContext) {
        this.commandContext = commandContext;
        this.commandSender = (CommandSender) Ref.invoke(commandContext.getSource(), Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"));
    }

    /**
     * ArgumentType: BaseArguments.string() [word(), greedyString()]<br>
     * Get argument as string type</br>
     * It doesn't allow space be contains in the string
     *
     * @param argumentName argument name
     * @return string
     */
    public String getAsString(String argumentName) {
        return StringArgumentType.getString(commandContext, argumentName);
    }

    /**
     * ArgumentType: BaseArguments.integer()<br>
     * Get argument as integer type
     *
     * @param argumentName argument name
     * @return integer
     */
    public Integer getAsInteger(String argumentName) {
        return IntegerArgumentType.getInteger(commandContext, argumentName);
    }

    /**
     * ArgumentType: BaseArguments.bool()<br>
     * Get argument as boolean type
     *
     * @param argumentName argument name
     * @return boolean
     */
    public Boolean getAsBoolean(String argumentName) {
        return BoolArgumentType.getBool(commandContext, argumentName);
    }

    /**
     * ArgumentType: BaseArguments.doubleArg()<br>
     * Get argument as double type
     *
     * @param argumentName argument name
     * @return double
     */
    public Double getAsDouble(String argumentName) {
        return DoubleArgumentType.getDouble(commandContext, argumentName);
    }

    /**
     * ArgumentType: BaseArguments.longArg()<br>
     * Get argument as long type
     *
     * @param argumentName argument name
     * @return long
     */
    public Long getAsLong(String argumentName) {
        return LongArgumentType.getLong(commandContext, argumentName);
    }

    /**
     * ArgumentType: BaseArguments.floatArg()<br>
     * Get argument as float type
     *
     * @param argumentName argument name
     * @return float
     */
    public Float getAsFloat(String argumentName) {
        return FloatArgumentType.getFloat(commandContext, argumentName);
    }

    /**
     * ArgumentType: ArgumentPlayer</br>
     * Multi player selector</br>
     * Get argument as player targets
     *
     * @param argumentName argument name
     * @return player targets
     */
    public List<Player> getAsPlayers(String argumentName) {
        List<Player> list = new ArrayList<>();
        Collection<Object> collection = (Collection<Object>) ArgumentPlayer.instance().get(commandContext, argumentName);
        if (collection != null) {
            for (Object object : collection) {
                Player player = (Player) ArgumentEntities.nmsEntityToBukkitEntity(object);
                if (player != null) list.add(player);
            }
        }
        return list;
    }

    /**
     * ArgumentType: ArgumentLocation</br>
     * Get argument as location</br>
     * X, y, z is double number
     *
     * @param argumentName argument name
     * @return location
     */
    public Location getAsLocation(String argumentName) {
        if (commandSender instanceof Player) {
            return ArgumentLocation.vec3DToLocation(((Player) commandSender).getWorld(), ArgumentLocation.instance().get(commandContext, argumentName));
        } else {
            return ArgumentLocation.vec3DToLocation(ArgumentLocation.instance().get(commandContext, argumentName));
        }

    }

    /**
     * ArgumentType: ArgumentWorld</br>
     * Get argument as world</br>
     * Three default world: world, world_nether, world_the_end are overworld, the_nether, the_end
     *
     * @param argumentName argument name
     * @return world
     */
    public World getAsWorld(String argumentName) {
        return ArgumentWorld.worldServerToBukkitWorld(ArgumentWorld.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentEnchantment</br>
     * Get argument as enchantment type
     *
     * @param argumentName argument name
     * @return enchantment type
     */
    public Enchantment getAsEnchantment(String argumentName) {
        return ArgumentEnchantment.nmsEnchantmentToBukkitEnchantment(ArgumentEnchantment.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentChat</br>
     * Get argument as chat message string</br>
     * Allow space, so the arguments after this will not be detected to their real type
     *
     * @param argumentName argument name
     * @return chat message string
     */
    public String getAsChatMessage(String argumentName) {
        return ArgumentChat.chatToString(ArgumentChat.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentBlockLocation</br>
     * Get argument as block location</br>
     * X, y, z is integer number
     *
     * @param argumentName argument name
     * @return location
     */
    public Location getAsBlockLocation(String argumentName) {
        if (commandSender instanceof Player) {
            return ArgumentBlockLocation.blockPositionToLocation(((Player) commandSender).getWorld(), ArgumentBlockLocation.instance().get(commandContext, argumentName));
        } else {
            return ArgumentBlockLocation.blockPositionToLocation(ArgumentBlockLocation.instance().get(commandContext, argumentName));
        }
    }

    /**
     * ArgumentType: ArgumentItemStack</br>
     * Get argument as a item stack</br>
     * Support NBT, so named ItemStack not Material
     *
     * @param argumentName argument name
     * @param amount       item stack amount
     * @return item stack
     */
    public ItemStack getAsItemStack(String argumentName, int amount) {
        return ArgumentItemStack.nmsItemStackToBukkitItemStack(ArgumentItemStack.instance().get(commandContext, argumentName), amount);
    }

    /**
     * ArgumentType: ArgumentItemStack</br>
     * Get as a block material
     *
     * @param argumentName argument name
     * @return block material
     */
    public Material getAsBlock(String argumentName) {
        return ArgumentBlock.nmsBlockToMaterial(ArgumentBlock.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentEntities</br>
     * Multi entity selector</br>
     * Get argument as entity targets
     *
     * @param argumentName argument name
     * @return entity targets
     */
    public List<Entity> getAsEntities(String argumentName) {
        List<Entity> list = new ArrayList<>();
        Collection<Object> collection = (Collection<Object>) ArgumentPlayer.instance().get(commandContext, argumentName);
        if (collection != null) {
            for (Object object : collection) {
                Entity entity = ArgumentEntities.nmsEntityToBukkitEntity(object);
                if (entity != null) list.add(entity);
            }
        }

        return list;
    }

    /**
     * ArgumentType: ArgumentEntity</br>
     * Single entity selector</br>
     * Get argument as a entity target
     *
     * @param argumentName argument name
     * @return entity target
     */
    public Entity getAsEntity(String argumentName) {
        return ArgumentEntities.nmsEntityToBukkitEntity(ArgumentEntity.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentOfflinePlayer</br>
     * Multi offline player selector</br>
     * Get argument as offline player targets
     *
     * @param argumentName argument name
     * @return offline player targets
     */
    public List<OfflinePlayer> getAsOfflinePlayer(String argumentName) {
        List<OfflinePlayer> list = new ArrayList<>();
        for (GameProfile gameProfile : ArgumentOfflinePlayer.instance().get(commandContext, argumentName)) {
            list.add(Bukkit.getOfflinePlayer(gameProfile.getId()));
        }
        return list;
    }

    /**
     * ArgumentType: ArgumentAttribute</br>
     * Get argument as an attribute type
     *
     * @param argumentName argument name
     * @return attribute type
     */
    public Attribute getAsAttribute(String argumentName) {
        return ArgumentAttribute.nmsAttributeBaseToBukkitAttribute(ArgumentAttribute.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentPotionEffectType</br>
     * Get argument type as a potion effect type
     *
     * @param argumentName argument name
     * @return potion effect type
     */
    public PotionEffectType getAsPotionEffectType(String argumentName) {
        return ArgumentPotionEffectType.mobEffectListToPotionEffectType(ArgumentPotionEffectType.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentEntityType</br>
     * Get argument type as an entity type
     *
     * @param argumentName argument name
     * @return entity name
     */
    public EntityType getAsEntityType(String argumentName) {
        return ArgumentEntityType.nmsEntityTypesToBukkitEntityType(ArgumentEntityType.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentParticle</br>
     * Get argument as a particle type
     *
     * @param argumentName argument name
     * @return particle type
     */
    public Particle getAsParticle(String argumentName) {
        return ArgumentParticle.nmsParticleToBukkitParticle(ArgumentParticle.instance().get(commandContext, argumentName));
    }

    /**
     * ArgumentType: ArgumentChatColor</br>
     * Get argument as a bungee chat color
     *
     * @param argumentName argument name
     * @return bungee chat color
     */
    public ChatColor getAsChatColor_Bungee(String argumentName) {
        return ChatColor.getByChar(ArgumentChatColor.getColorChar(ArgumentChatColor.instance().get(commandContext, argumentName)));
    }

    /**
     * ArgumentType: ArgumentChatColor</br>
     * Get argument as a bukkit chat color
     *
     * @param argumentName argument name
     * @return bukkit chat color
     */
    public org.bukkit.ChatColor getAsChatColor_Bukkit(String argumentName) {
        return org.bukkit.ChatColor.getByChar(ArgumentChatColor.getColorChar(ArgumentChatColor.instance().get(commandContext, argumentName)));
    }

    /**
     * ArgumentType: ArgumentUUID</br>
     * Get argument type as a UUID
     *
     * @param argumentName argument name
     * @return uuid
     */
    public UUID getAsUUID(String argumentName) {
        return ArgumentUUID.instance().get(commandContext, argumentName);
    }

    /**
     * ArgumentType: ArgumentNBTTag</br>
     * Get argument type as a nbt tag json object
     *
     * @param argumentName argument name
     * @return nbt tag json object
     */
    public JsonObject getAsNBTTag(String argumentName) {
        return ArgumentNBTTag.instance().get(commandContext, argumentName);
    }

}
