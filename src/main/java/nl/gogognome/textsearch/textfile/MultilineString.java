package nl.gogognome.textsearch.textfile;

class MultilineString {

    private final String data;
    private final int dataLength;

    public MultilineString(String data) {
        this.data = data;
        this.dataLength = data.length();
    }

    public int getStartOfLine(int index) {
        while (index > 0 && !isNewLine(index-1)) {
            index--;
        }
        return index;
    }

    public int getEndOfLineExcludingNewLine(int index) {
        if (index < dataLength && isNewLine(index)) {
            return index;
        }
        while (index+1 < dataLength && !isNewLine(index+1)) {
            index++;
        }
        return index+1;
    }

    public int getEndOfLineIncludingNewLine(int index) {
        index = getEndOfLineExcludingNewLine(index);

        // include up to two different newline characters
        if (index < dataLength && isNewLine(index)) {
            index++;
        }
        if (index < dataLength && isNewLine(index) && data.charAt(index-1) != data.charAt(index)) {
            index++;
        }

        return index;
    }

    public boolean isNewLine(int index) {
        char c = data.charAt(index);
        return c == '\n' || c == '\r';
    }

}
