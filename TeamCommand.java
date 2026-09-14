package com.example.eventplugin.commands;
import com.example.eventplugin.EventPlugin;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
public class TeamCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return true;
        String team = (label.equalsIgnoreCase("blau") || label.equalsIgnoreCase("blue") || command.getName().equalsIgnoreCase("blau") || command.getName().equalsIgnoreCase("blue")) ? "blue" : "red";
        String path="locations."+team;
        if(!EventPlugin.getInstance().getConfig().contains(path+".world")){ player.sendMessage("§cSpawn für "+team+" nicht gesetzt!"); return true; }
        World world=Bukkit.getWorld(EventPlugin.getInstance().getConfig().getString(path+".world"));
        double x=EventPlugin.getInstance().getConfig().getDouble(path+".x");
        double y=EventPlugin.getInstance().getConfig().getDouble(path+".y");
        double z=EventPlugin.getInstance().getConfig().getDouble(path+".z");
        float yaw=(float)EventPlugin.getInstance().getConfig().getDouble(path+".yaw");
        float pitch=(float)EventPlugin.getInstance().getConfig().getDouble(path+".pitch");
        player.teleport(new Location(world,x,y,z,yaw,pitch));
        Bukkit.getScheduler().runTaskLater(EventPlugin.getInstance(), () -> {
            player.getInventory().clear();
            Color color=team.equals("blue")?Color.fromRGB(0,0,255):Color.fromRGB(255,0,0);
            for(Material m : new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS}){
                ItemStack piece=new ItemStack(m);
                LeatherArmorMeta meta=(LeatherArmorMeta)piece.getItemMeta();
                meta.setColor(color); meta.setUnbreakable(true); piece.setItemMeta(meta);
                if(m==Material.LEATHER_HELMET) player.getInventory().setHelmet(piece);
                else if(m==Material.LEATHER_CHESTPLATE) player.getInventory().setChestplate(piece);
                else if(m==Material.LEATHER_LEGGINGS) player.getInventory().setLeggings(piece);
                else player.getInventory().setBoots(piece);
            }
            ItemStack sword=new ItemStack(Material.IRON_SWORD); var sm=sword.getItemMeta(); sm.setUnbreakable(true); sm.addEnchant(Enchantment.UNBREAKING, 3, true); sword.setItemMeta(sm);
            ItemStack bow=new ItemStack(Material.BOW); var bm=bow.getItemMeta(); bm.setUnbreakable(true); bm.addEnchant(Enchantment.UNBREAKING, 3, true); bow.setItemMeta(bm);
            player.getInventory().addItem(sword, bow, new ItemStack(Material.ARROW, 64));
            player.updateInventory();
            player.sendMessage(team.equals("blue")?"§9BLAUES TEAM!":"§cROTES TEAM!");
        }, 5L);
        return true;
    }
}
