package com.br.gabrielmartins.syntri.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String[] names();
    String[] permission() default {};
}
