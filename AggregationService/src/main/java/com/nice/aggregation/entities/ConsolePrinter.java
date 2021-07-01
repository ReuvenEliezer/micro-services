package com.nice.entities;

import java.io.IOException;
import java.io.Writer;

public class ConsolePrinter extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) {
        //TODO impl
    }

    @Override
    public void write(int value) {
        System.out.println("aggregated value is: " + value);
    }

    @Override
    public void flush() throws IOException {
        //TODO impl
    }

    @Override
    public void close() throws IOException {
        //TODO impl
    }
}
