package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        final List<Box<? extends Fruit>> boxList = new ArrayList<>();

        boxList.add(appleBox());
        boxList.add(orangeBox());
        boxList.add(fruitBox());

        boxList.stream()
                .forEach(box -> boxInfo(box));

        System.out.println("");

        final Box<Apple> appleToCompareBox = appleBox();
        boolean isCompareAppleBox = appleToCompareBox.compare(appleToCompareBox);
        System.out.println("The apple box and the apple box " + compareBoxMessage(isCompareAppleBox));

        final Box<Orange> orangeToCompareBox = orangeBox();
        boolean isCompareOrangeBox = orangeToCompareBox.compare(appleToCompareBox);
        System.out.println("The orange box and the apple box " + compareBoxMessage(isCompareOrangeBox));

        final Box<Fruit> fruitToCompareBox = fruitBox();
        boolean isCompareFruitBox = fruitToCompareBox.compare(orangeToCompareBox);
        System.out.println("The fruit box and the orange box " + compareBoxMessage(isCompareFruitBox));

        System.out.println("");
        final Box<Fruit> fruitBox = fruitBox();
        final Box<Apple> appleBox = appleBox();
        appleBox.copyToBox(appleBox);

        boxInfo(appleBox);
    }

    private static Box<Apple> appleBox() {
        final Box<Apple> appleBox = new Box<>("Apples");
        appleBox.add(new Apple(100L));
        appleBox.add(new Apple(110L));
        appleBox.add(new Apple(120L));
        appleBox.add(new Apple(130L));
        appleBox.add(new Apple(140L));
        return appleBox;
    }

    private static Box<Orange> orangeBox() {
        final Box<Orange> orangeBox = new Box<>("Oranges");
        orangeBox.add(new Orange(200L));
        orangeBox.add(new Orange(210L));
        orangeBox.add(new Orange(220L));
        return orangeBox;
    }

    private static Box<Fruit> fruitBox() {
        final Box<Fruit> fruitBox = new Box<>("Fruits");
        fruitBox.add(new Fruit(10L));
        fruitBox.add(new Apple(20L));
        fruitBox.add(new Orange(30L));
        return fruitBox;
    }

    private static void boxInfo(Box<? extends Fruit> box) {
        System.out.println("Products in the box : " + box.getFruitBoxName() + " have weight : " + box.weight());
    }

    private static String compareBoxMessage(boolean isCompare) {
        final String message = isCompare
                ? "have equal weight"
                : "do not have equal weight";
        return message;
    }

}