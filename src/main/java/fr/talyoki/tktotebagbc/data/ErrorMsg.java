package fr.talyoki.tktotebagbc.data;

import net.md_5.bungee.api.ChatColor;

public enum ErrorMsg
{
    ERROR_CMD(ChatColor.RED + "Erreur dans la commande"),
    ERROR_PERM(ChatColor.RED + "Vous n'avez pas la permission");

    private String name = "";

    ErrorMsg(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public static int size() {
        return values().length;
    }
}
