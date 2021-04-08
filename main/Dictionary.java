package main;

import java.io.*;
import java.util.ArrayList;

class Dictionary {
    String name;
    private String pathDist;
    ArrayList<Word> words = new ArrayList<>();

    static String mainPath = EnglishDictionaries.mainPath + "\\dictionaries";
    static {
        File dir =  new File(mainPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    Dictionary(String name) {
        this.name = name;
        pathDist = mainPath + "\\" + name + ".txt";
    }

    public void createFile() {
        File file = new File(pathDist);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                EnglishDictionaries.systemExit(e);
            }
        }
    }

    public void readFile(){
        createFile();

        try (FileInputStream fileInputStream = new FileInputStream(pathDist);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] splitInput = input.split("::");
                try {
                    words.add(new Word(splitInput[0], Word.getDateFromArgument(splitInput[1])));
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            EnglishDictionaries.systemExit(e);
        }
    }

    public void writeFile() {
        createFile();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathDist))){
            for (Word word : words) {
                bufferedWriter.write(word.getWord() + "::" + Word.getDateInTheRightFormat(word.getCreateDate()));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Записать словарь не удалось!");
        }
    }

    public boolean deleteFile() {
        File file = new File(pathDist);
        try {
            return file.delete();
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dictionary other = (Dictionary) obj;
        if (!name.equals(other.name))
            return false;
        return true;
    }
}
