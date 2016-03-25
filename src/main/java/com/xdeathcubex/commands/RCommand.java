package com.xdeathcubex.commands;

import com.xdeathcubex.FreundeSystem;
import com.xdeathcubex.mysql.MySQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RCommand extends Command {

    public RCommand() {
        super("r");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) cs;
        ProxiedPlayer p1 = FreundeSystem.msg.get(p);

        if (args.length < 1) {
            p.sendMessage(new TextComponent(FreundeSystem.prefix + "§cVerwendung: /r <Nachricht>"));
        } else {
            if (MySQL.is("Friends", p.getUniqueId().toString(), p1.getUniqueId().toString().replaceAll("-",""))) {
                if(MySQL.getProperties(p1.getUniqueId().toString().replaceAll("-",""), "msgs")) {
                    String nachricht = "";
                    for (String arg : args) {
                        nachricht += arg + " §e";
                    }
                    p.sendMessage(new TextComponent(FreundeSystem.prefix + "§7Du §8-> §r" + p1.getDisplayName() + " §8» §e" + nachricht));
                    p1.sendMessage(new TextComponent(FreundeSystem.prefix + "§r" + p.getDisplayName() + " §8-> §7Dir §8» §e" + nachricht));
                    FreundeSystem.msg.put(p1, p);
                } else {
                    p.sendMessage(new TextComponent(FreundeSystem.prefix +"§c" + p1.getDisplayName() + "§c erlaubt §ckeine §cprivaten §cNachrichten."));
                }
            } else {
                p.sendMessage(new TextComponent(FreundeSystem.prefix + "§cDu bist §cmit §cdiesem §cSpieler §cnicht §cbefreundet!"));
            }
        }
    }


}
