package tagger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author Chirag
 */
public class Activity {
    public String name;
    public ArrayList<String> prev, next,participant,similarActivity;
    public String source;

    public Activity(String name) {
        this.name = name;
        prev = new ArrayList<>();
        next = new ArrayList<>();
        participant = new ArrayList<>();
        similarActivity=new ArrayList<>();
        source="Movie Scripts";
    }

    public Activity() {
        //this.name = name;
        prev = new ArrayList<>();
        next = new ArrayList<>();
        participant = new ArrayList<>();
        similarActivity=new ArrayList<>();
        source="Movie Scripts";
    }

}

