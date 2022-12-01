package si.fri.rso.shoppingcart.lib;

import java.time.Instant;
import java.util.List;

public class ShoppingCart {

    private Integer id;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ShoppingCartProduct> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ShoppingCartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ShoppingCartProduct> products) {
        this.products = products;
    }
}
