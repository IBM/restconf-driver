package com.ibm.restconf.service.impl;

import com.hubspot.jinjava.Jinjava;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.service.MessageConversionException;
import com.ibm.restconf.service.MessageConversionService;
import com.ibm.restconf.service.MissingPropertyException;
import com.ibm.restconf.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("JinJavaMessageConversionServiceImpl")
public class JinJavaMessageConversionServiceImpl implements MessageConversionService {

    private static final Logger logger = LoggerFactory.getLogger(JinJavaMessageConversionServiceImpl.class);
    private static final String TEMPLATE_PATH = "template/";

    @Override
    public String generateMessageFromRequest(String messageType, ExecutionRequest executionRequest) throws MessageConversionException {
        Map<String, Object> resoureceProperties = executionRequest.getProperties();
        String contentType = (String)resoureceProperties.get("ContentType");
        String fullTemplateName = messageType + ".xml";
        if(!StringUtils.isEmpty(contentType) && contentType.equalsIgnoreCase("json")){
            fullTemplateName = messageType+ ".json";
        }
        logger.debug("fullTemplateName  {} \n", fullTemplateName);
        final String template = getTemplateFromExecutionRequest(executionRequest, fullTemplateName);
        logger.debug("template file from ExecutionRequest {} \n", template);
        final Jinjava jinjava = new Jinjava();
        try {
            String returnVal = jinjava.render(template, createJinJavaContext(executionRequest.getProperties(), template));
            logger.info("Message conversion script successfully run, returnVal is\n{}", returnVal);
            return returnVal;
        } catch (IOException e) {
            throw new MessageConversionException("Exception caught executing a script", e);
        }
    }


    private String getTemplateFromExecutionRequest(final ExecutionRequest executionRequest, final String fullTemplateName) {

        String templateContents = FileUtils.getFileFromLifecycleScripts(executionRequest.getDriverFiles(), TEMPLATE_PATH + fullTemplateName);

        if (templateContents == null) {
            try (InputStream inputStream = JinJavaMessageConversionServiceImpl.class.getResourceAsStream("/" + TEMPLATE_PATH +  "/" + fullTemplateName)) {
                if (inputStream != null) {
                    templateContents = IOUtils.toString(inputStream, Charset.defaultCharset());
                }
            } catch (IOException e) {
                logger.error("Exception raised looking up default lifecycle script", e);
            }
        }
        if (templateContents != null) {
            return templateContents;
        } else {
            throw new IllegalArgumentException(String.format("Unable to find a template called [%s]", fullTemplateName));
        }
    }


    private Map<String,Object> createJinJavaContext(Map<String, Object> resourceProperties, String templateContents) throws  IOException {
        Map<String, Object> context = new HashMap<>();
        List<String> list = findPropertyListFromTemplate(templateContents);
        logger.debug("list of properties in template {}", list);
        list.forEach(property -> {
            Object value = resourceProperties.get(property);
            logger.debug("property --> value  {} ---> {}", property, value);
            if (value == null) {
                throw new MissingPropertyException("Missing value for resource property: " + property);
            }
            context.put(property, value);
        });
        return context;
    }

    private List<String> findPropertyListFromTemplate(String templateContents) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
        Matcher match = pattern.matcher(templateContents);
        while(match.find()) {
            int start = match.start(0);
            int end = match.end(0);
            String extractedProperty = templateContents.substring(start + 2, end - 2);
            logger.debug("Extracted property {}", extractedProperty);
            list.add(extractedProperty);
        }
        if(list.isEmpty()){
            logger.debug("No properties extracted from the template {}", templateContents);
        }
        return list;
    }
}
