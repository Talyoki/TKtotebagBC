package fr.talyoki.tktotebagbc;

import fr.talyoki.tktotebagbc.cmd.PingCmd;
import fr.talyoki.tktotebagbc.cmd.ServerCmd;
import fr.talyoki.tktotebagbc.listeners.PluginMessageReceiver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class TktotebagBC extends Plugin
{
	@Override
	public void onEnable()
	{
		getProxy().registerChannel("lulu:tktotebagbc");

		// Commandes
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCmd());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerCmd());

		// Listeners
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessageReceiver());
	}

	@Override
	public void onDisable()
	{

	}

}
