package org.solarbank.server.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class NasaResponse {

    private String type;
    private Geometry geometry;
    private Properties properties;
    private Header header;
    private List<String> messages;
    private Parameters parameters;
    private Times times;

    @Data
    public static class Geometry {
        private String type;
        private List<Double> coordinates;
    }

    @Data
    public static class Properties {
        private Map<String, Map<String, Double>> parameter;
    }

    @Data
    public static class Header {
        private String title;
        private Api api;
        private List<String> sources;
        private Double fillValue;
        private String start;
        private String end;

        @Data
        public static class Api {
            private String version;
            private String name;
        }
    }

    @Data
    public static class Parameters {
        private Map<String, ParameterDetails> parameterDetails;

        @Data
        public static class ParameterDetails {
            private String units;
            private String longname;
        }
    }

    @Data
    public static class Times {
        private double data;
        private double process;
    }
}