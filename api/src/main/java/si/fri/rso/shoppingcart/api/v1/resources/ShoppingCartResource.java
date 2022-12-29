package si.fri.rso.shoppingcart.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.shoppingcart.lib.ShoppingCart;
import si.fri.rso.shoppingcart.lib.ShoppingCartProduct;
import si.fri.rso.shoppingcart.services.beans.ShoppingCartBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/shopping-carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, OPTIONS, PUT")
public class ShoppingCartResource {

    private Logger log = Logger.getLogger(ShoppingCartResource.class.getName());

    @Inject
    private ShoppingCartBean shoppingCartBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get data for single shopping cart.", summary = "Get data for single shopping cart")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Shopping cart information.",
            content = @Content(schema = @Schema(implementation = ShoppingCart.class))
        ),
        @APIResponse(
            responseCode = "404",
            description = "Shopping cart could not be found."
        ),
    })
    @GET
    @Path("/{shoppingCartId}")
    public Response getSingleProduct(@Parameter(description = "Shopping cart ID.", required = true)
                                     @PathParam("shoppingCartId") Integer shoppingCartId) {

        ShoppingCart shoppingCart = shoppingCartBean.getShoppingCart(shoppingCartId);
        if (shoppingCart == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(shoppingCart).build();
    }

    @POST
    @Path("/create")
    public Response createShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartBean.createShoppingCart();
        return Response.status(Response.Status.OK).entity(shoppingCart).build();
    }

    @PUT
    @Path("/{shoppingCartId}")
    public Response insertToShoppingCart(@PathParam("shoppingCartId") Integer shoppingCartId, ShoppingCartProduct product) {
        // Check if received JSON contains at least product id. If not, return an exception.
        if (product.getProductId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Insert product into shopping cart (in database).
        ShoppingCart updatedCart = null;
        try {
            updatedCart = shoppingCartBean.insertToShoppingCart(shoppingCartId, product);
        } catch (IllegalArgumentException exception) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (updatedCart == null) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }

        return Response.status(Response.Status.OK).entity(updatedCart).build();
    }

}
