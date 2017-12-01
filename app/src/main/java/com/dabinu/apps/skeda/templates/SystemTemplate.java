package com.dabinu.apps.skeda.templates;


public class SystemTemplate{

    String name;
    int image;

    public SystemTemplate(String name, int image){
        this.name = name;
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public int getImage(){
        return image;
    }
}
