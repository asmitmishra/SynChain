package com.example.mishr.logme;

public class Student {
    Student(){
    }

    Student(String name, String id, String lnk,String about, String key){
        this.stdname = name;
        this.stdid = id;
        this.link = lnk;
        this.aboutstd = about;
        this.key1 = key;


    }

    public String getStdname() {
        return stdname;
    }

    public String getStdid() {
        return stdid;
    }
    public String getLink() {
        return link;
    }


    private String stdname;
    private String stdid;
    private String link;

    public String getKey1() {
        return key1;
    }

    private String key1;





    public String getAboutstd() {
        return aboutstd;
    }

    private String aboutstd;





}
