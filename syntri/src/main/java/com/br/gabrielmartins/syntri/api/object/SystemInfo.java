package com.br.gabrielmartins.syntri.api.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.Bukkit;


public class SystemInfo {

    public static String getMinecraftVersion() {
        try {
            String info = Bukkit.getVersion();
            return info.split("MC: ")[1].split("\\)")[0];
        } catch (Throwable e) {
            return "Desconhecida";
        }
    }


    public static long getFreeMemoryComputer() {
        try {
            OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
            Method getFreeMemory = system.getClass().getMethod("getFreePhysicalMemorySize");
            getFreeMemory.setAccessible(true);
            return (long) getFreeMemory.invoke(system);
        } catch (Throwable e) {
            return -1;
        }
    }

    public static long getTotalMemoryComputer() {
        try {
            OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
            Method getTotalMemory = system.getClass().getMethod("getTotalPhysicalMemorySize");
            getTotalMemory.setAccessible(true);
            return (long) getTotalMemory.invoke(system);
        } catch (Throwable e) {
            return -1;
        }
    }

    public static long getFreeSpaceComputer() {
        long space = 0;
        for (File f : File.listRoots()) {
            space += f.getFreeSpace();
        }
        return space;
    }

    public static long getTotalSpaceComputer() {
        long space = 0;
        for (File f : File.listRoots()) {
            space += f.getTotalSpace();
        }
        return space;
    }

}