package Note;

import java.io.*;

public class BestScanner {
    public Reader inputStream;
    final int bufferSize = 2048;
    private char[] buffer = new char[bufferSize];
    private boolean bufferReaded = false;
    private boolean checkNextInt = false;
    StringBuilder niceWord = new StringBuilder();
    StringBuilder line = new StringBuilder();
    private int iter = 0;
    private int reading = 0;
    final char[] sep = System.lineSeparator().toCharArray();
    int strCount = 1;
    private boolean lastR = false;

    public BestScanner(String input, String encoding)
            throws FileNotFoundException, UnsupportedEncodingException {
        this.inputStream = new BufferedReader(
                new InputStreamReader(new FileInputStream(input), encoding));
    }

    public BestScanner(String input) {
        this.inputStream = new BufferedReader(new StringReader(input));
    }

    public BestScanner(InputStream input) {
        this.inputStream = new BufferedReader(new InputStreamReader(input));
    }

    //Закрываем файл
    public void close() {
        try {
            this.inputStream.close();
        } catch (IOException e) {
            System.out.println("Can't close");
        }
    }

    //Чтение
    private int read() {
        try {
            this.iter = 0;
            this.buffer = new char[this.bufferSize];
            this.reading = this.inputStream.read(this.buffer);
        } catch (IOException e) {
            System.out.println("Can't read");
        }
        return this.reading;
    }

    //Спрашиваем, есть ли дальше символы
    public boolean hasNext() {
        boolean flag = false;
        if (!this.bufferReaded | this.iter >= this.bufferSize) {
            if (read() != -1) {
                this.bufferReaded = true;
                flag = true;
            } else {
                this.bufferReaded = false;
            }
        } else {
            if (!isCharEmpty(this.buffer[this.iter])) {
                flag = true;
            }
        }
        return flag;
    }

    private boolean isCharEmpty(char sym) {
        return sym == '\u0000';
    }

    //Проверяем символ на separator
    private boolean isSep(char sym) {
        boolean isS = false;
        for (char sep : this.sep) {
            if (sym == sep) {
                isS = true;
                break;
            }
        }
        return isS;
    }

    //Возвращаем следующую строку
    public String getNextLine() {
// Считываем строку
        this.line.setLength(0);
        while (hasNext()) {
            while (iter < bufferSize) {
                if (!(isSep(buffer[iter])) & !isCharEmpty(buffer[iter])) {
                    this.line.append(buffer[iter]);
// Обработка исключения
                } else if (iter < bufferSize - 1 & iter > 0) {
                    if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                        iter++;
                        return this.line.toString();
                    }
                } else if (iter == 0) {
                    if (buffer[iter] != '\r' & lastR) {
                        lastR = false;
                    } else {
                        if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                            iter++;
                            return this.line.toString();
                        }
                    }
                } else if (iter == bufferSize - 1) {
                    if (isSep(buffer[iter])) {
                        if (buffer[iter] == '\r') {
                            lastR = true;
                        }
                        iter++;
                        return this.line.toString();
                    } else if (isCharEmpty(buffer[iter])) {
                        if (!isSep(buffer[iter - 1])) {
                            iter++;
                            return this.line.toString();
                        }
                    }
                }
                iter++;
            }
        }
        if (!this.line.isEmpty()) {
            return this.line.toString();
        } else {
            return "\u0000";
        }
    }

    //Спрашиваем, есть ли дальше число
    public boolean hasNextInt() {
        boolean flag = false;
        String nextToken = getNext();
        //Производить парсинг слова в строку, если получается - true
        //иначе - false
        if (!(nextToken.isEmpty())) {
            try {
                int token = Integer.parseInt(nextToken);
                flag = true;
            } catch (NumberFormatException e) {
                flag = false;
            }
        }
        this.checkNextInt = true;
        return flag;
    }

    //Возвращаем следующее число
    public int getNextInt() {
        int token = 0;
        if (this.checkNextInt) {
            token = Integer.parseInt(niceWord.toString());
            this.checkNextInt = false;
            this.niceWord.setLength(0);
        } else {
            try {
                token = Integer.parseInt(getNext());
            } catch (NumberFormatException e) {
                System.out.println("Can't parse");
            }
        }
        return token;
    }

    //Считываем следующее что-то
    public String getNext() {
        boolean flag = false;
        //Проверяем, считывали ли мы что-то в буфер
        if (this.checkNextInt) {
            this.checkNextInt = false;
            return this.niceWord.toString();
        }
        //Считываем слово
        this.niceWord.setLength(0);
        while (hasNext()) {
            while (iter < bufferSize) {
                flag = false;
                if (!Character.isWhitespace(buffer[iter]) & !isCharEmpty(buffer[iter])) {
                    this.niceWord.append(buffer[iter]);
                } else {
                    //Обработка исключения
                    if (isSep(buffer[iter])) {
                        if (iter < bufferSize - 1 & iter > 0) {
                            if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                                strCount++;
                                flag = true;
                            }
                        } else if (iter == 0) {
                            if (buffer[iter] != '\r' & lastR) {
                                lastR = false;
                            } else {
                                if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                                    strCount++;
                                    flag = true;
                                }
                            }
                        } else if (iter == bufferSize - 1) {
                            if (buffer[iter] == '\r') {
                                lastR = true;
                            }
                            strCount++;
                            flag = true;
                        }
                    }
                    if (!this.niceWord.isEmpty()) {
                        if (flag) {
                            strCount--;
                        }
                        return this.niceWord.toString();
                    }
                }
                iter++;
            }
        }
        if (!this.niceWord.isEmpty()) {
            return this.niceWord.toString();
        } else {
            return "\u0000";
        }
    }

    //Считываем следующее слово
    public String getNextWord() {
        boolean flag = true;
        //Считываем слово
        this.niceWord.setLength(0);
        while (hasNext()) {
            while (iter < bufferSize) {
                flag = false;
                if (Character.isLetter(buffer[iter]) |
                        Character.getType(buffer[iter]) == Character.DASH_PUNCTUATION |
                        buffer[iter] == '\'') {
                    this.niceWord.append(buffer[iter]);
                } else {
                    //Обработка исключения
                    if (isSep(buffer[iter])) {
                        if (iter < bufferSize - 1 & iter > 0) {
                            if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                                strCount++;
                                flag = true;
                            }
                        } else if (iter == 0) {
                            if (buffer[iter] != '\r' & lastR) {
                                lastR = false;
                            } else {
                                if (buffer[iter] != '\r' | buffer[iter + 1] != '\n') {
                                    strCount++;
                                    flag = true;
                                }
                            }
                        } else if (iter == bufferSize - 1) {
                            if (buffer[iter] == '\r') {
                                lastR = true;
                            }
                            strCount++;
                            flag = true;
                        }
                    }
                    if (!this.niceWord.isEmpty()) {
                        if (flag) {
                            strCount--;
                        }
                        return this.niceWord.toString();
                    }
                }
                iter++;
            }
        }
        return this.niceWord.toString();
    }
}
