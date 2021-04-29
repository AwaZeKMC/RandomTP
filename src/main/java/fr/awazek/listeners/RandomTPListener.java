package fr.awazek.listeners;

import fr.awazek.RandomTP;
import fr.awazek.util.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomTPListener implements Listener {

    RandomTP main;
    public RandomTPListener(RandomTP main) {
        this.main = main;
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy / HH:mm:ss");

        Title title = new Title();

        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {

                Date dateNow = new Date();
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

                if (dateSave != null) {
                    long difference_In_Time = dateSave.getTime() - dateNow.getTime();
                    long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
                    long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
                    long difference_In_Seconds = (difference_In_Time / 1000) % 60;

                    if (difference_In_Hours > 0) {
                        title.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.hours").replace("%hours%", difference_In_Hours + "").replace("%minutes%", difference_In_Minutes + "")));
                    }
                    if (difference_In_Hours <= 0) {
                        title.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.minutes").replace("%minutes%", difference_In_Minutes + "")));
                    }
                    if (difference_In_Minutes <= 0 && difference_In_Hours <= 0) {
                        title.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.seconds").replace("%seconds%", difference_In_Seconds + "")));
                    }
                    if (difference_In_Time <= 0) {
                        title.sendActionBar(player, (ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.available"))));
                    }
                } else  title.sendActionBar(player, (ChatColor.translateAlternateColorCodes('&', main.getTextConfig().getString("remaining-time.available"))));

            }
        }.

                runTaskTimer(main, 0L,20L* main.getTextConfig().getInt("time-between.actionbar"));
    }
}
