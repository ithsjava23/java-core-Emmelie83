package org.example.warehouse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private final String name;
    private final ArrayList<ProductRecord> products;
    private final ArrayList<ProductRecord> changedProducts;


    private Warehouse(String name) {
        this.name = name;
        products = new ArrayList<>();
        changedProducts = new ArrayList<>();
    }

    public static Warehouse getInstance(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Warehouse name cannot be null or empty.");
        return new Warehouse(name);
    }

    public static Warehouse getInstance() {
        return getInstance("DefaultWarehouse");
    }


    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        UUID productId = (id == null) ? UUID.randomUUID() : id;

        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Product name can't be null or empty.");

        if (products.stream().anyMatch(product -> product.uuid().equals(productId)))
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");

        if (category == null) throw new IllegalArgumentException("Category can't be null.");

        BigDecimal finalPrice = (price == null) ? BigDecimal.ZERO : price;
        ProductRecord product = new ProductRecord(productId, name, category, finalPrice);
        products.add(product);
        return product;
    }

    public Optional<ProductRecord> getProductById(UUID id) {
        return products.stream()
                .filter(product -> product.uuid().equals(id))
                .findFirst();
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        if (id == null || newPrice == null) {
            throw new IllegalArgumentException("Invalid ID or price.");
        }

        Optional<ProductRecord> productToUpdate = products.stream()
                .filter(product -> product.uuid().equals(id))
                .findFirst();

        if (productToUpdate.isPresent()) {
            ProductRecord product = productToUpdate.get();
            product.setPrice(newPrice);
            product.setChanged(true);

            if (!changedProducts.contains(product)) {
                changedProducts.add(product);
            }
        } else {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }
    }


    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::getCategory));
    }

    public List<ProductRecord> getProducts() {
        return products.stream()
                .toList();
    }

    public List<ProductRecord> getChangedProducts() {
        return changedProducts
                .stream()
                .toList();
    }


    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream()
                .filter(product -> product.getCategory().equals(category))
                .toList();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
}


