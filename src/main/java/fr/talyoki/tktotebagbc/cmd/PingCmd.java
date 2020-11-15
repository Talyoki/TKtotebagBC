package fr.talyoki.tktotebagbc.cmd;

import fr.talyoki.tktotebagbc.data.ErrorMsg;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCmd extends Command
{
	public PingCmd()
	{
		super("ping");
	}

	public void execute(CommandSender sender, String[] args)
	{
		if(args.length >= 1)
		{
			if(this.hasPingOtherPermission(sender))
			{
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
				if(player != null)
				{
					sender.sendMessage(new TextComponent(ChatColor.GOLD + "Le ping de " + player.getName() + " est de " + player.getPing() + " ms"));
				}
				else
				{
					sender.sendMessage(new TextComponent(ChatColor.RED + "Le joueur n'est pas connecté"));
				}
			}
			else
			{
				sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
			}
		}
		else
		{
			if(this.hasPingPermission(sender))
			{
				int ping = ProxyServer.getInstance().getPlayer(sender.getName()).getPing();
				sender.sendMessage(new TextComponent(ChatColor.GOLD + "Ton ping est de " + ping + " ms"));
			}
			else
			{
				sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
			}
		}
	}

	// Permission de faire /ping
	public boolean hasPingPermission(CommandSender sender)
	{
		return sender.hasPermission("tktotebagbc.ping.self");
	}

	// Permission de faire /ping [player]
	public boolean hasPingOtherPermission(CommandSender sender)
	{
		return sender.hasPermission("tktotebagbc.ping.other");
	}
}
