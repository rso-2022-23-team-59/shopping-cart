package si.fri.rso.shoppingcart.services.beans.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private String productCatalogBaseUrl;

    @ConfigValue(watch = true)
    private String storeCatalogBaseUrl;

    public String getProductCatalogBaseUrl() {
        return productCatalogBaseUrl;
    }

    public void setProductCatalogBaseUrl(String productCatalogBaseUrl) {
        this.productCatalogBaseUrl = productCatalogBaseUrl;
    }

    public String getStoreCatalogBaseUrl() {
        return storeCatalogBaseUrl;
    }

    public void setStoreCatalogBaseUrl(String storeCatalogBaseUrl) {
        this.storeCatalogBaseUrl = storeCatalogBaseUrl;
    }
}
