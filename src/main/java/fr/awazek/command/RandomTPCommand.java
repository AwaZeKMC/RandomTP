package fr.awazek.command;

import fr.awazek.RandomTP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomTPCommand implements CommandExecutor {
    RandomTP main;



    public RandomTPCommand(RandomTP main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("rtp") || label.equalsIgnoreCase("randomtp")) {

                Player player = (Player) sender;

                Location surfaceLocation = player.getLocation();
                surfaceLocation.setY(250);
                while (isSafetyLocation(surfaceLocation) == false) {
                    surfaceLocation = getSurfaceLocation(newLocation(player.getWorld()));
                }

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy / HH:mm:ss");
                Date dateNow = new Date();
                Date dateAfter = new Date();

                dateAfter.setHours(dateAfter.getHours() + main.getTextConfig().getInt("time-between.rtp"));

                String dateN = formatter.format(dateAfter);


                String dateSaved;
                if (main.getConfig().contains(String.valueOf(player.getUniqueId()))) {
                    dateSaved = main.getConfig().getString(String.valueOf(player.getUniqueId()));
                } else {
                    dateSaved = "";
                }


                Date dateSave = null;
                try {
                    if (!(dateSaved.equalsIgnoreCase(""))) {
                        dateSave = formatter.parse(dateSaved);
                    }
                } catch (ParseException e) {

                }


                if (dateSaved.equalsIgnoreCase("") || dateNow.after(dateSave)) {
                    player.teleport(surfaceLocation);
                    main.getConfig().createSection(String.valueOf(player.getUniqueId()));
                    main.getConfig().set(String.valueOf(player.getUniqueId()), dateN);
                    main.saveConfig();


                } else {

                    long difference_In_Time
                            = dateSave.getTime() - dateNow.getTime();
                    long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

                    long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
                    long difference_In_Seconds = (difference_In_Time / 1000) % 60;


                    if (difference_In_Hours > 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.hours").replace("%hours%", difference_In_Hours + "").replace("%minutes%", difference_In_Minutes + "")));
                    }
                    if (difference_In_Hours == 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.minutes").replace("%minutes%", difference_In_Minutes + ""))); //"il vous reste " + difference_In_Minutes + "minutes"
                    }
                    if (difference_In_Minutes <= 0 && difference_In_Hours <= 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.seconds").replace("%seconds%", difference_In_Seconds + "")));
                    }
                    if (difference_In_Hours == 0 && difference_In_Minutes == 0) {
                        player.sendMessage((ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.available"))));
                    }
                }

            }
        }


        return false;
    }


    public boolean isSafetyLocation(Location location) {
        Block underBlock = location.add(0, -1, 0).getBlock();
        if (underBlock.getType().equals(Material.LAVA)
                || underBlock.getType().equals(Material.STATIONARY_LAVA)
                || underBlock.getType().equals(Material.WATER)
                || underBlock.getType().equals(Material.STATIONARY_WATER)
                || underBlock.getType().equals(Material.AIR)) {
            return false;
        } else {
            return true;
        }
    }

    public Location getSurfaceLocation(Location location){
        Location newLocation = new Location(location.getWorld(), location.getX(), 256, location.getZ());

        for (int i = 150; i > 0; i--) {
            newLocation.setY(i);
            if (!newLocation.getBlock().getType().equals(Material.AIR)){

                return newLocation.add(0, 1,0);
            }
        }
        return newLocation;
    }

    public Location newLocation(World world){
        Random random = new Random();
        int X;
        int Y;
        int Z;
        int len = 0;

        while (len < main.getTextConfig().getInt("rtp.min") || len > main.getTextConfig().getInt("rtp.max")){
            X = random.nextInt(main.getTextConfig().getInt("rtp.max"));
            Y = 250;
            Z = random.nextInt(main.getTextConfig().getInt("rtp.max"));
            len = (int) Math.sqrt((X*X) + (Z*Z));
            if (len > main.getTextConfig().getInt("rtp.min") && len < main.getTextConfig().getInt("rtp.max")){
                return new Location(world , X, Y , Z);
            }
        }
        return null;
    }
}
