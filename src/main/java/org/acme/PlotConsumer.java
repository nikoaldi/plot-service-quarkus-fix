package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;




@Path("cheking")
public class PlotConsumer {
    @Inject
    PlotRepository plotRepository;

    public static List<Long> plotId = new ArrayList<>();
    @GET
    public Response getAll(){
        return Response.ok(plotId).build();
    }

    @Incoming("plot-in")
    @Transactional
    public Plot persistPlot(Plot plot) {
        if(plotId.contains(plot.getId())){
            plotRepository
                    .findByIdOptional(plot.getId())
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

        } else {
            plotId.add(plot.getId());
            plotRepository.persist(plot);
        }
        return plot;
    }

}
