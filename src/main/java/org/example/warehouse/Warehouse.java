package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static final Map<String, Warehouse> instances = new HashMap<>();
    private String name;
    private Map<UUID, ProductRecord> products;

    private Warehouse() {
        this.products = new HashMap<>();
    }

    private Warehouse(String name) {
        this.name = name;
        this.products = new HashMap<>();
    }

    public static Warehouse getInstance(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Warehouse name cannot be null or empty.");
        }
        if (!instances.containsKey(name)) {
            instances.put(name, new Warehouse(name));
        }
        return instances.get(name);
    }

    public static Warehouse getInstance() {
        return getInstance("DefaultWarehouse");
    }

    public String getName() {
        return name;
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (products.containsKey(id)) {
            throw new IllegalArgumentException("Product with the given ID already exists.");
        }

        ProductRecord product = new ProductRecord(name, category, price);
        products.put(id, product);
        return product;
    }


    public Optional<ProductRecord> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        if (products.containsKey(id)) {
            ProductRecord product = products.get(id);
            product.setPrice(newPrice);
            product.setChanged(true);
        }
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> groupedProducts = new HashMap<>();
        for (ProductRecord product : products.values()) {
            Category category = product.getCategory();
            if (!groupedProducts.containsKey(category)) {
                groupedProducts.put(category, new ArrayList<>());
            }
            groupedProducts.get(category).add(product);
        }
        return groupedProducts;
    }

    public List<ProductRecord> getProducts() {
        return new ArrayList<>(products.values());
    }

    public List<ProductRecord> getChangedProducts() {
        List<ProductRecord> changedProducts = new ArrayList<>();
        for (ProductRecord product : products.values()) {
            if (product.isChanged()) {
                changedProducts.add(product);
            }
        }
        return changedProducts;
    }


    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> productsByCategory = new ArrayList<>();
        for (ProductRecord product : getProducts()) {
            if (product.getCategory().equals(category)) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

}


