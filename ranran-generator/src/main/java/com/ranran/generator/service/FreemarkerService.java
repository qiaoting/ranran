package com.ranran.generator.service;

import com.ranran.generator.domain.dto.ClassInfoDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;

/**
 * @author ranran
 */
@Service
public class FreemarkerService {
    private Configuration configuration;
    @PostConstruct
    public void init() {
        configuration = new Configuration();
        try {
            configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
            configuration.setDefaultEncoding("UTF-8");
            configuration.setClassicCompatible(true);
            configuration.setNumberFormat("0.##");
        } catch (Exception e) {
            throw new RuntimeException("初始化freemarker配置失败", e);
        }
    }

    public String generateString(String templateName, ClassInfoDto classInfoDto) {
        try {
            Template template = configuration.getTemplate(templateName);
            Writer writer = new StringWriter();
            template.process(classInfoDto, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("freemarker模板渲染失败：" + templateName, e);
        }
    }
}
