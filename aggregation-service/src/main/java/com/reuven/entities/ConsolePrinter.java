package com.reuven.entities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.io.Writer;

public class ConsolePrinter extends Writer {

    private static final Logger logger = LogManager.getLogger(ConsolePrinter.class);

    @Override
    public void write(char[] cbuf, int off, int len) {
        //TODO impl
    }

    @Override
    public void write(int value) {
        logger.info("aggregated value is: {}", value);
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
