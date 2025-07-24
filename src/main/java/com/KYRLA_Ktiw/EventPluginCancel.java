package com.KYRLA_Ktiw;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;


public final class EventPluginCancel extends JavaPlugin implements CommandExecutor {
    // Приоритеты, отмена и получение информации
    private static EventPluginCancel plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("Плагин MyEventsPluginCancel включился!");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new MyEvents(this), (this));
        getLogger().info("PlayerJoinListener успешно зарегистрирован!");
        getCommand("blockinfo").setExecutor(this);
        getLogger().info("Команда blockinfo зарегистрирована");
        getCommand("checkregion").setExecutor(this);
        getLogger().info("Команда blockinfo зарегистрирована");
        getCommand("spawnguard").setExecutor(this);
        getLogger().info("Команда spawnguard зарегистрирована");
        getCommand("clearitems").setExecutor(this);
        getLogger().info("Команда clearitems зарегистрирована");
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин MyEventsPlugin выключился!");
    }
    public static EventPluginCancel getPlugin() {
        return plugin;
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду могут использовать только игроки!");
            return true;
        }

        Player player = (Player) sender; // приводим отправителя к типу player

        if (command.getName().equalsIgnoreCase("blockinfo")) {
            Block targetBlock = player.getTargetBlock(null,100);

            if (targetBlock != null) {
                player.sendMessage(ChatColor.YELLOW + "Вы смотрите на блок типа: " + targetBlock.getType().name());
                player.sendMessage(ChatColor.YELLOW + "Координаты: " + "X: " + targetBlock.getX() + "Y: " + targetBlock.getY() + "Z: " + targetBlock.getZ());

            } else {
                player.sendMessage(ChatColor.RED + "Вы не смотрите на блок.");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("checkregion")) {
            World world = player.getWorld();
            if (!world.getName().equalsIgnoreCase("world")) {
                player.sendMessage(ChatColor.RED + "Вы не находитесь в основном мире!");
                return true;
            }
            int playerX = player.getLocation().getBlockX();
            int playerY = player.getLocation().getBlockY();
            int playerZ = player.getLocation().getBlockZ();
            int minX = 0;
            int minY = 64;
            int minZ = 0;

            int maxX = 100;
            int maxY = 100;
            int maxZ = 100;
            if (playerX >= minX && playerX <= maxX && playerY >= minY && playerY <= maxY && playerZ >= minZ && playerZ <= maxZ ) {
                player.sendMessage(ChatColor.GREEN + "Вы в разрешенной зоне!");
            }else {
                player.sendMessage(ChatColor.RED + "Вы покинули безопасную зону");
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("spawnguard")){
            Location playerLoc = player.getLocation();
            if(playerLoc.getBlock().getType() == Material.AIR && playerLoc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + "не могу заспавнить стража в воздухе!");
                return true;
            }
            Location spawnLoc = playerLoc.add(0, 1 ,0);
            player.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            player.sendMessage(ChatColor.GREEN + "Зомби страж заспавнен!");
            this.plugin.getLogger().info(player.getName() + " Заспавнил зомби" );
            return true;
        }
        if (command.getName().equalsIgnoreCase("clearitems")) {
            World world = player.getWorld();
            int removeCount = 0;
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.DROPPED_ITEM) {
                    entity.remove();
                    removeCount++;
                }
            }
            player.sendMessage(ChatColor.DARK_PURPLE + "Все предметы были очищены! Количество: " + removeCount );
            this.plugin.getLogger().info("Все предметы были очищены! Количество: " + removeCount);
        }
        return false; // если не наша команда, то возращаем false
    }
}


