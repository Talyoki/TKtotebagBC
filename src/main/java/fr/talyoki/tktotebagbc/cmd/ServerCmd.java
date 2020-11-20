package fr.talyoki.tktotebagbc.cmd;

import fr.talyoki.tktotebagbc.data.ErrorMsg;
import fr.talyoki.tktotebagbc.data.Permissions;
import fr.talyoki.tktotebagbc.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServerCmd extends Command implements TabExecutor
{
	public ServerCmd()
	{
		super("server");
	}

	public void execute(CommandSender sender, String[] args)
	{
		if(!this.hasServerListPermission(sender))
		{
			sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
			return;
		}
		if(args.length < 1)
		{
			// Récupération de la liste des serveurs
			Map<String, ServerInfo> serverList = ProxyServer.getInstance().getServers();

			// Préparation de la liste
			TextComponent serverListPrint = new TextComponent("Liste des serveurs :\n");
			serverListPrint.setColor(ChatColor.GOLD);

			// Récupération du serveur du joueur
			String servActu = ((ProxiedPlayer) sender).getServer().getInfo().getName();

			// Génération de la liste
			for(ServerInfo serverInfo : serverList.values())
			{
				TextComponent serverServerPrint = new TextComponent("");

				if(this.hasByServerPermission(sender, serverInfo.getName()))
				{
					if(servActu.equalsIgnoreCase(serverInfo.getName()))
					{
						// Serveur du joueur
						serverServerPrint = new TextComponent(
								"[" + ChatColor.STRIKETHROUGH + serverInfo.getName() + ChatColor.RESET + ChatColor.GOLD + "] ");
						serverServerPrint.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Text(
								ChatColor.RED + "Tu es sur ce serveur")));
					}
					else
					{
						// Autres serveurs
						serverServerPrint = new TextComponent("[" + serverInfo.getName() + "] ");
						serverServerPrint.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Text("Rejoindre ce serveur")));
						serverServerPrint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + serverInfo.getName()));
					}
				}

				// Envoie de la liste clickable au joueur
				serverListPrint.addExtra(serverServerPrint);
			}
			sender.sendMessage(serverListPrint);
			return;
		}
		if(args.length >= 2)
		{
			if(this.hasServerOtherPermission(sender))
			{
				if(!this.hasByServerOtherPermission(sender, args[0]))
				{
					sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
					return;
				}
				// Récupération du joueur ciblé
				// Autocompletion
				final List<ProxiedPlayer> resultPlayers = new ArrayList<ProxiedPlayer>();
				StringUtil.getPartialMatchesPlayer(args[1], ProxyServer.getInstance().getPlayers(), resultPlayers);

				// Si plusieurs choix sont possible on return
				if(resultPlayers.size() == 1)
				{
					// Récupération du 1er résultats
					String pseudo = resultPlayers.get(0).getName();

					ProxiedPlayer player = ProxyServer.getInstance().getPlayer(pseudo);
					if(player != null)
					{
						// Récupération du serveur ciblé
						ServerInfo target = ProxyServer.getInstance().getServerInfo(args[0]);
						// Si le serveur existe pas
						if(target == null)
						{
							sender.sendMessage(new TextComponent(ChatColor.RED + "Ce serveur n'existe pas"));
							return;
						}
						// si le joueur est deja sur ce serveur
						if(player.getServer().getInfo().getName().equalsIgnoreCase(args[1]))
						{
							sender.sendMessage(new TextComponent(ChatColor.RED + "Le joueur est déjà sur ce serveur"));
							return;
						}
						// Téléportation du joueur
						player.connect(target);
					}
					else
					{
						sender.sendMessage(new TextComponent(ChatColor.RED + "Le joueur n'est pas connecté"));
					}
				}
				else
				{
					sender.sendMessage(new TextComponent(ChatColor.RED + "Trop de choix possible"));
				}
			}
			else
			{
				sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
			}
		}
		else
		{
			if(this.hasByServerPermission(sender, args[0]))
			{
				// Récupération du serveur ciblé
				ServerInfo target = ProxyServer.getInstance().getServerInfo(args[0]);
				ProxiedPlayer player = (ProxiedPlayer) sender;
				// Si le serveur demandé n'existe pas
				if(target == null)
				{
					sender.sendMessage(new TextComponent(ChatColor.RED + "Ce serveur n'existe pas"));
					return;
				}
				// Si le joueur est déjà sur ce serveur
				if(player.getServer().getInfo().getName().equalsIgnoreCase(args[0]))
				{
					sender.sendMessage(new TextComponent(ChatColor.RED + "Tu es déjà sur ce serveur"));
					return;
				}
				// Téléportation du sender
				player.connect(target);
			}
			else
			{
				sender.sendMessage(new TextComponent(String.valueOf(ErrorMsg.ERROR_PERM)));
			}
		}
	}

	public boolean hasServerListPermission(CommandSender sender)
	{
		return sender.hasPermission(Permissions.CMD_SERVER.toString());
	}

	public boolean hasServerOtherPermission(CommandSender sender)
	{
		return sender.hasPermission(Permissions.CMD_SERVER_OTHER.toString());
	}

	public boolean hasByServerOtherPermission(CommandSender sender, String world)
	{
		return sender.hasPermission(Permissions.CMD_SERVER_OTHER_BY_NAME + world);
	}

	public boolean hasByServerPermission(CommandSender sender, String world)
	{
		return sender.hasPermission(Permissions.CMD_SERVER_BY_NAME + world);
	}

	// auto completion
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] args)
	{
		// Récupération de la liste des serveurs
		Map<String, ServerInfo> serverList = ProxyServer.getInstance().getServers();

		if(args.length == 1)
		{
			List<String> list = new ArrayList<>();
			for(ServerInfo serverInfo : serverList.values())
			{
				if(commandSender.hasPermission(Permissions.CMD_SERVER_BY_NAME + serverInfo.getName()))
				{
					list.add(serverInfo.getName());
				}
			}

			final List<String> completions = new ArrayList<>();
			StringUtil.copyPartialMatches(args[0], list, completions);

			return completions;
		}

		// Récupération de la liste des joueurs
		Collection<ProxiedPlayer> playerList = ProxyServer.getInstance().getPlayers();

		if(args.length == 2)
		{
			if(commandSender.hasPermission(Permissions.CMD_SERVER_OTHER_BY_NAME + args[0]))
			{
				List<String> list = new ArrayList<>();
				for(ProxiedPlayer player : playerList)
				{
					list.add(player.getName());
				}

				final List<String> completions = new ArrayList<>();
				StringUtil.copyPartialMatches(args[1], list, completions);

				return completions;
			}
		}
		return new ArrayList<>();
	}
}
