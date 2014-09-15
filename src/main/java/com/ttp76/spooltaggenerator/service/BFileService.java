package com.ttp76.spooltaggenerator.service;

import com.ttp76.spooltaggenerator.domain.Material;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.io.InputStream;
import java.util.*;

@Service
public class BFileService {

    private final Logger log = LoggerFactory.getLogger(BFileService.class);

    public FileSystemResource process(InputStream inputStream) throws Exception {

        FlatFileItemReader<Material> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new InputStreamResource(inputStream));
        DefaultLineMapper<Material> lineMapper = new DefaultLineMapper();
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(Material.COLUMN_NAMES);
        tokenizer.setColumns(Material.COLUMN_RANGES);
        tokenizer.setStrict(false);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper<Material>() {
            @Override
            public Material mapFieldSet(FieldSet fieldSet) throws BindException {
                if (fieldSet == null) {
                    return null;
                }
                Material material = new Material();
                material.setProjectNo(fieldSet.readString("projectNo"));
                material.setUnitArea(fieldSet.readString("unitArea"));
                material.setDesignArea(fieldSet.readString("designArea"));
                material.setLineNo(fieldSet.readString("lineNo"));
                material.setTrainSheetNo(fieldSet.readString("trainSheetNo"));
                material.setPipelineRef(fieldSet.readString("pipelineRef"));
                material.setRevNo(fieldSet.readString("revNo"));
                material.setPipingClass(fieldSet.readString("pipingClass"));
                material.setSpoolId(fieldSet.readString("spoolId"));
                material.setSize(fieldSet.readString("size"));
                material.setIdentCode(fieldSet.readString("identCode"));
                material.setQuantity(fieldSet.readString("quantity"));
                material.setUom(fieldSet.readString("uom"));
                material.setGroup(fieldSet.readString("group"));
                material.setMatCategory(fieldSet.readString("matCategory"));
                material.setDescription(fieldSet.readString("description"));
                return material;
            }
        });
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new SimpleRecordSeparatorPolicy() {
            @Override
            public boolean isEndOfRecord(String aLine) {
                if (aLine.trim().length() == 0) {
                    return false;
                }
                return super.isEndOfRecord(aLine);
            }

            @Override
            public String postProcess(String aRecord) {
                if (aRecord == null || aRecord.trim().length() == 0 || "null".equals(aRecord)) {
                    return null;
                }
                return super.postProcess(aRecord);
            }
        });

        itemReader.open(new ExecutionContext());

        List<Material> materials = new ArrayList<>();
        Set<Material> spoolTags = new TreeSet<>(new Comparator<Material>() {
            @Override
            public int compare(Material o1, Material o2) {
                return o1.getSpoolId().compareTo(o2.getSpoolId());
            }
        });

        Material material;
        do {
            material = itemReader.read();
            if (material != null) {
                materials.add(material);
                if (StringUtils.isNotEmpty(material.getSpoolId())) {
                    Material spoolTag = (Material) BeanUtils.cloneBean(material);
                    spoolTag.setSize("");
                    spoolTag.setIdentCode("SPOOL-TAG");
                    spoolTag.setQuantity("");
                    spoolTag.setUom("");
                    spoolTag.setGroup("");
                    spoolTag.setMatCategory("");
                    spoolTag.setDescription("");
                    spoolTags.add(spoolTag);
                }
                log.debug("Read: " + material.toString());
            }
        } while (material != null);

        materials.addAll(spoolTags);
        log.debug("To Write: " + Arrays.toString(materials.toArray()));

        FlatFileItemWriter<Material> itemWriter = new FlatFileItemWriter<>();
        FileSystemResource resource = new FileSystemResource(String.valueOf(new Date().getTime()));
        itemWriter.setResource(resource);
        FormatterLineAggregator<Material> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFormat(Material.OUTPUT_LINE_FORMAT);
        BeanWrapperFieldExtractor<Material> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(Material.COLUMN_NAMES);
        lineAggregator.setFieldExtractor(extractor);
        itemWriter.setLineAggregator(lineAggregator);
        itemWriter.open(new ExecutionContext());
        itemWriter.write(materials);

        return resource;
    }

}
