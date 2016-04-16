package ru.spbstu.dis.opc.client.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.net.HostAndPort;
import feign.Feign;
import feign.Request.Options;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import java.util.concurrent.TimeUnit;

public class OpcClientApiFactory {

  public static OpcAccessApi createOpcAccessApi(HostAndPort hostAndPort) {
    final int connectionTimeout = (int) TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS);
    return new OpcClientApiFactory().defaultFeignBuilder()
        .options(new Options(connectionTimeout, connectionTimeout))
        .target(OpcAccessApi.class, String.format("https://%s:%s",
            hostAndPort.getHostText(), hostAndPort.getPort()));
  }

  private Feign.Builder defaultFeignBuilder() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    return Feign.builder()
        .encoder(new JacksonEncoder(mapper))
        .decoder(new JacksonDecoder(mapper))
        .contract(new JAXRSContract());
  }
}
