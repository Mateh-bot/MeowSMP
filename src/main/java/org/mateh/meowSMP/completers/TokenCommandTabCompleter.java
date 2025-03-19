package org.mateh.meowSMP.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenCommandTabCompleter implements TabCompleter {
    private final List<String> tokenNames = Arrays.asList("lion", "cheetah", "tiger", "house", "catfish", "village", "black", "jungle", "sphinx");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String token : tokenNames) {
                if (token.startsWith(args[0].toLowerCase())) {
                    completions.add(token);
                }
            }
        }
        return completions;
    }
}
