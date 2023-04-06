package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlotRepository implements PanacheRepository<Plot> {
}