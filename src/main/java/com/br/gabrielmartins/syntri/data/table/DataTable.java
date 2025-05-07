package com.br.gabrielmartins.syntri.data.table;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.api.translate.Translate;
import org.bukkit.entity.Player;

import java.sql.*;

public enum DataTable {

    SPAWN("spawn", "CREATE TABLE IF NOT EXISTS spawn (" + "id INT PRIMARY KEY, world VARCHAR(255), x DOUBLE, y DOUBLE, z DOUBLE);", "INSERT INTO spawn (id, world, x, y, z) VALUES (1, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE world = ?, x = ?, y = ?, z = ?"),
    HOME("homes", "CREATE TABLE IF NOT EXISTS homes (" + "uuid VARCHAR(36) NOT NULL, " + "home_name VARCHAR(255) NOT NULL, " + "world VARCHAR(255) NOT NULL, " + "x DOUBLE NOT NULL, " + "y DOUBLE NOT NULL, " + "z DOUBLE NOT NULL, " + "PRIMARY KEY (uuid, home_name));", "INSERT INTO homes (uuid, home_name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)" + "ON DUPLICATE KEY UPDATE x = ?, y = ?, z = ?", "SELECT home_name FROM homes WHERE uuid = ?", "SELECT world, x, y, z FROM homes WHERE uuid = ? AND home_name = ?");

    private final String tableName;
    private final String createTableSQL;
    private final String insertSQL;
    private final String listSQL;
    private final String selectSQL;

    DataTable(String tableName, String createTableSQL, String insertSQL) {
        this.tableName = tableName;
        this.createTableSQL = createTableSQL;
        this.insertSQL = insertSQL;
        this.listSQL = null;
        this.selectSQL = null;
    }

    DataTable(String tableName, String createTableSQL, String insertSQL, String listSQL, String selectSQL) {
        this.tableName = tableName;
        this.createTableSQL = createTableSQL;
        this.insertSQL = insertSQL;
        this.listSQL = listSQL;
        this.selectSQL = selectSQL;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCreateTableSQL() {
        return createTableSQL;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getListSQL() {
        return listSQL;
    }

    public String getSelectSQL() {
        return selectSQL;
    }

    public void createTable() {
        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSpawn(Player player) {
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SPAWN.getInsertSQL())) {

            stmt.setString(1, world);
            stmt.setDouble(2, x);
            stmt.setDouble(3, y);
            stmt.setDouble(4, z);
            stmt.setString(5, world);
            stmt.setDouble(6, x);
            stmt.setDouble(7, y);
            stmt.setDouble(8, z);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void teleportToSpawn(Player player) {
        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT world, x, y, z FROM spawn WHERE id = 1")) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String worldName = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");

                if (player.getServer().getWorld(worldName) != null) {
                    player.teleport(player.getServer().getWorld(worldName).getBlockAt((int) x, (int) y, (int) z).getLocation());
                    player.sendMessage("§aVocê foi teleportado para o spawn.");
                } else {
                    player.sendMessage("§cO mundo do spawn não foi encontrado.");
                }
            } else {
                player.sendMessage("§cSpawn ainda não foi definido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHome(Player player, String homeName) {
        String uuid = player.getUniqueId().toString();
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, uuid);
            stmt.setString(2, homeName);
            stmt.setString(3, world);
            stmt.setDouble(4, x);
            stmt.setDouble(5, y);
            stmt.setDouble(6, z);
            stmt.setDouble(7, x);
            stmt.setDouble(8, y);
            stmt.setDouble(9, z);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet listHomes(Player player) {
        String uuid = player.getUniqueId().toString();
        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             PreparedStatement stmt = conn.prepareStatement(listSQL)) {

            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                player.sendMessage(Translate.get("§a§lSuas casas:"));
                do {
                    String homeName = rs.getString("home_name");
                    player.sendMessage(Translate.get("§e - " + homeName));
                } while (rs.next());
            } else {
                player.sendMessage("§cVocê não tem casas salvas.");
            }

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void teleportToHome(Player player, String homeName) {
        String uuid = player.getUniqueId().toString();
        try (Connection conn = SyntriPlugin.getInstance().getBackend().getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {

            stmt.setString(1, uuid);
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();

            if (rs != null && rs.next()) {
                String worldName = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");

                if (player.getServer().getWorld(worldName) != null) {
                    player.teleport(player.getServer().getWorld(worldName).getBlockAt((int) x, (int) y, (int) z).getLocation());
                    player.sendMessage("§aVocê foi teleportado para a casa " + homeName + ".");
                } else {
                    player.sendMessage("§cO mundo da casa não foi encontrado.");
                }
            } else {
                player.sendMessage("§cVocê não tem uma casa salva com o nome: " + homeName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
