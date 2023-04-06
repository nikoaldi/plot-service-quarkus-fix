package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PlotDeserializer extends ObjectMapperDeserializer<Plot> {
    public PlotDeserializer(){
        super(Plot.class);
    }
}
