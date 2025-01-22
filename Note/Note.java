package Note;

public class Note {

    private String text;
    private int number;

    public Note(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public Note(int number) {
        this.text = "";
        this.number = number;
    }

    public Note() {
        this.text = "";
        this.number = 1;
    }

    public int getNumber() {return number;}

    public String getText() {
        return text;
    }

    public void changeNumber(int number) {
        this.number = number;
    }

    public void clear() {
        this.text = "";
        this.number = 1;
    }

    public void addText(String text) {
        this.text += " " + text;
    }

    public String toString() {
        return number + ". " + "{" + text + "}\n";
    }
}
