package org.acme;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

@Path("/plotss")
@Tag(name = "Map Plot Service", description = "Bisnis Logic Plot Service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlotService {

    @Inject
    PlotRepository plotRepository;
    public static List<Plot> plots = new ArrayList<>();

    public static List<Plot> kafkaConsumer = new ArrayList<>();
    @GET
    @Path("/kafkaconsumer")
    public List<Plot> getKafkaConsumer(){
        return kafkaConsumer;
    }

    @GET
    @Operation(
            operationId = "getAll",
            summary = "get All Plot",
            description = "method to get all Plot"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complited",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getAll(){
        plots = plotRepository.listAll();
        return Response.ok(plots).build();
    }

    @GET
    @Operation(
            operationId = "getById",
            summary = "get Plot By id",
            description = "method to get plot by id"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complited",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Path("{id}")
    public Response getById(@PathParam("id") Long id){
        return plotRepository.findByIdOptional(id)
                .map(plot -> Response.ok(plot).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Operation(
            operationId = "getByName",
            summary = "get Plot By name",
            description = "method to get plot by name"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complited",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Path("nama/{nama}")
    public Response getByNama(@PathParam("nama") String nama){
        return plotRepository.find("nama", nama)
                .singleResultOptional()
                .map(plot -> Response.ok(plot).build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Operation(
            operationId = "getByWarna",
            summary = "get Plot By color",
            description = "method to get plot by color"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complited",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Path("warna/{warna}")
    public Response getByWarna(@PathParam("warna") String warna){
        return plotRepository.find("warna", warna)
                .singleResultOptional()
                .map(plot -> Response.ok(plot).build())
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @POST
    @Operation(
            operationId = "createPlot",
            summary = "create new Plot",
            description = "Create a new Plot to add inside the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Plot Created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Transactional
    public Response create(
            @RequestBody(
                    description = "Plot to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Plot.class))
            )
            Plot plot){
        plotRepository.persist(plot);
        if (plotRepository.isPersistent(plot)){
            return Response.created(URI.create("/plots/"+plot.getId())).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long id, Plot plot) {
        return plotRepository
                .findByIdOptional(id)
                .map(
                        m -> {
                            m.setNama(plot.getNama());
                            m.setWarna(plot.getWarna());
                            m.setKoordinatX(Double.valueOf(plot.getKoordinatX()));
                            m.setKoordinatY(Double.valueOf(plot.getKoordinatY()));
                            m.setDeskripsi(plot.getDeskripsi());
                            return Response.ok(m).build();
                        })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Operation(
            operationId = "DeletePlot",
            summary = "Delete  Plot",
            description = "Delete Plot from  inside the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Plot Deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = plotRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Incoming("plot-in")
    public void addKafkaToList(Plot plot){
        kafkaConsumer.add(plot);
    }

}