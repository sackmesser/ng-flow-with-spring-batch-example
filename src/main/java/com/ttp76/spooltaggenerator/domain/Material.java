package com.ttp76.spooltaggenerator.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
