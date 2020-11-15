package fr.talyoki.tktotebagbc;

import fr.talyoki.tktotebagbc.cmd.PingCmd;
import fr.talyoki.tktotebagbc.cmd.ServerCmd;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class TktotebagBC extends Plugin
{
	@Override
	public void onEnable()
	{
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCmd());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerCmd());
	}

	@Override
	public void onDisable()
	{

	}
}
