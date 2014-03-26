package de.metalcon.sdd.testBandRecordUser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class CsvIteratable implements Iterable<String[]> {

    public class CsvIterator implements Iterator<String[]> {

        private String[] lines;

        private int index;

        public CsvIterator(
                Path csvPath,
                String fieldSep,
                String lineSep) throws IOException {
            StringBuffer contents = new StringBuffer();
            for (String line : Files.readAllLines(csvPath,
                    Charset.defaultCharset())) {
                contents.append(line);
                contents.append("\n");
            }

            lines = contents.toString().split(lineSep);
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index != lines.length;
        }

        @Override
        public String[] next() {
            return lines[index++].split(fieldSep);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private Path csvPath;

    private String fieldSep;

    private String lineSep;

    public CsvIteratable(
            Path csvPath,
            String fieldSep,
            String lineSep) {
        this.csvPath = csvPath;
        this.fieldSep = fieldSep;
        this.lineSep = lineSep;
    }

    @Override
    public Iterator<String[]> iterator() {
        try {
            return new CsvIterator(csvPath, fieldSep, lineSep);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
