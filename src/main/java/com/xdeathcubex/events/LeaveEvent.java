package com.xdeathcubex.events;

import com.xdeathcubex.main.FreundeSystem;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        String[] friends = MySQL.get("Friends", uuid).split(" ");
        for(String freunde : friends){
            if(!freunde.equals("")){
                if(ProxyServer.getInstance().getPlayer(UUID.fromString(freunde)) != null) {
                    ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(UUID.fromString(freunde));
                    if(MySQL.getProperties(p1.getUniqueId().toString(), "notifies")) {
                        p1.sendMessage(new TextComponent(FreundeSystem.prefix + e.getPlayer().getDisplayName() + " §7ist nun §coffline"));
                    }
                }
            }
        }
    }
}