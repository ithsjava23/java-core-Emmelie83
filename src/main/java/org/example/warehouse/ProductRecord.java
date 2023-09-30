package org.example.warehouse;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class ProductRecord {

    private final UUID id;
    private String name;
    private Category category;
    private BigDecimal price;

    private boolean changed;



    public ProductRecord(String name, Category category, BigDecimal price) {
        id = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.price = price;
        this.changed = false;
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

    public boolean isChanged() {
        return changed;
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
