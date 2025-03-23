package org.holiday.meowSMP.commands;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.holiday.meowSMP.enums.TokenType;
import org.holiday.meowSMP.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class TokenCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("meowsmp.token")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /token <token_name>");
            return true;
        }

        String tokenName = args[0].toLowerCase();
        TokenType selectedToken = null;
        for (TokenType token : TokenType.values()) {
            if (token.name().toLowerCase().equals(tokenName)) {
                selectedToken = token;
                break;
            }
        }

        if (selectedToken == null) {
            player.sendMessage(ChatColor.YELLOW + "Token not found. Use /token and press TAB for suggestions.");
            return true;
        }

        ItemStack tokenItem = TokenUtils.createTokenItem(selectedToken, 1);
        player.getInventory().addItem(tokenItem);
        player.sendMessage(ChatColor.GREEN + "You have received the token: " + selectedToken.getDisplayName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (TokenType token : TokenType.values()) {
                String tokenName = token.name().toLowerCase();
                if (tokenName.startsWith(partial)) {
                    completions.add(tokenName);
                }
            }
        }
        return completions;
    }
}
