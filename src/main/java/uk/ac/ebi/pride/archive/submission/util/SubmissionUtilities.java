package uk.ac.ebi.pride.archive.submission.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.submission.controller.SubmissionController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;

/**
 * @author Jose A. Dianes
 * @author Rui Wang
 * @version $Id$
 */
public class SubmissionUtilities {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionUtilities.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * Generate a unique reference for the PX submission
     *
     * @return submission reference
     */
    public static String generateSubmissionReference() {
        String dateStamp = getCurrentDateStamp();
        SecureRandom random = new SecureRandom();

        return "1-" + dateStamp + "-" + new BigInteger(16, random).toString(8).toUpperCase();
    }

    /**
     * Create ftp folder for upload, this however doesn't create the actually folder
     * The creation of the folder should be done by the client side,
     * this is a design compromise for EBI file system permissions
     */
    public static File createUploadFolder(File folder, String userName) {
        String path = folder.getAbsolutePath();

        String[] parts = userName.split("@");

        String newPath = path + FILE_SEPARATOR + parts[0] + "_" + getCurrentTimestamp();
        File uploadDirectory = new File(newPath);

        int cnt = 1;
        while (uploadDirectory.exists()) {
            uploadDirectory = new File(newPath + "_" + cnt);
            cnt++;
        }

        return uploadDirectory;
    }

    private static String getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        StringBuilder timestamp = new StringBuilder();
        timestamp.append(year);
        timestamp.append(month < 10 ? "0" : "");
        timestamp.append(month);
        timestamp.append(day < 10 ? "0" : "");
        timestamp.append(day);
        timestamp.append("_");
        timestamp.append(hour < 10 ? "0" : "");
        timestamp.append(hour);
        timestamp.append(minute < 10 ? "0" : "");
        timestamp.append(minute);
        timestamp.append(second < 10 ? "0" : "");
        timestamp.append(second);

        return timestamp.toString();
    }


    private static String getCurrentDateStamp() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + ((month < 10) ? "0" : "") + month + ((day < 10) ? "0" : "") + day;
    }
}
