package com.br.gabrielmartins.engine.data.factory;

import com.br.gabrielmartins.commons.backend.BackendType;
import com.br.gabrielmartins.syntri.backend.firebird.FirebirdBackend;
import com.br.gabrielmartins.syntri.backend.hikari.Hikari;
import com.br.gabrielmartins.syntri.backend.mariadb.MariaDBBackend;
import com.br.gabrielmartins.syntri.backend.mongo.MongoBackend;
import com.br.gabrielmartins.syntri.backend.oracle.OracleBackend;
import com.br.gabrielmartins.syntri.backend.postgresql.PostgreSQLBackend;
import com.br.gabrielmartins.syntri.backend.sqllite.SQLiteBackend;
import com.br.gabrielmartins.syntri.backend.sqlserver.SQLServerBackend;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class BackendFactory {

    public static Backend buildSQLBackend(ConfigurationSection cfg, File dataFolder) {
        BackendType type = BackendType.fromString(cfg.getString("type", "sqlite"));
        String host = cfg.getString("host");
        int port = cfg.getInt("port");
        String db = cfg.getString("database");
        String user = cfg.getString("username");
        String pass = cfg.getString("password");

        switch (type) {
            case MYSQL:
                return new Hikari(host, port, db, user, pass);
            case POSTGRESQL:
                return new PostgreSQLBackend(host, port, db, user, pass);
            case MARIADB:
                return new MariaDBBackend(host, port, db, user, pass);
            case ORACLE:
                return new OracleBackend(host, port, cfg.getString("service"), user, pass);
            case FIREBIRD:
                return new FirebirdBackend(host, port, db, user, pass);
            case SQLSERVER:
                return new SQLServerBackend(host, port, db, user, pass);
            case SQLITE:
            default:
                return new SQLiteBackend(new File(new File(dataFolder, cfg.getString("file", "database.db")).getPath()));
        }
    }

    public static MongoBackend buildMongoBackend(ConfigurationSection cfg) {
        return new MongoBackend(cfg.getString("uri"), cfg.getString("database"));
    }
}
