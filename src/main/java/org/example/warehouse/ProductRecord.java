package org.example.warehouse;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class ProductRecord {

    private final UUID id;
    private final String name;
    private final Category category;
    private BigDecimal price;

    private boolean changed;


    public ProductRecord(UUID id, String name, Category category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        changed = false;
    }

    public UUID uuid() {
        return id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    @Override
    public String toString() {
        return "ProductRecord{" +
                "uuid=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }

    public BigDecimal price() {
        return price;
    }

    public Category category() {
        return category;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRecord that = (ProductRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
