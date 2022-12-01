package si.fri.rso.shoppingcart.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
@CrossOrigin(allowOrigin = "*")
@OpenAPIDefinition(
    info = @Info(title = "Shopping cart API", version = "v1",
    contact = @Contact(email = "nb2020@student.uni-lj.si"),
    license = @License(name = "dev"), description = "API for managing shopping carts."),
    servers = @Server(url = "http://localhost:8080/"))
@ApplicationPath("/v1")
public class ShoppingCartApplication extends Application {

}
