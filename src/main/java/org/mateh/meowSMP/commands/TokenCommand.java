package org.mateh.meowSMP.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mateh.meowSMP.Main;
import org.mateh.meowSMP.TokenItems;

public class TokenCommand implements CommandExecutor {

    private final Main main;

    public TokenCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /token <token>");
            player.sendMessage(ChatColor.YELLOW + "Available tokens: lion, cheetah, tiger, house, catfish, village, black, jungle, sphinx");
            return true;
        }

        String token = args[0].toLowerCase();
        ItemStack tokenItem = null;
        switch(token) {
            case "lion":
                tokenItem = TokenItems.createLionTokenItem(main);
                break;
            case "cheetah":
                tokenItem = TokenItems.createCheetahTokenItem(main);
                break;
            case "tiger":
                tokenItem = TokenItems.createTigerTokenItem(main);
                break;
            case "house":
                tokenItem = TokenItems.createHouseCatTokenItem(main);
                break;
            case "catfish":
                tokenItem = TokenItems.createCatfishTokenItem(main);
                break;
            case "village":
                tokenItem = TokenItems.createVillageCatTokenItem(main);
                break;
            case "black":
                tokenItem = TokenItems.createBlackCatTokenItem(main);
                break;
            case "jungle":
                tokenItem = TokenItems.createJungleCatTokenItem(main);
                break;
            case "sphinx":
                tokenItem = TokenItems.createSphinxTokenItem(main);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown token type.");
                return true;
        }
        player.getInventory().addItem(tokenItem);
        player.sendMessage(ChatColor.GREEN + "You have received the " + token + " token.");
        return true;
    }
}
