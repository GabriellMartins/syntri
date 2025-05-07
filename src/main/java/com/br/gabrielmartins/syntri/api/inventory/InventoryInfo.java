package com.br.gabrielmartins.syntri.api.inventory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InventoryInfo {
    String name();
    int size() default 27;
    String title() default "Inventário";
}
