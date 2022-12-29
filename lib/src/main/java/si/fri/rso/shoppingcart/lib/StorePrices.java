package si.fri.rso.shoppingcart.lib;

import java.util.List;

public class StorePrices {

    private Store store;
    private List<ProductPrice> prices;

    public List<ProductPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<ProductPrice> prices) {
        this.prices = prices;
    }

    public Integer getStoreId() {
        if (this.store == null) return null;
        return this.store.getId();
    }

    public void setStoreId(Integer storeId) {
        if (this.store == null) {
            this.store = new Store();
        }
        this.store.setId(storeId);
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Double getTotalPrice() {
        double totalPrice = 0.0;
        for (ProductPrice price : this.prices) {
            Double productPrice = price.getPrice();
            if (productPrice == null) continue;
            totalPrice += productPrice;
        }
        return totalPrice;
    }

    public String getCurrency() {
        for (ProductPrice price : this.prices) {
            String currency = price.getCurrency();
            if (currency != null) return currency;
        }
        return null;
    }

}
