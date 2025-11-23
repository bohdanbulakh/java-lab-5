package org.example;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class DecipherReader extends FilterReader {
    private final int keyOffset;

    public DecipherReader(Reader in, char keyChar) {
        super(in);
        this.keyOffset = keyChar;
    }

    @Override
    public int read() throws IOException {
        int originalChar = super.read();

        if (originalChar != -1) {
            return originalChar - keyOffset;
        }
        return -1;
    }
}