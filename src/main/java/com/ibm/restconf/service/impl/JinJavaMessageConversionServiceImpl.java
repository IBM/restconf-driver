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
    private static final String TEMPLATE_PATH = "templates/";

    @Override
    public String generateMessageFromRequest(String messageType, ExecutionRequest executionRequest) throws MessageConversionException {
        Map<String, Object> resoureceProperties = executionRequest.getProperties();
        String contentType = (String)resoureceProperties.get("ContentType");
        String fullTemplateName = messageType + ".xml";
        if(!StringUtils.isEmpty(contentType) && contentType.equalsIgnoreCase("json")){
            fullTemplateName = messageType+ ".json";
        }

        final String template = getTemplateFromExecutionRequest(executionRequest, fullTemplateName);
        final Jinjava jinjava = new Jinjava();
        try {
            String returnVal = jinjava.render(template, createJinJavaContext(executionRequest.getProperties(), fullTemplateName));
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


    private Map<String,Object> createJinJavaContext(Map<String, Object> resourceProperties, String templateFile) throws  IOException {
        List<String> list = findPropertyListFromTemplate(templateFile);
        Map<String, Object> context = new HashMap<>();
        list.forEach(property -> {
            Object value = resourceProperties.get(property);
            if (value == null){
                throw new MissingPropertyException("Missing value for resource property: "+ property);
            }
            context.put(property, resourceProperties.get(property));
        });
        return context;
    }

    private List<String> findPropertyListFromTemplate(String templateFile) throws IOException {
        List<String> list = new ArrayList<>();
        try (InputStream inputStream = JinJavaMessageConversionServiceImpl.class.getResourceAsStream("/" + TEMPLATE_PATH +  "/" + templateFile)) {
            if (inputStream != null) {
                BufferedReader read = new BufferedReader(
                        new InputStreamReader(inputStream));
                Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
                String line;
                while ((line = read.readLine()) != null) {
                    Matcher match = pattern.matcher(line);
                    while (match.find()) {
                        int start = match.start(0);
                        int end = match.end(0);
                        list.add(line.substring(start+2, end-2));
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Exception raised looking up default lifecycle script", e);
            throw new FileNotFoundException("Default template not found");
        }

        return list;
    }
}
