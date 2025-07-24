package com.KYRLA_Ktiw;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import static org.bukkit.GameMode.ADVENTURE;

public class MyEvents implements Listener {
    // Приоритеты, отмена и получение информации
    private final EventPluginCancel plugin;
    public MyEvents(EventPluginCancel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material brokenBlockType = event.getBlock().getType();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (brokenBlockType == Material.DIAMOND_BLOCK) {
            if (itemInHand.getType().name().contains("_PICKAXE")) {
                player.sendMessage(ChatColor.AQUA + "Вы сломали алмазный блок киркой!");
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Вы не можете ломать алмазные блоки без кирки!");
                Location blockLocation = event.getBlock().getLocation();
                blockLocation = event.getBlock().getLocation();
                double x = blockLocation.getX();
                double y = blockLocation.getY();
                double z = blockLocation.getZ();
                plugin.getLogger().info(player.getName() + " Попытался сломать алмазный блок без кирки по координатам: " + "X: " + x + "Y: " + y + "Z: " + z);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.RED + player.getName() + " покинул нас :( ");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        player.sendMessage(ChatColor.AQUA + "Вы сказали: " + message);
        FileConfiguration config = EventPluginCancel.getPlugin().getConfig();
        String echoPrefix = config.getString("chat-echo-prefix", "§5[Эхо] §f");
        echoPrefix = ChatColor.translateAlternateColorCodes('&', echoPrefix);
        event.setMessage(echoPrefix + message);
//        event.setMessage(ChatColor.LIGHT_PURPLE + "[Эхо] " + ChatColor.WHITE + message);
    }

    @EventHandler
    // playerjoinevent это игрок который только что вызвал событие(зашел на сервер)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = EventPluginCancel.getPlugin().getConfig();
        boolean enableWelcome = config.getBoolean("enable-welcome-message", true);
        if (enableWelcome) {
            String welcomeMesage = config.getString("welcome-message", "Привет, %player_name%! Добро пожаловать на сервер!");
            welcomeMesage = welcomeMesage.replace("%player_name%", player.getName());
            welcomeMesage = ChatColor.translateAlternateColorCodes('§', welcomeMesage);
            player.sendMessage(welcomeMesage);
        }
        player.setHealth(player.getMaxHealth());
        player.sendMessage(ChatColor.GREEN + "Ваше здоровье полностью восстановлено!");
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.sendMessage(ChatColor.GREEN + "Ваш голод утолен!");
        player.setGameMode(ADVENTURE);
        player.sendMessage(ChatColor.DARK_PURPLE + "Ваш режим теперь приключение!");
        player.setLevel(5);
        player.sendMessage(ChatColor.DARK_GREEN + "Ваш опыт установлен на 5!");
        Location spawnLocation = player.getWorld().getSpawnLocation();
        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.BLUE + "Вы были телепортированы на спавн!");
        this.plugin.getLogger().info(player.getName() + " Зашел на сервер и был полностью готов к игре!");
        player.sendActionBar(ChatColor.GREEN + "Привет! " + player.getName());
        player.sendTitle(ChatColor.GOLD + "Добро пожаловать ", ChatColor.DARK_AQUA + "На сервер!", 20, 60, 20 );
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 64);
        ItemStack torch = new ItemStack(Material.TORCH, 10);
        player.getInventory().addItem(woodenSword, bread, torch);
        player.sendMessage(ChatColor.YELLOW + "Вы получили стартовый набор!");
        event.setJoinMessage(null);
    }
    @EventHandler
    public void handleinventoryClick (InventoryClickEvent event) {
        // проверяем что именно игрок заходит в инвентарь
        event.getWhoClicked();
        if (!(event.getWhoClicked() instanceof Player)) {
            return;//  возращаем если не он
        }
        // даем понять что именно игрок будет взаимодействовать с инвентарем
        Player player = (Player) event.getWhoClicked();
        // проверяем что открывается сундку
        if (event.getInventory().getType() == InventoryType.CHEST) {
            // получаем предмет по которому кликнули
            ItemStack clikedItem = event.getCurrentItem();
            // проверяем не алмаз ли это и что мы не кликнули по пустому месту, если алмаз отменяем
            if(clikedItem != null && clikedItem.getType() == Material.DIAMOND) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Вы не можете брать алмазы из этого сундука!");
                this.plugin.getLogger().info(player.getName() + " Попытался взять из сундука АЛМАЗ!!");
            }
        }
    }
    @EventHandler
    public void handleEntityDamage (EntityDamageEvent event) {
        // проверяем что это игрок
        if (event.getEntity() instanceof Player) {
            // проверяем что он был именно он падения и отменяем его
            if (event.getCause() == DamageCause.FALL) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity(); // подводим сущность к игроку
                player.sendMessage(ChatColor.GREEN + "Урон не поступил!");
                this.plugin.getLogger().info(player.getName() + "Избежал урона от падения.");
            }
        }
    }
    @EventHandler
    public void handleEntityDeath(EntityDeathEvent event) {
        // проверяем что умер именно зомби
        if (event.getEntityType() == EntityType.ZOMBIE) {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
            this.plugin.getLogger().info("Зомби умер и с него выпал 1 алмаз!");
        }
    }
    @EventHandler
    public void handlePlayerInteract (PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        // --- Логика для ТНТ ---
            boolean disableTnt = this.plugin.getConfig().getBoolean("disable-tnt");
            if (disableTnt) {
            if (action == Action.RIGHT_CLICK_BLOCK && event.getItem().getType() == Material.TNT) {
                event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Ты не можешь ставить ТНТ!");

            this.plugin.getLogger().info("Игрок хотел поставить тнт!");
            return;
              }
            }

        // --- Логика для Факела ---
        if (action == Action.RIGHT_CLICK_AIR && event.hasItem() && event.getItem().getType() == Material.TORCH) {
            PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 20 * 10, 3);
            PotionEffect speedEffect1 = new PotionEffect(PotionEffectType.CONDUIT_POWER, 20 * 10, 3);
            player.addPotionEffect(speedEffect);
            player.addPotionEffect(speedEffect1);
            player.sendMessage(ChatColor.LIGHT_PURPLE + " Вы почувствовали прилив энергии!!");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            this.plugin.getLogger().info("Кто то взбодрился");
            return;
        }

        // --- Логика для Блок-переключателя ---
        if (action == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
            if (clickedBlock.getType() == Material.STONE) {
                clickedBlock.setType(Material.DIAMOND_BLOCK);
                player.sendMessage(ChatColor.BLUE + "Камень превратился в алмазный блок!");
                event.setCancelled(true);
                return;
            } else if (clickedBlock.getType() == Material.DIAMOND_BLOCK) {
                clickedBlock.setType(Material.STONE);
                player.sendMessage(ChatColor.BLUE + " Вы поменяли алмазный блок на камень!");
                event.setCancelled(true);
                return;
            }
        }

        if(action == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
            if(clickedBlock.getType() == Material.CHEST) {
                BlockState state = clickedBlock.getState();
                if (state instanceof Chest) {
                    Chest chest = (Chest) state;
                    Inventory chestInventory = chest.getInventory();
                    if (chestInventory.contains(Material.DIAMOND)) {
                        player.sendMessage(ChatColor.GRAY + "Этот сундук уже пуст");
                        this.plugin.getLogger().info(player.getName() + "сундук пуст: " + clickedBlock.getX() + " " + clickedBlock.getY() + " " + clickedBlock.getZ() );
                    } else {
                        ItemStack diamondschest = new ItemStack(Material.DIAMOND, 1);
                        ItemStack diamonds = new ItemStack(Material.DIAMOND, 10);
                        player.getInventory().addItem(diamonds);
                        chestInventory.addItem(diamondschest);
                        player.sendMessage(ChatColor.AQUA + " Вы получили 10 алмазов!");
                    }
                    event.setCancelled(true);// отменяем открытие сундука
                    return;
                }
            }
        }
        // --- Логика для вывода информации о клике (ТОЛЬКО если ни одно из предыдущих условий не сработало)
        // Если код дошел сюда, значит, это был клик, который не обрабатывался ТНТ, Факелом, или переключателем
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (clickedBlock != null) {
                player.sendMessage(ChatColor.GREEN + "Вы кликнули по блоку: " + clickedBlock.getType().name());
            } else {
                player.sendMessage(ChatColor.GRAY + "Вы кликнули по воздуху с предметом: " + (event.hasItem() ? event.getItem().getType().name() : "пустой рукой"));
            }
            // Здесь return не нужен, так как это последнее, что должно произойти,
        }
    }


}