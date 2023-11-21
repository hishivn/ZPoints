package hishs.addons.zpoints;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.util.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cc.javajobs.factionsbridge.util.Updater.VersionStatus.UP_TO_DATE;

public final class ZPoint extends JavaPlugin {

    private FileConfiguration config;
    @Override
    public void onEnable() {
        FactionsBridge bridge = new FactionsBridge();
        bridge.connect(this);
        try {
            Objects.requireNonNull(getCommand("factionsbridge")).setExecutor(this);
        } catch (Exception e) {
            bridge.exception(e, "Callum is an idiot.");
        }
        if (!FactionsBridge.get().connected()) {
            bridge.log("FactionsBridge didn't connect.");
            return;
        }

        // API test.
        if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null) {
            bridge.warn("FastAsyncWorldEdit changes the load-order of the Server. Delaying the API test by 5 seconds.");
            Bukkit.getScheduler().runTaskLater(this, () -> {
                int loaded = FactionsBridge.getFactionsAPI().getFactions().size();
                bridge.warn(loaded + " factions have been loaded.");
            }, 100L);
        } else {
            int loaded = FactionsBridge.getFactionsAPI().getFactions().size();
            bridge.warn(loaded + " factions have been loaded.");
        }

        // Check for updates.
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            bridge.log("Checking for Updates.");
            try {
                Updater updater = new Updater(getDescription().getVersion());
                switch (updater.getStatus()) {
                    case BETA:
                        bridge.log("You're using a Beta version of FactionsBridge.");
                        break;
                    case UP_TO_DATE:
                        bridge.log("You're all up to date. No issues here!");
                        break;
                    case OUT_OF_DATE_MAJOR:
                        bridge.error("The version of FactionsBridge you're currently using is majorly out of date.");
                        break;
                    case OUT_OF_DATE_MINOR:
                        bridge.warn("The version of FactionsBridge you're currently using is out of date.");
                        break;
                    case OUT_OF_DATE_MINI:
                        bridge.warn("The version of FactionsBridge you're currently using is slightly out of date.");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + updater.getStatus());
                }
            } catch (Exception e) {
                bridge.warn("Couldn't check for updates.");
            }
        });



        getServer().getLogger().info("§aZkothAddon enabled");
        getServer().getPluginManager().registerEvents(new hishs.addons.zpoints.events.KothWin(), this);
        loadConfig();        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("§cZkothAddon disabled");        // Plugin shutdown logic
    }

    private void loadConfig() {
        // Создаем (если не существует) и загружаем конфигурацию из файла config.yml
        config = getConfig();
        config.options().copyDefaults(true); // Копировать значения по умолчанию, если их нет
        saveConfig(); // Сохраняем конфиг обратно в файл
    }
}
