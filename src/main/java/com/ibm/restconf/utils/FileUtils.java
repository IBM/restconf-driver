package com.ibm.restconf.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String getFileFromLifecycleScripts(final String lifecycleScripts, final String filename) {
        // Attempt to extract the script from the executionRequest passed in
        if (!StringUtils.isEmpty(lifecycleScripts)) {
            // lifecycleScripts should contain (if not empty) a Base64 encoded Zip file of all scripts concerning the VNFM driver
            byte[] decodedByteArray = Base64.getDecoder().decode(lifecycleScripts);

            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(decodedByteArray))) {
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    logger.trace("Found zip entry: {}", entry);
                    if (filename.equalsIgnoreCase(entry.getName())) {
                        logger.debug("Found file called [{}], extracting...", entry.getName());
                        final String script = IOUtils.toString(zis, Charset.defaultCharset());
                        logger.debug("File content is\n{}", script);
                        return script;
                    }
                    // Get the next entry for the loop
                    entry = zis.getNextEntry();
                }
            } catch (IOException e) {
                logger.error("Exception raised reading lifecycle scripts", e);
            }
        }

        return null;
    }
}