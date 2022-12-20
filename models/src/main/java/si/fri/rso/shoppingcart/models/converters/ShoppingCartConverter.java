package si.fri.rso.shoppingcart.models.converters;

import si.fri.rso.shoppingcart.lib.ShoppingCart;
import si.fri.rso.shoppingcart.lib.ShoppingCartProduct;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartEntity;
import si.fri.rso.shoppingcart.models.entities.ShoppingCartProductEntity;

import java.util.List;

public class ShoppingCartConverter {

    public static ShoppingCart toDto(ShoppingCartEntity entity) {

        ShoppingCart dto = new ShoppingCart();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Convert all ShoppingCartProductEntity objects to ShoppingCartProduct objects.
        List<ShoppingCartProduct> shoppingCartProducts = entity.getProducts().stream().map(ShoppingCartProductConverter::toDto).toList();
        dto.setProducts(shoppingCartProducts);

        return dto;

    }

    public static ShoppingCartEntity toEntity(ShoppingCart dto) {

        ShoppingCartEntity entity = new ShoppingCartEntity();
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        // Convert all ShoppingCartProduct objects to ShoppingCartProductEntity objects.
        List<ShoppingCartProductEntity> shoppingCartProductEntities = dto.getProducts().stream().map(shoppingCartProduct -> ShoppingCartProductConverter.toEntity(shoppingCartProduct, entity)).toList();
        entity.setProducts(shoppingCartProductEntities);

        return entity;

    }

}
