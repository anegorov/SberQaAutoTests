package com.sber.qa.generators;

import freemarker.template.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log4j2
@Component
public class FreemarkerEngine {

    public static final Version VERSION = Configuration.VERSION_2_3_28;
    private final Configuration cfg;

    public FreemarkerEngine() {
        cfg = new Configuration(VERSION);
        try {
            cfg.setDirectoryForTemplateLoading(new File("templates"));
        } catch (IOException e) {
            log.error("Can't find templates directory");
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setWrapUncheckedExceptions(true);
    }

    private String renderDoc(Template templ, Map<String,String> dataModel) throws IOException, TemplateException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
        try (Writer out = new OutputStreamWriter(bos)){
            templ.process(dataModel, out);
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
//            return new String(bos.toByteArray(), Charset.forName("windows-1251"));
        }
    }

    public String createBody(String fileTemplateName, Map<String, String> messageBodyParameters) {
        try {
            Template temp = cfg.getTemplate(fileTemplateName);
            return renderDoc(temp,messageBodyParameters);
        } catch (IOException ioe) {
            log.error("Can't find template file: " + fileTemplateName);
        } catch (TemplateException te) {
            log.error("Template file conversion problem: " + te.getMessage());
        }
        return "default body";
    }
}
