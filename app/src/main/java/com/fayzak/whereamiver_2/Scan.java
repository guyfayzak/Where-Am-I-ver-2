package com.fayzak.whereamiver_2;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Scan {
    private String dateTime;
    private int googlePosition;
    private int page;
    private int dayOfYear;
    // epoch time

    // to ensure a scan object is created only after an actual scan
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Scan (int page, int googlePosition){
        this.page = page;
        this.googlePosition = googlePosition;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.dayOfYear = LocalDateTime.now().getDayOfYear(); // will help me building a graph
    }

    // getters:
    public String getDateTime() {
        return dateTime;
    }

    public int getGooglePosition() {
        return googlePosition;
    }

    public int getPage() {
        return page;
    }

    // the actual scanning algorithm, remember i have to run it on a different thread, maybe i need
    // it to be threadsafe.. i don't, i will just make the button disappear or something/ synch
    // in the thread itself.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Scan scanKeyword(Keyword keyword){
        // build the website urls:
        String url = keyword.getParentUrl();
        String withUrl = "https://www." + url;
        String without = "https://" + url;

        // build url for search
        String keyWords = keyword.getKeyWord().replace(' ', '+');
        String rawUrl = "https://www.google.com/search?q=" + keyWords + "&start=";

        // iterate until we find result, each time get the next html doc
        int googlePos = 0;
        int pageNum = 0;
        boolean found = false;
        while (!found && pageNum < 100) {
            String currUrl = rawUrl + pageNum*10;
            try {
                Document page = Jsoup.connect(currUrl).get();
                Elements elements = page.select("cite");
                for(Element element : elements){
                    googlePos += 1;
                    Element span = element.getElementsByTag("span").first();
                    if (span != null && (without.equals(span.text()) || withUrl.equals(span.text()))) {
                        found = true;
                        break;
                    }
                }
            }
            catch (Exception e){
            }
            pageNum++;
        }
        keyword.addScan(new Scan(pageNum, (googlePos +1)/2));
        return keyword.getLastScanInfo();
    }
}
