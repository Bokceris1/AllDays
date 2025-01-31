package Note;

import Note.Parser.NoteParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static void readFromFile (String prefix, String fileAddress) {
        try (BestScanner scanner = new BestScanner(fileAddress, "utf-8")) {
            while (scanner.hasNext()) {
                System.out.println(prefix + scanner.getNextLine());
            }
        } catch (IOException ioe) {
            System.out.println("IOException in reading " + ioe.getMessage());
        }
    }
    /*
    Метод readFromFile читает содержимое файла и выводит его на экран,
    при этом к каждой строке прибавляется prefix
     */

    public static void init() {
        readFromFile("", "src/Note/Menu");
    }
    /*
    Метод init выводит на экран начальный интерфейс
     */

    public static void start(Note note, int number) {
        note.clear();
        note.changeNumber(number);
    }
    /*
    Метод start готовит заметку к вводу
     */

    public static void file(String fileAddress, String update) {
        readFromFile("||   ", fileAddress);
        try (BestScanner sc_update = new BestScanner(update)) {
            while (sc_update.hasNext()) {
                System.out.println("||   " + sc_update.getNextLine());
            }
        } catch (IOException ioe) {
            System.out.println("IOException in reading " + ioe.getMessage());
        }
    }
    /*
    Метод file производит вывод всех заметок из базы данных и обновления
    */

    public static void delete(BestScanner input, List<Note> notes, StringBuilder update) {
        int delNum;
        int l = 1;
        if (input.hasNextInt()) {
            delNum = input.getNextInt();

            try {
                BestScanner sc = new BestScanner("src/Note/new_file", "utf-8");
                readNotes(sc, delNum, notes, update, WriteMode.SAVED, l);
                sc.close();

                Writer add = new OutputStreamWriter(
                        new FileOutputStream("src/Note/new_file"), "utf-8");
                l = 1;
                for (Note n : notes) {
                    n.changeNumber(l);
                    l += 1;
                    add.write(n.toString());
                }
                add.close();

                String up = update.toString();
                BestScanner sc2 = new BestScanner(up);
                update.setLength(0);

                readNotes(sc2, delNum, notes, update, WriteMode.UPDATE, l);
                sc2.close();
            } catch (IOException ioe) {
                System.out.println("IOException in delete " + ioe.getMessage());
            }
        } else {
            System.out.println("Вместо номера заметки вы ввели что-то странное");
        }
    }
    /*
    Метод delete удаляет из базы данных(notes) и обновления (update) заметку с заданным номером (delNum)
    Номер задаётся сразу после команды
    */

    public static void end(StringBuilder update, Note note, List<Note> upNotes) {
        update.append(note);
        upNotes.add(note);
        System.out.println("Ничего себе, заметка кончилась");
    }
    /*
    Метод end записывает заметку(note) в обновление(update)
    */

    public static void readNotes (BestScanner scanner, int delNum, List<Note> notes,
                                     StringBuilder update, WriteMode mode, int numNow) {
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            String line = scanner.getNextLine();
            sb.append(line).append("\n");
            if (line.charAt(line.length() - 1) == '}') {
                Note lNote = new NoteParser().parse(sb.toString());
                if (lNote.getNumber() != delNum) {
                    if (mode == WriteMode.SAVED) {
                        notes.add(lNote);
                    } else {
                        lNote.changeNumber(numNow);
                        numNow += 1;
                        update.append(lNote);
                    }
                }
                sb.setLength(0);
            }
        }
    }
    /*
    Метод readNotes производит парсинг всех заметок из базы данных
     */

    public static void writeToFile(String fileAddress, String text) {
        try (Writer newWriter = new OutputStreamWriter(
                new FileOutputStream(fileAddress), "utf-8")) {
            newWriter.write(text);
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    /*
    Метод writeToFile производит запись текста в файл по адресу
    */

    public  static void appendToFile(String fileAddress, String text) {
        try (Writer newWriter = new FileWriter("src/Note/new_file",
                StandardCharsets.UTF_8, true)) {
            newWriter.write(text);
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    /*
    appendToFile производит запись текста без удаления прошлых данных
     */

    public static void clear(String fileAddress) {
        writeToFile(fileAddress, "");
    }
    /*
    Метод clear очищает файл по адресу
     */

    public static void main(String[] args) {
        String newWord;
        int number = 1;
        int last = 1;
        boolean inNote = false;
        Note note = new Note();
        StringBuilder update = new StringBuilder();
        List<Note> notes = new ArrayList<>();
        BestScanner input = new BestScanner(System.in);
        List<Note> upNotes = new ArrayList<>();

        //Interface
        init();

        //Bag: } не должна приниматься в качестве символа

        /*
        Чтение содержимого файла.
         */

//        int finalNumber = number;
//        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//            public void run() {
//                if (!update.isEmpty()) {
//                    appendToFile("src/Note/new_file", update.toString());
//                    writeToFile("src/Note/constants", notes.size() + upNotes.size() + 1 + "");
//                    System.out.println("Записано");
//                } else {
//                    System.out.println("empty");
//                }
//            }
//        }, "Shutdown-thread"));
        /*
        Поток позволяющий выполнить запись в файл после закрытия программы. В разработке...
         */

        try (Writer writer = new FileWriter("src/Note/new_file", StandardCharsets.UTF_8, true)) {
            BestScanner numRead = new BestScanner("src/Note/constants", "utf-8");
            number = numRead.getNextInt();
            while (!(newWord = input.getNext()).equals("quit")) {
                if (!inNote) {
                    if (newWord.equals("clear")) {
                        clear("src/Note/new_file");
                        update.setLength(0);
                        number = 1;
                        writeToFile("src/Note/constants", number + "");
                    } else if (newWord.equals("start")) {
                        start(note, number);
                        inNote = true;
                    } else if (newWord.equals("file")) {
                        file("src/Note/new_file", update.toString());
                    } else if (newWord.equals("delete")) {
                        delete(input, notes, update);
                        number -= 1;
                        writeToFile("src/Note/constants", number + "");
                    }
                } else {
                    if (newWord.equals("end")) {
                        end(update, note, upNotes);
                        inNote = false;
                        number += 1;
                    } else {
                        int now = input.getStrCount();
                        if (last < now) {
                            note.addText("\n   ");
                        }
                        note.addText(newWord);
                    }
                }
                last = input.getStrCount();
            }
            writer.write(update.toString());
            writeToFile("src/Note/constants", number + "");
            System.out.println("Вы вышли из *лучшей* программы для заметок :(");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
