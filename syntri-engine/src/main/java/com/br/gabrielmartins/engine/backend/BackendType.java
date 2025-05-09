package com.br.gabrielmartins.engine.backend;

public enum BackendType {

    MYSQL,
    SQLITE,
    MONGODB,
    POSTGRESQL,
    MARIADB,
    ORACLE,
    FIREBIRD,
    REDIS,
    SQLSERVER;

    public static BackendType fromString(String value) {
        try {
            return BackendType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
