package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Comparator.comparingLong;

public class CustomerService {

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> map;

    public CustomerService() {
         this.map = treeMap();
    }

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> mapEntry = map.firstEntry();
        return cloneCustomerFromMapEntryToMap(mapEntry).firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> mapEntry = map.higherEntry(customer);
        return cloneCustomerFromMapEntryToMap(mapEntry).firstEntry();
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private TreeMap<Customer, String> treeMap(){
        return new TreeMap<>(comparingLong(Customer::getScores));
    }

    private Customer cloneCustomer(Customer customer) {
        Customer clone = new Customer(customer.getId(), customer.getName(), customer.getScores());
        return clone;
    }

    private TreeMap<Customer, String> cloneCustomerFromMapEntryToMap(Map.Entry<Customer, String> mapEntry){
        TreeMap<Customer, String> map = new TreeMap<>(comparingLong(Customer::getScores));
        if (mapEntry != null) {
            Customer cloneCustomer = cloneCustomer(mapEntry.getKey());
            map.put(cloneCustomer, mapEntry.getValue());
        }
        return map;
    }

}
