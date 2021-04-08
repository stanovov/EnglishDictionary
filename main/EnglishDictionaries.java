package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class EnglishDictionaries {
    static String mainPath = System.getProperty("user.dir") + "\\src\\resources";
    static String dirDictionariesFile = mainPath + "\\dictionaries.txt";
    static ArrayList<Dictionary> dictionaries = new ArrayList<>();
    static Dictionary currentDictionary;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("Доброе пожаловать в словарь!");
        System.out.println("Для выхода, в любой момент введите \"exit\"");

        File dictionariesFile = new File(dirDictionariesFile);
        if (!dictionariesFile.exists()) {
            try {
                dictionariesFile.createNewFile();
            } catch (IOException e) {
                systemExit(e);
            }
        }

        try (FileInputStream fileInputStream = new FileInputStream(dirDictionariesFile);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] splitInput = input.split("::");
                Dictionary dictionary = new Dictionary(splitInput[0]);
                dictionary.readFile();
                dictionaries.add(dictionary);
                if (splitInput.length > 1 && splitInput[1].equals("*")) {
                    currentDictionary = dictionary;
                }
            }
        } catch (IOException e) {
            systemExit(e);
        }

        if (dictionaries.size() == 1 && currentDictionary == null) {
            currentDictionary = dictionaries.get(0);
        }

        while (true) {
            if (dictionaries.size() == 0) {
                createDictionary();
            } else if (currentDictionary == null) {
                createOrSelect();
            }

            System.out.println("Выбран словарь - " + currentDictionary);

            while (true) {
                System.out.println(
                        "Начать вводить слова - \"start\"; Вывести список слов - \"show\"; " +
                        "Очистить словарь - \"clear\"; Удалить словарь - \"delete\"; " +
                        "Назад - \"back\""
                );

                try {
                    String input = reader.readLine();
                    systemExit(input);
                    if (input.equals("start")) {
                        System.out.println("Для остановки ввода, введите пустую строку!");
                        System.out.println("Проверить, есть ли такое слово - \"get\"; Удалить слово - \"delete\"");
                        while (true) {
                            input = reader.readLine().toLowerCase(Locale.ROOT);
                            systemExit(input);
                            if (input.isEmpty()) {
                                System.out.println("Ввод закончен!");
                                break;
                            } else if (input.contains("delete ")) {
                                if (input.length() == 7) {
                                    System.out.println("Неверный формат. После ключевого слова \"delete\" необходимо через пробел написать слово.");
                                } else {
                                    String searchWord = input.substring(7);
                                    int index = currentDictionary.words.indexOf(new Word(searchWord));
                                    if (index == -1) {
                                        System.out.println("Слово - \"" + searchWord + "\" не найдено! Попробуйте снова.");
                                        continue;
                                    }
                                    System.out.println("Слово - \"" + searchWord + "\" удалено!");
                                    currentDictionary.words.remove(index);
                                }
                            } else if (input.contains("get ")) {
                                if (input.length() == 4) {
                                    System.out.println("Неверный формат. После ключевого слова \"get\" необходимо через пробел написать слово.");
                                } else {
                                    String searchWord = input.substring(4);
                                    int index = currentDictionary.words.indexOf(new Word(searchWord));
                                    if (index == -1) {
                                        System.out.println("Слово - \"" + searchWord + "\" не найдено.");
                                        continue;
                                    }
                                    Word word = currentDictionary.words.get(index);
                                    System.out.println("Слово - \"" + searchWord + "\" уже есть в словаре. Создано: " + Word.getDateInTheRightFormat(word.getCreateDate()));
                                }
                            } else {
                                int index = currentDictionary.words.indexOf(new Word(input));
                                if (index >= 0) {
                                    Word word = currentDictionary.words.get(index);
                                    System.out.println("Слово \""+word.getWord()+"\" уже есть! Создано: " + Word.getDateInTheRightFormat(word.getCreateDate()));
                                } else {
                                    currentDictionary.words.add(new Word(input));
                                }
                            }
                        }
                    } else if (input.equals("show")) {
                        for (int i = 0; i < currentDictionary.words.size(); i++) {
                            System.out.println(i+1 + "|\t" + currentDictionary.words.get(i));
                        }
                    } else if (input.equals("clear")) {
                        System.out.println("Словарь \"" + currentDictionary.name + "\" очищен");
                        currentDictionary.words.clear();
                    } else if (input.equals("delete")) {
                        if (currentDictionary.deleteFile()) {
                            dictionaries.remove(dictionaries.indexOf(currentDictionary));
                            System.out.println("Словарь \"" + currentDictionary.name + "\" удален");
                            currentDictionary = null;
                            break;
                        } else {
                            System.out.println("Словарь \"" + currentDictionary.name + "\" удалить не вышло!");
                        }
                    } else if (input.equals("back")) {
                        currentDictionary.writeFile();
                        currentDictionary = null;
                        break;
                    } else {
                        if (input.isEmpty()) {
                            System.out.println("Нужно ввести команду!");
                        } else {
                            System.out.println("Команда введа неверно!");
                        }
                    }
                } catch (IOException e) {
                    systemExit(e);
                }
            }
        }

    }

    static private void createOrSelect() {
        System.out.println("Выберите словарь, из существующих или создайте новый");
        System.out.println("Существующие словари: " + dictionaries.toString());
        try {
            while (true) {
                System.out.println("Выбрать словарь - \"select\" и через пробел имя словаря; Создать новый - \"new\"");
                String input = reader.readLine();
                systemExit(input);
                if(input.contains("select ")) {
                    if (input.length() == 7) {
                        System.out.println("Неверный формат. После ключевого слова \"select\" необходимо через пробел написать имя словаря.");
                    } else {
                        String searchName = input.substring(7);
                        int index = dictionaries.indexOf(new Dictionary(searchName));
                        if (index == -1){
                            System.out.println("Словарь с наименованием - \"" + searchName + "\" не найден! Попробуйте снова.");
                            continue;
                        }
                        currentDictionary = dictionaries.get(index);
                        break;
                    }
                } else if (input.contains("new")) {
                    createDictionary();
                    break;
                } else {
                    System.out.println("Неверный формат, повторите попытку");
                }
            }
        } catch (IOException e) {
            systemExit(e);
        }
    }

    static private void createDictionary() {
        System.out.println("Введите название нового словаря:");
        try {
            while (true) {
                String input = reader.readLine();
                systemExit(input);
                if (input.isEmpty()) {
                    System.out.println("Введена пустая строка, попробуйте снова!");
                    continue;
                }
                currentDictionary = new Dictionary(input);
                currentDictionary.createFile();
                dictionaries.add(currentDictionary);
                break;
            }
        } catch (IOException e) {
            systemExit(e);
        }
    }

    static void systemExit(String input) {
        if (input.equals("exit")) {
            systemExit();
        }
    }

    static void systemExit(Exception e) {
        e.printStackTrace();
        systemExit();
    }

    static void systemExit() {
        try {
            reader.close();
        } catch (IOException ioException) {
        }
        if (currentDictionary != null) {
            currentDictionary.writeFile();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dirDictionariesFile))){
            for (Dictionary dictionary : dictionaries) {
                bufferedWriter.write(dictionary + "::" + (dictionary == currentDictionary ? "*" : ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Записать словарь не удалось!");
        }

        System.exit(0);
    }
}
