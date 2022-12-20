package si.fri.rso.shoppingcart.models.entities;

import javax.persistence.*;

@Entity
@Table(
    name = "shopping_cart_products",
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueShoppingCartProduct", columnNames = {"shopping_cart_id", "product_id"}),
    }
)
@NamedQueries({
    @NamedQuery(
        name="ShoppingCartProductEntity.find",
        query="SELECT product FROM ShoppingCartProductEntity product WHERE product.productId = :productId AND product.shoppingCart.id = :shoppingCartId"
    ),
})
public class ShoppingCartProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shopping_cart_id")
    private ShoppingCartEntity shoppingCart;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShoppingCartEntity getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
