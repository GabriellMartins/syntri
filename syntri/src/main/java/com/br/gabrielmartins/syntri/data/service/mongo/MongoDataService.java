package com.br.gabrielmartins.syntri.data.service.mongo;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.service.DataService;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Filters.*;

public class MongoDataService implements DataService {

    private final MongoDatabase db;

    public MongoDataService(MongoDatabase db) {
        this.db = db;
    }

    @Override
    public void saveHome(Player player, String homeName) {
        Document doc = new Document("uuid", player.getUniqueId().toString())
                .append("home_name", homeName)
                .append("world", player.getWorld().getName())
                .append("x", player.getLocation().getX())
                .append("y", player.getLocation().getY())
                .append("z", player.getLocation().getZ());

        getHomes().replaceOne(and(
                eq("uuid", player.getUniqueId().toString()),
                eq("home_name", homeName)
        ), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }

    @Override
    public void saveWarp(Player player, String warpName) {
        Document doc = new Document("uuid", player.getUniqueId().toString())
                .append("warp_name", warpName)
                .append("world", player.getWorld().getName())
                .append("x", player.getLocation().getX())
                .append("y", player.getLocation().getY())
                .append("z", player.getLocation().getZ());

        getWarps().replaceOne(and(
                eq("uuid", player.getUniqueId().toString()),
                eq("warp_name", warpName)
        ), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }

    @Override
    public void listHomes(Player player) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();
        var docs = getHomes().find(eq("uuid", player.getUniqueId().toString()));

        if (!docs.iterator().hasNext()) {
            player.sendMessage(mm.getMessage("home.none"));
            return;
        }

        player.sendMessage(mm.getMessage("home.list_header"));
        for (Document doc : docs) {
            player.sendMessage(mm.getMessage("home.list_item").replace("%name%", doc.getString("home_name")));
        }
    }

    @Override
    public void listWarps(Player player) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();
        var docs = getWarps().find(eq("uuid", player.getUniqueId().toString()));

        if (!docs.iterator().hasNext()) {
            player.sendMessage(mm.getMessage("warp.none"));
            return;
        }

        player.sendMessage(mm.getMessage("warp.list_header"));
        for (Document doc : docs) {
            player.sendMessage(mm.getMessage("warp.list_item").replace("%name%", doc.getString("warp_name")));
        }
    }

    @Override
    public void teleportToHome(Player player, String homeName) {
        Document doc = getHomes().find(and(
                eq("uuid", player.getUniqueId().toString()),
                eq("home_name", homeName)
        )).first();

        teleport(player, doc, homeName, "home");
    }

    @Override
    public void teleportToWarp(Player player, String warpName) {
        Document doc = getWarps().find(and(
                eq("uuid", player.getUniqueId().toString()),
                eq("warp_name", warpName)
        )).first();

        teleport(player, doc, warpName, "warp");
    }

    @Override
    public void createTables() {
        // Não aplicável ao MongoDB
    }

    private MongoCollection<Document> getHomes() {
        return db.getCollection("homes");
    }

    private MongoCollection<Document> getWarps() {
        return db.getCollection("warps");
    }

    private void teleport(Player player, Document doc, String name, String tipo) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (doc == null) {
            player.sendMessage(mm.getMessage(tipo + ".not_found").replace("%" + tipo + "%", name));
            return;
        }

        String world = doc.getString("world");
        double x = doc.getDouble("x");
        double y = doc.getDouble("y");
        double z = doc.getDouble("z");

        if (Bukkit.getWorld(world) != null) {
            player.teleport(Bukkit.getWorld(world).getBlockAt((int) x, (int) y, (int) z).getLocation());
            player.sendMessage(mm.getMessage(tipo + ".teleported").replace("%" + tipo + "%", name));
        } else {
            player.sendMessage(mm.getMessage(tipo + ".world_not_found"));
        }
    }
}
