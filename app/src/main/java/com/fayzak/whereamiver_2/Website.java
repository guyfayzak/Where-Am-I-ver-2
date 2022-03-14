package com.fayzak.whereamiver_2;
import java.util.ArrayList;
import java.util.HashMap;

public class Website {
    // members
    private String url;
    private ArrayList<Keyword> keywordArrayList;

    // constructors
    public Website(String url){
        this.url = url;
        this.keywordArrayList = new ArrayList<>();
    }

    // setters
    public void addKeyword(String keyword){
        this.keywordArrayList.add(new Keyword(keyword, url));
    }

    public void removeKeyword(Keyword k){
        this.keywordArrayList.remove(k);
    }

    // getters
    public Keyword getKeyWordByName(String name){
        for (Keyword k : keywordArrayList){
            if(k.getKeyWord().equals(name)){
                return k;
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Keyword> getKeywordArrayList() {
        return keywordArrayList;
    }
}
