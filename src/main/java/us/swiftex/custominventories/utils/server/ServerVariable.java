package us.swiftex.custominventories.utils.server;

import us.swiftex.custominventories.CustomInventories;
import us.swiftex.custominventories.utils.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerVariable {

    private static final Pattern ONLINE_PATTERN = makePlaceholder("online");
    private static final Pattern MAX_PATTERN = makePlaceholder("max_players");
    private static final Pattern MOTD_PATTERN = makePlaceholder("motd");
    private static final Pattern STATUS_PATTERN = makePlaceholder("status");

    private static Pattern makePlaceholder(String prefix) {
        return Pattern.compile("(\\{" + Pattern.quote(prefix) + ":)(.+?)(\\})");
    }

    private static String extractValue(Matcher matcher) {
        return matcher.group(2).trim();
    }

    public static String replace(String text) {
        Matcher matcher;

        matcher = ONLINE_PATTERN.matcher(text);
        while (matcher.find()) {
            ServerInfo info = CustomInventories.getServerManager().getServer(extractValue(matcher));

            text = matcher.replaceFirst(String.valueOf(info == null ? 0 : info.getOnlinePlayers()));
        }

        matcher = MAX_PATTERN.matcher(text);
        while (matcher.find()) {
            ServerInfo info = CustomInventories.getServerManager().getServer(extractValue(matcher));

            text = matcher.replaceFirst(String.valueOf(info == null ? 0 : info.getMaxPlayers()));
        }

        matcher = MOTD_PATTERN.matcher(text);
        while (matcher.find()) {
            ServerInfo info = CustomInventories.getServerManager().getServer(extractValue(matcher));

            text = matcher.replaceFirst(info == null ? Messages.OFFLINE.get() : info.getMotd());
        }

        matcher = STATUS_PATTERN.matcher(text);
        while (matcher.find()) {
            ServerInfo info = CustomInventories.getServerManager().getServer(extractValue(matcher));

            text = matcher.replaceFirst(info == null || !info.isOnline() ? Messages.OFFLINE.get() : Messages.ONLINE.get());
        }

        return text;
    }
}
