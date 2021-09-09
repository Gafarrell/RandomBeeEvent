package io.github.arkrimus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Main extends JavaPlugin implements CommandExecutor {

    private BEES currentTask;

    @Override
    public void onEnable() {
        // Save the config and log enable info to console
        saveDefaultConfig();
        getLogger().info("Enabling the bees!");

        // Create task and start timer once server starts
        currentTask = new BEES(this, getConfig());
        currentTask.runTaskTimer(this, (long) getConfig().getInt("SpawnInterval")*20, (long)getConfig().getInt("SpawnInterval")*20);
    }

    @Override
    public void onDisable() {
        // Log disable info to server console
        getLogger().info("Disabling the bees!");
    }

    // Command listener to control bee spawning parameters
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Return if command used incorrectly
        if (args.length < 1) return false;

        // Ensure it's the bees command
        if (label.equalsIgnoreCase("bees")){

            // Sets the amount of bees to spawn
            if (args[0].equalsIgnoreCase("setCount")){
                int count = Integer.parseInt(args[1]);
                getConfig().set("SpawnCount", count);
                sender.sendMessage("Bees will now spawn in swarms of " + count + "!");
                return true;
            }

            // Sets the interval between bee spawns
            else if (args[0].equalsIgnoreCase("setTime")){
                long time = Long.parseLong(args[1]);
                getConfig().set("SpawnInterval", time);
                if (getServer().getScheduler().isQueued(currentTask.getTaskId())) {
                    getServer().getScheduler().cancelTask(currentTask.getTaskId());
                }
                currentTask = new BEES(this, getConfig());
                currentTask.runTaskTimer(this, (long) getConfig().getInt("SpawnInterval")*20, (long)getConfig().getInt("SpawnInterval")*20);
                sender.sendMessage("Bees will now spawn every " + time + " seconds.");
                return true;
            }

            // Stops spawning of bees
            else if (args[0].equalsIgnoreCase("stop")){
                if (getServer().getScheduler().isQueued(currentTask.getTaskId())){
                    getServer().getScheduler().cancelTask(currentTask.getTaskId());
                    getServer().broadcastMessage("Stopped the bees... for now.");
                    return true;
                }
                sender.sendMessage("The bees aren't running... But they could be :)");
                return true;
            }

            // Enables bee spawning.
            else if (args[0].equalsIgnoreCase("start")){
                if (!getServer().getScheduler().isQueued(currentTask.getTaskId())){
                    currentTask = new BEES(this, getConfig());
                    currentTask.runTaskTimer(this, (long) getConfig().getInt("SpawnInterval")*20, (long)getConfig().getInt("SpawnInterval")*20);
                    getServer().broadcastMessage("Let the bees begin!");
                    return true;
                }
                sender.sendMessage("Bee task already running!");
                return true;
            }
        }
        return false;
    }
}
