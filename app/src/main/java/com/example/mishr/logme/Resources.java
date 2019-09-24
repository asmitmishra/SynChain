package com.example.mishr.logme;

public class Resources {

    Resources(){

    }

    Resources( String name,String link,String id1){
        this.resourceName = name;
        this.resourceLink = link;
        this.resourceid = id1;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceLink() {
        return resourceLink;
    }


    public String getResourceid() {
        return resourceid;
    }

    private String resourceid;
    private String resourceName;
    private String resourceLink;
}
