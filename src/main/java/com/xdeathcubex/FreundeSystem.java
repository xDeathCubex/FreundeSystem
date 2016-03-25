package com.xdeathcubex;

import com.xdeathcubex.commands.FriendCommand;
import com.xdeathcubex.commands.MSGCommand;
import com.xdeathcubex.commands.RCommand;
import com.xdeathcubex.events.JoinEvent;
import com.xdeathcubex.events.LeaveEvent;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashMap;

public class FreundeSystem extends Plugin {

    public static HashMap<ProxiedPlayer, ProxiedPlayer> msg = new HashMap<ProxiedPlayer, ProxiedPlayer>();
    public static String prefix = "§7[§cFreunde§7] §7";
    public static FreundeSystem instance;

    public void onEnable(){
        instance = this;
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new JoinEvent());
        pluginManager.registerListener(this, new LeaveEvent());
        pluginManager.registerCommand(this, new FriendCommand());
        pluginManager.registerCommand(this, new MSGCommand());
        pluginManager.registerCommand(this, new RCommand());
        new MySQL("localhost", "LogMC", "asd", "LogMC");
    }
}
