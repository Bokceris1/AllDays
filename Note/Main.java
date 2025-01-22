package Note;

import Note.Parser.NoteParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void writeToConst(int number) {
        try(Writer numWr = new OutputStreamWriter(
                new FileOutputStream("src/Note/constants"), "utf-8")) {
            numWr.write(number + "");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    public static void main(String[] args) {
        String newWord;
        int number;
        int last = 1;
        int l = 1;
        boolean inNote = false;
        Note note = new Note();
        BestScanner input = new BestScanner(System.in);
        StringBuilder update = new StringBuilder();
        List<Note> notes = new ArrayList<>();

        //Interface
        System.out.println("/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/" + "\n");
        System.out.println("Здравствуйте! Вы находитесь в *лучшей* программе для заметок версии Nedo-Alpha 0.52.0!" + "\n");
        System.out.println("____----                                                                           ||\\\\              ||");
        System.out.println("Команды:                                                                           ||  \\\\            ||");
        System.out.println("1. clear -> удалить содежимое файла.                                               ||    \\\\          ||");
        System.out.println("2. start -> начать ввод заметки.                                                   ||      \\\\        ||");
        System.out.println("3. end -> закончить ввод заметки.                                                  ||        \\\\      ||");
        System.out.println("4. file -> вывести содержимое файла.                                               ||          \\\\    ||    _   ___  ___   _,                                    ");
        System.out.println("5. quit -> *выйти из лучшей* программы для заметок?                                ||            \\\\  ||   | |   |   |--  |_                    ");
        System.out.println("6. delete <номер заметки> -> удалить заметку.                                      ||              \\\\||   \\_/   |   |__  ._/       "
                + "\n__-_-___\n");
        System.out.println("@!______________________________!@");
        System.out.println("Ваш отзыв очень важен для нас!");
        System.out.println("Пожалуйста, оставьте его при себе!");
        System.out.println("@!______________________________!@" + "\n");
        System.out.println("/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/~/");
        //
        //Bag: } не должна приниматься в качестве символа

        try (Writer writer = new PrintWriter(new FileOutputStream("src/Note/new_file", true))) {
            BestScanner numRead = new BestScanner("src/Note/constants", "utf-8");
            number = numRead.getNextInt();
            while (!(newWord = input.getNext()).equals("quit")) {
                if (!inNote) {
                    if (newWord.equals("clear")) {
                        Writer delete = new OutputStreamWriter(
                                new FileOutputStream("src/Note/new_file"), "utf-8");
                        delete.write("");
                        delete.close();
                        update.setLength(0);
                        number = 1;
                        writeToConst(number);
                    } else if (newWord.equals("start")) {
                        note.clear();
                        note.changeNumber(number);
                        inNote = true;
                    } else if (newWord.equals("file")) {
                        BestScanner sc = new BestScanner("src/Note/new_file", "utf-8");
                        while (sc.hasNext()) {
                            System.out.println("||   " + sc.getNextLine());
                        }
                        BestScanner sc_update = new BestScanner(update.toString());
                        while (sc_update.hasNext()) {
                            System.out.println("||   " + sc_update.getNextLine());
                        }
                        sc_update.close();
                    } else if (newWord.equals("delete")) {
                        int delNum;
                        if (input.hasNextInt()) {
                            delNum = input.getNextInt();
                            //System.out.println(delNum);
                        } else {
                            System.out.println("Вместо номера заметки вы ввели что-то странное");
                            continue;
                        }
                        BestScanner sc = new BestScanner("src/Note/new_file", "utf-8");
                        StringBuilder sb = new StringBuilder();
                        while (sc.hasNext()) {
                            String line = sc.getNextLine();
                            sb.append(line).append("\n");
                            //System.out.println(line);
                            if (line.charAt(line.length() - 1) == '}') {
                                Note lNote = new NoteParser().parse(sb.toString());
                                //System.out.println(delNum + " " + lNote.getNumber());
                                if (lNote.getNumber() != delNum) {
                                    notes.add(lNote);
                                }
                                sb.setLength(0);
                            }
                        }
                        sc.close();

                        //System.out.println(notes);
                        Writer add = new OutputStreamWriter(
                                new FileOutputStream("src/Note/new_file"), "utf-8");
                        l = 1;
                        for (Note n : notes) {
                            //System.out.println(n.toString());
                            n.changeNumber(l);
                            l += 1;

                            add.write(n.toString());
                        }
                        add.close();

                        String up = update.toString();
                        BestScanner sc2 = new BestScanner(up);
                        update.setLength(0);

                        int p = l;
                        while (sc2.hasNext()) {
                            String line = sc2.getNextLine();
                            sb.append(line).append("\n");
                            if (line.charAt(line.length() - 1) == '}') {
                                Note lNote = new NoteParser().parse(sb.toString());
                                if (lNote.getNumber() != delNum) {
                                    lNote.changeNumber(p);
                                    p += 1;
                                    update.append(lNote);
                                }
                                sb.setLength(0);
                            }
                        }
                        sc2.close();

                        number -= 1;
                        writeToConst(number);
                    }
                } else {
                    if (newWord.equals("end")) {
                        System.out.println("Ничего себе, заметка кончилась");
                        update.append(note);
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
            writeToConst(number);
            System.out.println("Вы вышли из *лучшей* программы для заметок :(");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
