package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Word {
    private String word;
    private Date createDate;

//  Возможно когда-то допилю функционал
/*
    private String transfer;
    String transcription;
    ArrayList<String> synonyms = new ArrayList<>();
    ArrayList<String> antonyms = new ArrayList<>();
    String interpretation;
    String sentence;
*/
    Word(String word) {
        this.word = word;
        this.createDate = new Date();
    }

    Word(String word, Date createDate) {
        this.word = word;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return getDateInTheRightFormat(createDate) + "\t" + word;
    }

    public String getWord() {
        return word;
    }

    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (!word.equals(other.word))
            return false;
        return true;
    }

    public static String getDateInTheRightFormat(Date date) {
        if (date == null) {
            return null;
        }
        String pattern = "dd-MMM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static Date getDateFromArgument(String strDate) {
        if (strDate == null){
            return null;
        }
        String pattern = "dd-MMM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }

    }

}
