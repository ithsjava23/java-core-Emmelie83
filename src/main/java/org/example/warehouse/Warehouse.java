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
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }

        if (price == null) {
            price = BigDecimal.ZERO;
        }

        ProductRecord product = new ProductRecord(name, category, price);
        products.put(id, product);
        return product;
    }


    public Optional<ProductRecord> getProductById(UUID id) {
        if (id == null || id.toString().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(products.get(id));
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        if (id == null || newPrice == null) {
            throw new IllegalArgumentException("Invalid ID or price.");
        }
        ProductRecord product = products.get(id);
        if (product != null) {
            product.setPrice(newPrice);
            product.setChanged(true);
        } else {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
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
        List<ProductRecord> sortedProducts = new ArrayList<>(products.values());
        sortedProducts.sort(Comparator.comparing(ProductRecord::uuid));
        return Collections.unmodifiableList(sortedProducts);
    }

    public List<ProductRecord> getChangedProducts() {
        List<ProductRecord> changedProducts = new ArrayList<>();
        for (ProductRecord product : products.values()) {
            if (product.isChanged()) {
                changedProducts.add(product);
            }
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


