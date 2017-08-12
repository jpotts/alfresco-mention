package com.metaversant.alfresco.mentions.service;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for scanning an input stream for @mentions.
 *
 * Created by jpotts, Metaversant on 7/31/17.
 */
public class MentionScanner {

    private static Logger logger = Logger.getLogger(MentionScanner.class);

    public static List<String> getUsers(InputStream inputStream) {
        List<String> userList = new ArrayList<String>();

        // for right now, copy the inputStream into a String
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    logger.error(ioe);
                }
            }
        }

        String regex = "@([A-Za-z0-9.\\-_]+)";
        Matcher matcher = Pattern.compile(regex).matcher(writer.toString());
        while (matcher.find()) {
            String userName = matcher.group(1);
            if (userName.endsWith(".")) {
                // Let's assume that no one would name a user ending with a dot, even though that is technically
                // an acceptable userName. This lets us correctly find mentions at the end of a sentence.
                userName = userName.substring(0, userName.length() -1);
            }
            userList.add(userName);
        }

        return userList;
    }

}
