package si.fri.rso.shoppingcart.services.beans;

import si.fri.rso.shoppingcart.lib.ShoppingCart;
import si.fri.rso.shoppingcart.lib.ShoppingCartProduct;
import si.fri.rso.shoppingcart.models.converters.ShoppingCartConverter;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartEntity;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartProductEntity;
import si.fri.rso.shoppingcart.services.beans.configuration.RestProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@RequestScoped
public class ShoppingCartBean {

    private Logger log = Logger.getLogger(ShoppingCartBean.class.getName());

    // TODO: Discover product catalog service using "com.kumuluz.ee.discovery" library
    // @Inject
    // @DiscoverService(value = "product-catalog-microservice", environment = "dev", version = "*")
    // private Optional<String> productCatalogServicePath;

    @Inject
    private RestProperties properties;

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

        // If the product catalog service is enabled, we should check if the product that we are trying
        // to add to shopping cart, actually exists. Perform a web request to [product-catalog] microservice.
        // The product catalog service is enabled when the following key is set in Consul config server.
        // environments/dev/services/shopping-cart-microservice/1.0.0/config/rest-properties/product-catalog-base-url
        String baseUrl = properties.getProductCatalogBaseUrl();
        if (baseUrl != null) {
            String productUrl = baseUrl + "/v1/products/" + product.getProductId();
            Response productResponse = ClientBuilder.newClient().target(productUrl).request().get();

            if (!productResponse.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                log.warning("Remote server '" + properties.getProductCatalogBaseUrl() + "' responded with status " + productResponse.getStatus());
                throw new IllegalArgumentException("Product does not exist");
            }
        } else {
            // The key is not set in Consul config server. Warn the user and continue.
            log.warning("The 'rest-properties/product-catalog-base-url' value is not set. The service will not check if product exists before adding it to shopping cart.");
        }

        // Check if this item is already in shopping cart.
        TypedQuery<ShoppingCartProductEntity> query = em.createNamedQuery("ShoppingCartProductEntity.find", ShoppingCartProductEntity.class);
        query.setParameter("productId", product.getProductId());
        query.setParameter("shoppingCartId", shoppingCartId);

        // The database has a UNIQUE constraint on (product_id, shopping_cart_id), so there will be
        // at most one result in database.
        ShoppingCartProductEntity existingProduct = null;
        try {
            existingProduct = query.getSingleResult();
        } catch (NoResultException e) {
            // Do nothing, this will be handled below.
        }

        // Compute the quantity: if the quantity is not set, set it to 1.
        // Otherwise, the quantity should be 0 or a positive value.
        int quantity = 0;
        if (product.getQuantity() == null) {
            quantity = 1;
        } else {
            // Make sure the quantity is non-negative (0 or more) and not null.
            quantity = Math.max(product.getQuantity(), quantity);
        }

        // Perform an operation based on the request data:
        //   * if the product is already in the shopping cart and the new quantity is 0, remove it.
        //   * if the product is already in the shopping cart, update its quantity
        //   * if the product is not in the shopping cart yet, add it.
        if (existingProduct != null && quantity == 0) {
            // Set item quantity to 0 - remove product from database.
            try {
                beginTx();
                shoppingCartEntity.getProducts().remove(existingProduct);
                em.remove(existingProduct);
                em.persist(shoppingCartEntity);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
            return ShoppingCartConverter.toDto(shoppingCartEntity);
        }

        ShoppingCartProductEntity productEntity = existingProduct;

        // If the product is not yet in database, create a new entity and set its fields.
        if (existingProduct == null) {
            // Create a new product
            productEntity = new ShoppingCartProductEntity();
            productEntity.setProductId(product.getProductId());
            productEntity.setShoppingCart(shoppingCartEntity);

            // Add product into shopping cart.
            shoppingCartEntity.getProducts().add(productEntity);
        }

        // Update quantity for an existing or new object.
        productEntity.setQuantity(quantity);

        // Persist the object in database.
        try {
            beginTx();
            em.persist(productEntity);
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
