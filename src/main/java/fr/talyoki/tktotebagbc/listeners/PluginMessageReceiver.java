package fr.talyoki.tktotebagbc.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

public class PluginMessageReceiver implements Listener
{
	@EventHandler
	public void on(PluginMessageEvent event)
	{
		if(!event.getTag().equalsIgnoreCase("lulu:tktotebagbc"))
		{
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

		String tmp = in.readUTF();
		tmp = tmp.substring(2, tmp.length());
		String[] args = tmp.split("\\|");

		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
		if(player == null)
		{
			return;
		}

		if(!ProxyServer.getInstance().getPluginManager().isExecutableCommand(args[1], player))
		{
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "[TKtotebagBC] : Une commande sudo a été exécutée mais la commande demandée n'existe pas");
			player.sendMessage(new TextComponent(ChatColor.RED + "Vous avez été forcé de faire une commande qui n'existe pas"));
			return;
		}

		ProxyServer.getInstance().getPluginManager().dispatchCommand(player, args[1]);
	}
}
