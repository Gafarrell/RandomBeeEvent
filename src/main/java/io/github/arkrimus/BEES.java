package io.github.arkrimus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class BEES extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final FileConfiguration beeYaml;

    BEES(JavaPlugin plugin, FileConfiguration bees){
        this.plugin = plugin;
        beeYaml = bees;
    }

    @Override
    public void run() {

        if (!plugin.getServer().getOnlinePlayers().isEmpty()){

            // Get the online players and select a random one, notify them that they've been "Bee'd"
            ArrayList<Player> onlinePlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
            int player = random.nextInt()%onlinePlayers.size();
            Player p = (onlinePlayers.size() > 1) ? onlinePlayers.get(player) : onlinePlayers.get(0);
            p.sendMessage("You've been Bee'd!");

            // Spawn the bees on the player
            for (int i = 0; i < beeYaml.getInt("SpawnCount"); i++){
                Entity attacker = p.getWorld().spawnEntity(p.getLocation().add(0,1,0), EntityType.BEE);
                if (attacker instanceof Bee) {
                    ((Bee) attacker).setTarget(p);
                    ((Bee) attacker).setAnger(630);
                }
            }
        }
    }
}
