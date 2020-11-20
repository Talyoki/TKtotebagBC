package fr.talyoki.tktotebagbc.data;

public enum Permissions
{
	CMD_SERVER("server"),
	CMD_SERVER_BY_NAME("server."),
	CMD_SERVER_OTHER("server.other"),
	CMD_SERVER_OTHER_BY_NAME("server.other."),
	CMD_PING_SELF("ping.self"),
	CMD_PING_OTHER("ping.other");

	private String name = "";

	Permissions(String name)
	{
		String prefix = "tktotebagbc.";
		this.name = prefix + name;
	}

	public String toString() {
		return this.name;
	}

	public static int size() {
		return values().length;
	}

}
