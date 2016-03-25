package com.xdeathcubex.commands;

import com.xdeathcubex.FreundeSystem;
import com.xdeathcubex.mysql.MySQL;
import com.xdeathcubex.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend");
    }

    String uuid = null;
    ProxiedPlayer p1 = null;

    TextComponent notexist = new TextComponent(FreundeSystem.prefix + "§cDieser Spieler §cexistiert §cnicht!");

    @Override
    public void execute(CommandSender cs, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer)cs;
        if (args.length == 0) {
            player.sendMessage(new TextComponent("§e---------- §aFreundeSystem §e----------\n"
                    + "§e/friend add <Name> §8- §7Fügt einen Freund hinzu\n"
                    + "§e/friend remove <Name> §8- §7Entfernen einen Freund\n"
                    + "§e/friend list §8- §7Zeigt alle Freunde\n"
                    + "§e/friend requests §8- §7 Zeigt offene §7Freundschaftsanfragen an\n"
                    + "§e/friend jump <Name> §8- §7Zum Server vom Freund §7verbinden\n"
                    + "§e/friend accept <Name> §8- §7Nimmt eine §7Freundschaftsanfrage an\n"
                    + "§e/friend deny <Name> §8- §7Lehnt eine §7Freundschaftsanfrage §7ab\n"
                    + "§e/msg <Name> <Nachricht> §8- §7Sendet einem Freund eine §7Nachricht\n"
                    + "§e/r <Nachricht> §8- §7Antwortet auf eine Nachricht\n"
                    + "§e/friend toggle §8- §7Anfragen deaktivieren\n"
                    + "§e/friend togglemsgs §8- §7private Nachrichten §7deaktivieren\n"
                    + "§e/friend togglejump §8- §7Nachspringen §7deaktivieren\n"
                    + "§e/friend togglenotifies §8- §7Join / Leave Nachrichten §7deaktivieren\n"
                    + "§e---------- §aFreundeSystem §e----------"));


        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cHast du sonst keine Freunde?"));
            } else {
                uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null || MySQL.get("Friends", uuid) == null) {
                    player.sendMessage(notexist);
                } else {
                    if (MySQL.is("Friends", uuid, player.getUniqueId().toString().replaceAll("-",""))) {
                        player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu bist mit §cdiesem Spieler §cbereits befreundet!"));
                    } else {
                        if (MySQL.is("Requests", player.getUniqueId().toString().replaceAll("-",""), uuid)) {
                            player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu hast diesem §cSpieler bereits §ceine §cFreundschaftsanfrage §cgeschickt!"));
                        } else {
                            if (MySQL.getProperties(uuid, "invites")) {
                                MySQL.add(uuid, "Requests", player.getUniqueId().toString().replaceAll("-",""));
                                player.sendMessage(new TextComponent(FreundeSystem.prefix + "§7Anfrage wurde an §6" + args[1] + " §7geschickt!"));
                                p1 = ProxyServer.getInstance().getPlayer(args[1]);
                                if (p1 != null) {
                                    p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" + player.getDisplayName() + " §7hat §7dir §7eine §7Freundschaftsanfrage §7geschickt."));
                                    p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§e/friend accept " + player.getDisplayName() + " §8- §7um §7die §7Anfrage §aanzunehmen"));
                                    p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§e/friend deny " + player.getDisplayName() + " §8- §7um die §7Anfrage §cabzulehnen"));
                                }
                            } else {
                                player.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" + p1.getDisplayName() + " §cempfängt §ckeine §cFreundschaftsanfragen!"));
                            }
                        }
                    }
                }
            }


        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            uuid = UUIDFetcher.getUUID(args[1]);
            if (uuid == null) {
                player.sendMessage(notexist);
            } else {
                if (!MySQL.is("Friends", uuid, player.getUniqueId().toString().replaceAll("-",""))) {
                    player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu §cbist §cmit §cdiesem §cSpieler §cnicht §cbefreundet!"));
                } else {
                    MySQL.remove(uuid, "Friends", player.getUniqueId().toString().replaceAll("-",""));
                    MySQL.remove(player.getUniqueId().toString().replaceAll("-",""), "Friends", uuid);
                    player.sendMessage(new TextComponent(FreundeSystem.prefix + "§7Du hast §6 " + args[1] + " §7von §7deiner §7Freundesliste §7entfernt!"));
                    p1 = ProxyServer.getInstance().getPlayer(args[1]);
                    if (p1 != null) {
                        p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" + player.getDisplayName() + " §7hat §7dich §7von §7seiner §7Freundesliste §7entfernt."));
                    }
                }
            }


        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            player.sendMessage(new TextComponent("§eFreunde: "));
            String freunde = MySQL.get("Friends", player.getUniqueId().toString().replaceAll("-",""));
            String[] freunde1 = freunde.split(" ");
            for (String friends : freunde1) {
                if(!friends.equals("")) {
                    String name = UUIDFetcher.getName(friends);
                    ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(name);
                    if(p1 != null){
                        player.sendMessage(new TextComponent("§8- §7" + p1.getDisplayName() + " §8- §a" + p1.getServer().getInfo().getName()));
                    } else {
                        player.sendMessage(new TextComponent("§8- §7" + name));
                    }
                }
            }


        } else if (args.length == 1 && args[0].equalsIgnoreCase("requests")) {
            player.sendMessage(new TextComponent("§eAusstehende Freundschaftsanfragen: "));
            String anfragen = MySQL.get("Requests", player.getUniqueId().toString().replaceAll("-",""));
            String[] anfragen1 = anfragen.split(" ");
            for (String requests : anfragen1) {
                if (!requests.equals("")) {
                    player.sendMessage(new TextComponent("§8- §7" + UUIDFetcher.getName(requests)));
                }
            }
        } else if(args.length == 2 && args[0].equalsIgnoreCase("jump")) {
            uuid = UUIDFetcher.getUUID(args[1]);
            if(uuid == null){
                player.sendMessage(notexist);
            } else {
                if(MySQL.is("Friends", uuid, player.getUniqueId().toString().replaceAll("-",""))){
                    if(MySQL.getProperties(uuid, "jump")){
                        p1 = ProxyServer.getInstance().getPlayer(uuid);
                        player.connect(p1.getServer().getInfo());
                    } else {
                        player.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" +p1.getDisplayName() + " §cerlaubt §ckein §cNachjoinen!"));
                    }
                }
            }

        } else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            uuid = UUIDFetcher.getUUID(args[1]);
            if (uuid == null) {
                player.sendMessage(notexist);
            } else {
                if (MySQL.is("Friends", uuid, player.getUniqueId().toString().replaceAll("-",""))) {
                    player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu bist §cbereits §cmit §cdiesem §cSpieler §cbefreundet!"));
                } else {
                    if (MySQL.is("Requests", uuid, player.getUniqueId().toString().replaceAll("-",""))) {
                        MySQL.remove(player.getUniqueId().toString().replaceAll("-",""), "Requests", uuid);
                        MySQL.add(uuid, "Friends", player.getUniqueId().toString().replaceAll("-",""));
                        MySQL.add(player.getUniqueId().toString().replaceAll("-",""), "Friends", uuid);
                        player.sendMessage(new TextComponent(FreundeSystem.prefix + "§7Du bist nun mit §6" + args[1] + " §7befreundet!"));
                        p1 = ProxyServer.getInstance().getPlayer(args[1]);
                        if (p1 != null) {
                            p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" + player.getDisplayName() + " §7hat §7deine §7Freundschaftsanfrage §7angenommen!"));
                        }
                    } else {
                        player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu hast §ckeine §cFreundschaftsanfrage §cvon §cdiesem Spieler bekommen!"));
                    }
                }
            }


        } else if (args.length == 2 && args[0].equalsIgnoreCase("deny")) {
            uuid = UUIDFetcher.getUUID(args[1]);
            if (uuid == null) {
                player.sendMessage(notexist);
            } else {
                if (MySQL.is("Requests", uuid, player.getUniqueId().toString().replaceAll("-",""))) {
                    MySQL.remove(player.getUniqueId().toString().replaceAll("-",""), "Requests", uuid);
                    player.sendMessage(new TextComponent(FreundeSystem.prefix + "§7Du §7hast §7die §7Freundschaftsanfrage §7von §6" + args[1] + " §7abgelehnt!"));
                    p1 = ProxyServer.getInstance().getPlayer(args[1]);
                    if (p1 != null) {
                        p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§6" + player.getDisplayName() + " §7hat deine§7 Freundschaftsanfrage §7abgelehnt!"));
                    }
                } else {
                    player.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu hast §7keine Freundschaftsanfrage §7von §7diesem §7Spieler §7bekommen!"));
                }
            }
        } else if(args.length == 1 && args[0].equalsIgnoreCase("toggle")){
            MySQL.changeProperties(player.getUniqueId().toString().replaceAll("-",""), "invites");
            player.sendMessage(new TextComponent(MySQL.getProperties(player.getUniqueId().toString().replaceAll("-",""), "invites") ? FreundeSystem.prefix+"Du kannst nun §awieder §7Freundschaftsanfragen §7erhalten.":
                    FreundeSystem.prefix+"Du erhälst nun §ckeine §7Freundschaftsanfragen §7mehr."));
        } else if(args.length == 1 && args[0].equalsIgnoreCase("togglemsgs")){
            MySQL.changeProperties(player.getUniqueId().toString().replaceAll("-",""), "msgs");
            player.sendMessage(new TextComponent(MySQL.getProperties(player.getUniqueId().toString().replaceAll("-",""), "msgs") ? FreundeSystem.prefix+"Du erhälst nun §awieder §7private §7Nachrichten." :
                    FreundeSystem.prefix+"Du erhälst nun §ckeine §7privaten §7Nachrichten."));
        } else if(args.length == 1 && args[0].equalsIgnoreCase("togglejump")){
            MySQL.changeProperties(player.getUniqueId().toString().replaceAll("-",""), "jump");
            player.sendMessage(new TextComponent(MySQL.getProperties(player.getUniqueId().toString().replaceAll("-",""), "jump") ? FreundeSystem.prefix+"Dir kann nun §awieder §7nachgesprungen §7werden." :
                    FreundeSystem.prefix+"Dir kann nun §cnicht mehr §7nachgesprungen §7werden."));
        } else if(args.length == 1 && args[0].equalsIgnoreCase("togglenotifies")){
            MySQL.changeProperties(player.getUniqueId().toString().replaceAll("-",""), "notifies");
            player.sendMessage(new TextComponent(MySQL.getProperties(player.getUniqueId().toString().replaceAll("-",""), "notifies") ? FreundeSystem.prefix+"Du erhälst §awieder §7Join/Leave §7Benachrichtungen." :
                    FreundeSystem.prefix+"Du erhälst nun §ckeine §7Join/Leave §7Benachrichtigungen."));
        }
    }
}