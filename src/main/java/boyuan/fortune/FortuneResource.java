package boyuan.fortune;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;

@Path("/api/fortune")
public class FortuneResource {

    private final FortuneStore store;

    public FortuneResource(FortuneStore store) {
        this.store = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Fortune> fortune() {
        return store.getRandom();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(@Valid @NotNull Fortune fortune) {
        store.persist(fortune);
    }
}
