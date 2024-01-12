package ru.otus;

import com.google.common.collect.Lists;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> names = Lists.newArrayList("John", "Adam", "Jane");
        System.out.println( "Names : " + String.join( ", ", names ) );
        HelloOtus helloOtus = new HelloOtus();
        List<String> reversed = helloOtus.reverseList( names );
        System.out.println( "Reverse names : " + String.join( ", ", reversed ) );
    }
}