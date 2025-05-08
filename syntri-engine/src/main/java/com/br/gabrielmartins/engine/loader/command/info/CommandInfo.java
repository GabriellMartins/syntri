package com.br.gabrielmartins.engine.loader.command.info;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String[] names();
    String[] permission() default {};
}
