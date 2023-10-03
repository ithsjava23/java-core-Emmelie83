package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.Collections;

public class Warehouse {


    private String name;
    private final ArrayList<ProductRecord> products;

    private final ArrayList<UUID> changedProductIds;

    ArrayList<ProductRecord> changedProducts;



    private Warehouse(String name) {
        this.name = name;
        products = new ArrayList<>();
        changedProducts = new ArrayList<>();
        changedProductIds = new ArrayList<>();
    }

    public static Warehouse getInstance(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Warehouse name cannot be null or empty.");
        }
        return new Warehouse(name);
    }

    public static Warehouse getInstance() {
        return getInstance("DefaultWarehouse");
    }


    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }

        for (ProductRecord product : products) {
            if (product.uuid().equals(id)) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }

        // Price handling logic based on your application's requirements
        BigDecimal finalPrice = (price == null) ? BigDecimal.ZERO : price;

        ProductRecord product = new ProductRecord(id, name, category, finalPrice);
        products.add(product);
        return product;
    }


    public Optional<ProductRecord> getProductById(UUID id) {
        for (ProductRecord product : products) {
            if (product.uuid() == id)
                return Optional.of(product);
        }
        return Optional.empty();
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        if (id == null || newPrice == null) {
            throw new IllegalArgumentException("Invalid ID or price.");
        }
        boolean productFound = false;
        for (ProductRecord product : products) {
            if (product.uuid().equals(id)) {
                product.setPrice(newPrice);
                product.setChanged(true);
                if (!changedProductIds.contains(id)) {
                    changedProductIds.add(id);
                }
                productFound = true;
                break;
            }
        }
        if (!productFound) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }
    }


    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> groupedProducts = new HashMap<>();
        for (ProductRecord product : products) {
            Category category = product.getCategory();
            if (!groupedProducts.containsKey(category)) {
                groupedProducts.put(category, new ArrayList<>());
            }
            groupedProducts.get(category).add(product);
        }
        return groupedProducts;
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<ProductRecord> getChangedProducts() {
        List<ProductRecord> changedProducts = new ArrayList<>();
        for (UUID id : changedProductIds) {
            Optional<ProductRecord> productOptional = getProductById(id);
            productOptional.ifPresent(changedProducts::add);
        }
        return Collections.unmodifiableList(changedProducts);
    }



    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> productsByCategory = new ArrayList<>();
        for (ProductRecord product : getProducts()) {
            if (product.getCategory().equals(category)) {
                productsByCategory.add(product);
            }
        }
        return Collections.unmodifiableList(productsByCategory);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
}


