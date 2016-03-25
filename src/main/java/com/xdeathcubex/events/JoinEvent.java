package com.xdeathcubex.events;

import com.xdeathcubex.FreundeSystem;
import com.xdeathcubex.mysql.MySQL;
import com.xdeathcubex.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();

        String name = p.getName();
        if(name.length() > 14){
            name = name.substring(0,14);
        }
        String uuid = p.getUniqueId().toString().replaceAll("-","");
        if(MySQL.getRank(uuid) != null){
            String group = MySQL.getRank(uuid);
            if(group.equalsIgnoreCase("admin")){
                p.setDisplayName("§4" + name);
            }
            if(group.equalsIgnoreCase("dev")){
                p.setDisplayName("§b" + name);
            }
            if(group.equalsIgnoreCase("srmod")){
                p.setDisplayName("§c" + name);
            }
            if(group.equalsIgnoreCase("mod")){
                p.setDisplayName("§c" + name);
            }
            if(group.equalsIgnoreCase("builder")){
                p.setDisplayName("§2" + name);
            }
            if(group.equalsIgnoreCase("youtuber")){
                p.setDisplayName("§5" + name);
            }
            if(group.equalsIgnoreCase("premium")){
                p.setDisplayName("§6" + name);
            }
        } else {
            p.setDisplayName("§a" + name);
        }

        MySQL.setUser(uuid);
        MySQL.setupProperties(uuid);
        String[] friends = MySQL.get("Friends", uuid).split(" ");
        for(String freunde : friends){
            if(!freunde.equals("")){
                if(ProxyServer.getInstance().getPlayer(UUIDFetcher.getName(freunde)) != null) {
                    ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(UUIDFetcher.getName(freunde));
                    if(MySQL.getProperties(p1.getUniqueId().toString().replaceAll("-",""), "notifies")) {
                        p1.sendMessage(new TextComponent(FreundeSystem.prefix + p.getDisplayName() + " §7ist nun §aonline"));
                    }
                }
            }
        }

        String[] requests = MySQL.get("requests" , uuid).split(" ");
        int counter = 0;
        for(String anfragen : requests){
            if(!anfragen.equalsIgnoreCase("")){
                counter++;
            }
        }
        if(counter != 0) {
            p.sendMessage(new TextComponent(FreundeSystem.prefix + "Du hast noch §6" + counter + " §7ausstehende §7Freundschaftsanfrage(n)."));
            p.sendMessage(new TextComponent(FreundeSystem.prefix + "Gib §6/friend requests §7ein, um §7diese §7anzusehen!"));
        }
    }
}