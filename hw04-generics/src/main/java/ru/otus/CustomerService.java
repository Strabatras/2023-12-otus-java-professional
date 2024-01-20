package ru.otus;

import static java.util.Comparator.comparingLong;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customerStringTreeMap;

    public CustomerService() {
        this.customerStringTreeMap = customerStringTreeMap();
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> mapEntry = customerStringTreeMap.firstEntry();
        return cloneCustomerFromMapEntryToMap(mapEntry).firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> mapEntry = customerStringTreeMap.higherEntry(customer);
        return cloneCustomerFromMapEntryToMap(mapEntry).firstEntry();
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }

    private TreeMap<Customer, String> customerStringTreeMap() {
        return new TreeMap<>(comparingLong(Customer::getScores));
    }

    private Customer cloneCustomer(Customer customer) {
        return new Customer(customer.getId(), customer.getName(), customer.getScores());
    }

    private TreeMap<Customer, String> cloneCustomerFromMapEntryToMap(Map.Entry<Customer, String> mapEntry) {
        TreeMap<Customer, String> treeMapCustomer = customerStringTreeMap();
        if (mapEntry != null) {
            Customer cloneCustomer = cloneCustomer(mapEntry.getKey());
            treeMapCustomer.put(cloneCustomer, mapEntry.getValue());
        }
        return treeMapCustomer;
    }
}
