package net.deechael.dynamichat.extension.blacklist.commands;

import net.deechael.dynamichat.extension.blacklist.BlackListManager;
import net.deechael.dynamichat.extension.blacklist.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CommandBlackList extends Command {

    public CommandBlackList() {
        super("blacklist");
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                list.add("help");
                list.add("add");
                list.add("remove");
                list.add("list");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).forEach(list::add);
                    list.remove(sender.getName());
                } else if (args[0].equalsIgnoreCase("remove")) {
                    list.addAll(BlackListManager.getBlacked(player));
                } else {
                    list.add(" ");
                }
            } else {
                list.add(" ");
            }
        } else {
            list.add(" ");
        }
        return list;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Lang.sendConsole(sender, "command.mustbeplayer");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                Lang.send(player, "command.blacklist.help");
            } else if (args[0].equalsIgnoreCase("list")) {
                List<String> blacks = BlackListManager.getBlacked(player);
                if (blacks.size() <= 10) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("§6§l========= §r§6[§r§e1§r§6/§r§e1§r§6] §r§6§l=========\n");
                    blacks.forEach(black -> builder.append("§e§l").append(black).append("\n"));
                    builder.append("§6§l=======================");
                    player.sendMessage(builder.toString());
                } else {
                    int pages = blacks.size() % 10 == 0 ? blacks.size() / 10 : blacks.size() / 10 + 1;
                    StringBuilder builder = new StringBuilder();
                    builder.append("§6§l========= §r§6[§r§e1§r§6/§r§e").append(pages).append("§r§6] §r§6§l=========\n");
                    for (int i = 0; i < 10; i++) {
                        builder.append("§e§l").append(blacks.get(i)).append("\n");
                    }
                    builder.append("§6§l=======================");
                    player.sendMessage(builder.toString());
                }
            } else {
                Lang.send(player, "command.blacklist.gotohelp");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (player.getName().equalsIgnoreCase(args[1])) {
                    Lang.send(player, "command.blacklist.notself");
                } else {
                    if (BlackListManager.getBlacked(player).contains(args[1].toLowerCase())) {
                        Lang.send(player, "command.blacklist.add.alreadyin", args[1]);
                    } else {
                        BlackListManager.add(player, args[1].toLowerCase());
                        Lang.send(player, "command.blacklist.add.success", args[1]);
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (player.getName().equalsIgnoreCase(args[1])) {
                    Lang.send(player, "command.blacklist.selfnot");
                } else {
                    if (!BlackListManager.getBlacked(player).contains(args[1].toLowerCase())) {
                        Lang.send(player, "command.blacklist.remove.notin", args[1]);
                    } else {
                        BlackListManager.remove(player, args[1].toLowerCase());
                        Lang.send(player, "command.blacklist.remove.success", args[1]);
                    }
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                try {
                    int page = Integer.parseInt(args[1]);
                    List<String> blacks = BlackListManager.getBlacked(player);
                    int pages = blacks.size() % 10 == 0 ? blacks.size() / 10 : blacks.size() / 10 + 1;
                    if (page < 1)
                        page = 1;
                    if (page > pages)
                        page = pages;
                    StringBuilder builder = new StringBuilder();
                    builder.append("§6§l========= §r§6[§r§e").append(page).append("§r§6/§r§e").append(pages).append("§r§6] §r§6§l=========\n");
                    for (int i = 10 * (page - 1); i < Math.min(10 * page, blacks.size()); i++) {
                        builder.append("§e§l").append(blacks.get(i)).append("\n");
                    }
                    builder.append("§6§l=======================");
                    player.sendMessage(builder.toString());
                } catch (NumberFormatException e) {
                    Lang.send(player, "command.blacklist.notint");
                }
            } else {
                Lang.send(player, "command.blacklist.gotohelp");
            }
        } else {
            Lang.send(player, "command.blacklist.gotohelp");
        }
        return true;
    }

}
