package com.br.gabrielmartins.syntri.loan.manager;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.loan.data.LoanData;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoanManager {

    @Getter
    private static final Map<UUID, LoanData> activeLoans = new HashMap<>();
    private static final File file = new File(SyntriPlugin.getInstance().getDataFolder(), "loans.yml");
    private static final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void startScheduler() {
        loadAllLoans();
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                for (LoanData loan : new ArrayList<>(activeLoans.values())) {
                    if (!loan.isPaid() && now.isAfter(loan.getDueDate())) {
                        Player p = Bukkit.getPlayer(loan.getPlayerId());
                        if (p != null && p.isOnline()) {
                            p.sendMessage("§cSeu empréstimo venceu e será cobrado agora.");
                            tryPay(p);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(SyntriPlugin.getInstance(), 0L, 20L * 60);
    }

    public static boolean hasLoan(Player player) {
        return activeLoans.containsKey(player.getUniqueId()) && !activeLoans.get(player.getUniqueId()).isPaid();
    }

    public static void createLoan(Player player, double amount, double interestPercent, int daysToPay) {
        double total = amount + (amount * (interestPercent / 100.0));
        LocalDateTime due = LocalDateTime.now().plusDays(daysToPay);

        LoanData data = new LoanData(player.getUniqueId(), amount, total, due, false);
        activeLoans.put(player.getUniqueId(), data);
        saveLoan(data);

        Economy econ = SyntriPlugin.getInstance().getEconomy();
        if (econ != null) econ.depositPlayer(player, amount);

        player.sendMessage("§aVocê pegou um empréstimo de §2$" + amount + "§a a ser pago até §e" + due.format(formatter));
    }

    public static void tryPay(Player player) {
        UUID id = player.getUniqueId();
        LoanData data = activeLoans.get(id);
        if (data == null || data.isPaid()) return;

        Economy econ = SyntriPlugin.getInstance().getEconomy();
        if (econ == null) return;

        double total = data.getTotalWithInterest();
        if (econ.has(player, total)) {
            econ.withdrawPlayer(player, total);
            data.setPaid(true);
            player.sendMessage("§aSeu empréstimo foi pago com sucesso!");
            saveLoan(data);
        } else {
            player.sendMessage("§cVocê ainda não tem dinheiro suficiente para quitar seu empréstimo de §4$" + total);
        }
    }

    public static void openLoanMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§aBanco - Empréstimos");

        var cfg = SyntriPlugin.getInstance().getConfig().getConfigurationSection("loan");
        if (cfg == null) return;

        double interest = cfg.getDouble("interest", 15.0);
        int days = cfg.getInt("payment-days", 3);
        List<Integer> values = cfg.getIntegerList("values");

        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);
            ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a$" + value);
            meta.setLore(List.of(
                    "§fJuros: " + interest + "%",
                    "§fTotal: §2$" + String.format("%.2f", value + (value * interest / 100)),
                    "§fPrazo: §e" + days + " dias"
            ));
            item.setItemMeta(meta);
            inv.setItem(2 + i * 2, item);
        }

        player.openInventory(inv);
    }

    private static void saveLoan(LoanData data) {
        String key = data.getPlayerId().toString();
        config.set(key + ".amount", data.getAmount());
        config.set(key + ".total", data.getTotalWithInterest());
        config.set(key + ".due", data.getDueDate().format(formatter));
        config.set(key + ".paid", data.isPaid());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAllLoans() {
        for (String key : config.getKeys(false)) {
            try {
                UUID id = UUID.fromString(key);
                double amount = config.getDouble(key + ".amount");
                double total = config.getDouble(key + ".total");
                LocalDateTime due = LocalDateTime.parse(config.getString(key + ".due"), formatter);
                boolean paid = config.getBoolean(key + ".paid");
                activeLoans.put(id, new LoanData(id, amount, total, due, paid));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Erro ao carregar empréstimo de " + key);
            }
        }
    }
}
