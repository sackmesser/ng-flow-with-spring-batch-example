package com.ttp76.spooltaggenerator;

import org.springframework.batch.item.file.transform.Range;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static String[] MATERIAL_COLUMN_NAMES = new String[]{
            "projectNo",
            "unitArea",
            "designArea",
            "lineNo",
            "trainSheetNo",
            "pipelineRef",
            "revNo",
            "pipingClass",
            "spoolId",
            "size",
            "identCode",
            "quantity",
            "uom",
            "group",
            "matCategory",
            "description"
    };

    public static Range[] MATERIAL_COLUMN_RANGES = {
            new Range(1, 3),
            new Range(4, 14),
            new Range(15, 21),
            new Range(22, 34),
            new Range(35, 38),
            new Range(39, 84),
            new Range(85, 88),
            new Range(89, 94),
            new Range(95, 130),
            new Range(131, 137),
            new Range(138, 148),
            new Range(149, 155),
            new Range(156, 159),
            new Range(160, 168),
            new Range(169, 175),
            new Range(176, 284)
    };

    public static String MATERIAL_OUTPUT_LINE_FORMAT =
            "%-4s" +
            "%-10s" +
            "%-7s" +
            "%-13s" +
            "%-4s" +
            "%-46s" +
            "%-4s" +
            "%-6s" +
            "%-36s" +
            "%-7s" +
            "%-11s" +
            "%-7s" +
            "%-4s" +
            "%-9s" +
            "%-7s" +
            "%-109s";

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5MB");
        factory.setMaxRequestSize("5MB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}