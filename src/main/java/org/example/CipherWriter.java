package org.example;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class CipherWriter extends FilterWriter {
    private final int keyOffset;

    public CipherWriter(Writer out, char keyChar) {
        super(out);
        this.keyOffset = keyChar;
    }

    @Override
    public void write(int c) throws IOException {
        int cipheredChar = c + keyOffset;
        super.write(cipheredChar);
    }
}