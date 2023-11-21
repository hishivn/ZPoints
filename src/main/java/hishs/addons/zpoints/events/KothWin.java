package hishs.addons.zpoints.events;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FactionsAPI;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import fr.maxlego08.zkoth.api.event.events.KothWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class KothWin implements Listener {

    FactionsAPI api = FactionsBridge.getFactionsAPI();
    @EventHandler
    public void KothWin(KothWinEvent kothwin){
        Factions factions = Factions.getInstance();
        if (factions == null) {
        } else {
            String factionTag = kothwin.getKoth().getCurrentFaction();
            Player player = kothwin.getPlayer();
            Faction faction = (Faction) api.getFaction(player);


            Bukkit.broadcastMessage("Фракция " + factionTag + " §7Захватила точку");
            player.sendMessage("Очки фракции §6+1");           //Integration code here
        }


    }

}
