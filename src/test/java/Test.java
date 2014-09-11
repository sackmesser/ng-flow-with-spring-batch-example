import com.ttp76.spooltaggenerator.domain.Material;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.*;

/**
 * Created by fvalmeida on 9/9/14.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        org.springframework.batch.item.file.FlatFileItemReader<Material> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("/M05-L1-PC-C16-1255_REV0_Bfile.txt"));
        DefaultLineMapper<Material> lineMapper = new DefaultLineMapper();
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(new String[]{
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
        });
        tokenizer.setColumns(new Range[]{
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
                }
        );
        tokenizer.setStrict(false);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new MaterialFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());

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
                System.out.println("Read: " + material.toString());
            }
        } while (material != null);

        materials.addAll(spoolTags);

        System.out.println(Arrays.toString(materials.toArray()));


        FlatFileItemWriter<Material> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("output.txt"));
        FormatterLineAggregator<Material> lineAggregator = new FormatterLineAggregator<Material>();
        lineAggregator.setFormat(
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
                "%-109s"
        );
        BeanWrapperFieldExtractor<Material> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{
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
        });
        lineAggregator.setFieldExtractor(extractor);
        itemWriter.setLineAggregator(lineAggregator);

        itemWriter.open(new ExecutionContext());

        itemWriter.write(materials);
    }

    public static class MaterialFieldSetMapper implements FieldSetMapper<Material> {
        public Material mapFieldSet(FieldSet fs) {

            if (fs == null) {
                return null;
            }

            Material material = new Material();
            material.setProjectNo(fs.readString("projectNo"));
            material.setUnitArea(fs.readString("unitArea"));
            material.setDesignArea(fs.readString("designArea"));
            material.setLineNo(fs.readString("lineNo"));
            material.setTrainSheetNo(fs.readString("trainSheetNo"));
            material.setPipelineRef(fs.readString("pipelineRef"));
            material.setRevNo(fs.readString("revNo"));
            material.setPipingClass(fs.readString("pipingClass"));
            material.setSpoolId(fs.readString("spoolId"));
            material.setSize(fs.readString("size"));
            material.setIdentCode(fs.readString("identCode"));
            material.setQuantity(fs.readString("quantity"));
            material.setUom(fs.readString("uom"));
            material.setGroup(fs.readString("group"));
            material.setMatCategory(fs.readString("matCategory"));
            material.setDescription(fs.readString("description"));

            return material;
        }
    }

    public static class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

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
    }
}
