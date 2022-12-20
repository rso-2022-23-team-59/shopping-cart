package si.fri.rso.shoppingcart.services.beans;

import si.fri.rso.shoppingcart.lib.ShoppingCart;
import si.fri.rso.shoppingcart.lib.ShoppingCartProduct;
import si.fri.rso.shoppingcart.models.converters.ShoppingCartConverter;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartEntity;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartProductEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.logging.Logger;


@RequestScoped
public class ShoppingCartBean {

    private Logger log = Logger.getLogger(ShoppingCartBean.class.getName());

    @Inject
    private EntityManager em;

    public ShoppingCart getShoppingCart(Integer id) {
        ShoppingCartEntity shoppingCartEntity = em.find(ShoppingCartEntity.class, id);

        if (shoppingCartEntity == null) {
            throw new NotFoundException();
        }

        return ShoppingCartConverter.toDto(shoppingCartEntity);
    }

    public ShoppingCart createShoppingCart() {
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        beginTx();
        em.persist(shoppingCartEntity);
        commitTx();
        return ShoppingCartConverter.toDto(shoppingCartEntity);
    }

    public ShoppingCart insertToShoppingCart(Integer shoppingCartId, ShoppingCartProduct product) {

        // Check if shopping cart actually exists. If not, exit.
        ShoppingCartEntity shoppingCartEntity = em.find(ShoppingCartEntity.class, shoppingCartId);
        if (shoppingCartEntity == null) {
            return null;
        }

        // If quantity is not set, it will default to 1.
        int quantity = product.getQuantity() == null ? 1 : product.getQuantity();

        // Create a new product entity that will be inserted into database.
        // Note that we don't need to set shopping cart, as this will be done
        // automatically when product is inserted into shopping cart.
        ShoppingCartProductEntity productEntity = new ShoppingCartProductEntity();
        productEntity.setProductId(product.getProductId());
        productEntity.setQuantity(quantity);

        // Add product into shopping cart.
        shoppingCartEntity.getProducts().add(productEntity);

        try {
            beginTx();
            em.persist(shoppingCartEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return ShoppingCartConverter.toDto(shoppingCartEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
