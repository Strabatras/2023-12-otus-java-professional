package ru.otus;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private String fruitBoxName;
    private List<T> fruits;

    public Box(String fruitBoxName) {
        this.fruitBoxName = fruitBoxName;
        this.fruits = new ArrayList<>();
    }

    public String getFruitBoxName() {
        return fruitBoxName;
    }

    public void add(T fruit) {
        fruits.add(fruit);
    }

    public Long weight() {
        return fruits.stream()
                .map(x -> x.getWeight())
                .reduce(0L, Long::sum);
    }

    public boolean compare(Box<? extends Fruit> box) {
        return weight().equals(box.weight());
    }

    public List<? extends Fruit> getAll(){
        return fruits;
    }

    public static <T> T cast(Object o, Class<T> clazz) {
        return clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    public void copyToBox(Box<? extends Fruit> box) {
        System.out.println(

              "AAAA"
        );
        //getAll().stream()
        //        .forEach(fruit -> System.out.println(fruit.getClass().getName()));
    }
}

/*
        for(T fruit : fruits){
            //System.out.println(fruit.getClass() + " => " + box.getClass() );
            //ParameterizedType type = (ParameterizedType )fruit.getGenericSuperclass();
            //System.out.println(fruit.getClass().getAnnotatedSuperclass());
        }
*/
