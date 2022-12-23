package si.fri.rso.shoppingcart.services.beans.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private String productCatalogBaseUrl;

    public String getProductCatalogBaseUrl() {
        return productCatalogBaseUrl;
    }

    public void setProductCatalogBaseUrl(String productCatalogBaseUrl) {
        this.productCatalogBaseUrl = productCatalogBaseUrl;
    }
}
