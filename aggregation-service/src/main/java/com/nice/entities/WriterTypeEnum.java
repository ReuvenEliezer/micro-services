package com.nice.entities;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public enum WriterTypeEnum {
    PRINT_WRITER("PrintWriter", PrintWriter.class),
    CONSOLE_WRITER("ConsoleWriter", ConsolePrinter.class),
    ;

    private final Class<? extends Writer> writer;
    private final String writerClassName;

    WriterTypeEnum(String writerClassName, Class<? extends Writer> writer) {
        this.writer = writer;
        this.writerClassName = writerClassName;
    }

    private static final Map<String, Class<? extends Writer>> map = new HashMap<>();

    static {
        for (WriterTypeEnum type : WriterTypeEnum.values()) {
            map.put(type.writerClassName, type.writer);
        }
    }

    public static Class<? extends Writer> getWriter(String writerClassName) {
        return map.get(writerClassName);
    }

}
