package com.fayzak.whereamiver_2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
    // when restoring a hashmap from json, the references to the same objects are changed...
    // so hash map is not good here.
    private ArrayList<Website> websites;
    //private HashMap<String, Website> quickSearch;
    private static final String FILE_NAME = "Data.txt";
    private static Context context;

    // i want it to be a singleton that is usable throughout the activities
    private DataManager (boolean fromFile){
        try(FileInputStream inputStream = context.openFileInput(FILE_NAME)){
            // try with resources on the input stream, then using th code bellow to read
            // line by line an create the final string that will be converted to a object with gson
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            Gson gson = new Gson();
            DataManager temp = gson.fromJson(builder.toString(), DataManager.class);
            this.websites = temp.websites;
        }
        catch (FileNotFoundException e){
            this.websites = new ArrayList<>();
        }
        catch (IOException e){
            Log.wtf("IO EXCEPTION:","problem reading the file, crates a new dataManager");
            this.websites = new ArrayList<>();
        }
    }

    private static class holder{
        private static DataManager holder = new DataManager(true);
    }

    public static DataManager getInstance(){
        return holder.holder;
    }

    public static void setContext(Context con){
        context = con;
    }

    public void saveDataState(){
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        file.delete();
        try(FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            outputStream.write(gson.toJson(this).getBytes());
        }
        catch(Exception e){}
    }

    public void addWebsite(Website website){
        this.websites.add(website);
        //this.quickSearch.put(website.getUrl(), website);
    }

    public void removeWebsite(Website website){
        this.websites.remove(website);
    }

    public Website getWebsite(String url){
        for (Website website: websites)
            if (website.getUrl().equals(url))
                return website;
        return null;
    }

    public ArrayList<Website> getWebsites(){
        return this.websites;
    }

}
