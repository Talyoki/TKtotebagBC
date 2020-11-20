package fr.talyoki.tktotebagbc.cmd;

import fr.talyoki.tktotebagbc.data.ErrorMsg;
import fr.talyoki.tktotebagbc.data.Permissions;
import fr.talyoki.tktotebagbc.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PingCmd extends Command implements TabExecutor
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
				// Autocompletion
				final List<ProxiedPlayer> resultPlayers = new ArrayList<ProxiedPlayer>();
				StringUtil.getPartialMatchesPlayer(args[0], ProxyServer.getInstance().getPlayers(), resultPlayers);
				// Si plusieurs choix sont possible on return
				if(resultPlayers.size() != 1)
				{
					return;
				}

				// Récupération du 1er résultats
				String pseudo = resultPlayers.get(0).getName();

				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(pseudo);
				// So le joueur demandé n'est pas connecté
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
				sender.sendMessage(new TextComponent(ErrorMsg.ERROR_PERM.toString()));
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
				sender.sendMessage(new TextComponent(ErrorMsg.ERROR_PERM.toString()));
			}
		}
	}

	// Permission de faire /ping
	public boolean hasPingPermission(CommandSender sender)
	{
		return sender.hasPermission(Permissions.CMD_PING_SELF.toString());
	}

	// Permission de faire /ping [player]
	public boolean hasPingOtherPermission(CommandSender sender)
	{
		return sender.hasPermission(Permissions.CMD_PING_OTHER.toString());
	}

	// auto completion
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] args)
	{
		// Récupération de la liste des joueurs
		Collection<ProxiedPlayer> playerList = ProxyServer.getInstance().getPlayers();

		if(args.length == 1)
		{
			List<String> list = new ArrayList<>();
			for(ProxiedPlayer player:playerList)
			{
				if(commandSender.hasPermission(Permissions.CMD_PING_OTHER.toString()))
				{
					list.add(player.getName());
				}
			}

			final List<String> completions = new ArrayList<>();
			StringUtil.copyPartialMatches(args[0], list, completions);

			return completions;
		}

		return new ArrayList<>();
	}
}
