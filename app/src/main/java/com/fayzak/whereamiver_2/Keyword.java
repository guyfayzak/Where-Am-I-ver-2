package com.fayzak.whereamiver_2;
import java.util.LinkedList;

public class Keyword {
    private String keyWord;
    private String parentUrl;
    private LinkedList<Scan> scans;

    public Keyword(String keyWord, String parentUrl){
        this.keyWord = keyWord;
        this.parentUrl = parentUrl;
        this.scans = new LinkedList<>();
    }

    // setters
    public void addScan(Scan scan){
        // to avoid same day scan duplications - we always replace the scan with the
        // most relevant scan that we have. if it is not the same day we store the new scan in
        // the list.
        Scan prev = getLastScanInfo();
        if (prev != null && prev.getDateTime().equals(scan.getDateTime()))
            this.scans.removeLast();
        this.scans.addLast(scan);
    }


    // getters
    public Scan getLastScanInfo(){
        if(this.scans.isEmpty())
            return null;
        return this.scans.getLast();
    }

    public Scan getFirstScanInfo(){
        if(this.scans.isEmpty())
            return null;
        return this.scans.getFirst();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public LinkedList<Scan> getScans() {
        return scans;
    }
}
