package Note.Parser;

import Note.Note;
public class NoteParser {
    public static Note parse(final CharSource source) {
        return new NParser(source).parseNote();
    }

    public Note parse(String note) {
        return parse(new StringSource(note));
    }

    private static class NParser extends BaseParser {
        public NParser(CharSource source) {
            super(source);
        }

        public Note parseNote() {
            skipWhitespace();
            int number = parseNumber();
            take('.');
            skipWhitespace();
            String text = parseText();
            return new Note(text, number);
        }

        private int parseNumber() {
            final StringBuilder number = new StringBuilder();
            final int c;
            skipWhitespace();
            //Если число начинается на ноль, то это ноль
            if (take('0')) {
                return 0;
            }
            //Пока можем парсим константу
            while (between('0', '9')) {
                number.append(take());
            }
            //Пробуем парсить int
            //Если не получалось, кидаем исключение
            try {
                c = Integer.parseInt(number.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("bad number");
            }
            return c;
        }
        public String parseText() {
            skipWhitespace();
            StringBuilder sb = new StringBuilder();
            take('{');
            while (!take('}')) {
                sb.append(take());
            }
            return sb.toString();
        }
        private void skipWhitespace() {
            while (testString(" \t\u000B\u2029\f" + System.lineSeparator())) {
                take();
            }
        }
    }
}
