package com.ttp76.spooltaggenerator.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.item.file.transform.Range;

/**
 * Created by fvalmeida on 9/10/14.
 */
@Setter
@Getter
@ToString
public class Material {
    private String projectNo;
    private String unitArea;
    private String designArea;
    private String lineNo;
    private String trainSheetNo;
    private String pipelineRef;
    private String revNo;
    private String pipingClass;
    private String spoolId;
    private String size;
    private String identCode;
    private String quantity;
    private String uom;
    private String group;
    private String matCategory;
    private String description;

    public static String[] COLUMN_NAMES = new String[]{
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

    public static Range[] COLUMN_RANGES = {
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

    public static String OUTPUT_LINE_FORMAT =
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
}
