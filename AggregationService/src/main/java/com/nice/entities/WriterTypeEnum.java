package com.nice.entities;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public enum WriterTypeEnum {
    PRINT_WRITER("PrintWriter", PrintWriter.class),
    CONSOLE_WRITER("ConsoleWriter", ConsolePrinter.class),
    ;

    private Class<? extends Writer> writer;
    private String writerClassName;

    WriterTypeEnum(String writerClassName, Class<? extends Writer> writer) {
        this.writer = writer;
        this.writerClassName = writerClassName;
    }

    private static Map<String, Class<? extends Writer>> map = new HashMap<>();

    static {
        for (WriterTypeEnum type : WriterTypeEnum.values()) {
            map.put(type.writerClassName, type.writer);
        }
    }

    public static Class<? extends Writer> getWriter(String writerClassName) {
        return map.get(writerClassName);
    }

}
