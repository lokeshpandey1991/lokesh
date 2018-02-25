package com.roche.pharma.customerportal.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.roche.pharma.customerportal.core.constants.RocheConstants;

/**
 * The Class RocheDateUtil.
 */
public final class RocheDateUtil {
    
    /**
     * Instantiates a new roche date util.
     */
    private RocheDateUtil() {
        
    }
    
    /**
     * Gets the event date.
     * @param eventStartDate the event start date
     * @param eventEndDate the event end date
     * @return the event date
     */
    public static String getEventDate(Calendar eventStartDate, Calendar eventEndDate) {
        String publishDate = StringUtils.EMPTY;
        final DateFormat fmt = new SimpleDateFormat("MMMM dd", Locale.US);
        fmt.setTimeZone(eventEndDate.getTimeZone());
        final DateFormat defaultFormet = new SimpleDateFormat(RocheConstants.DEFAULT_DATE_DISPLAY_FORMAT, Locale.US);
        defaultFormet.setTimeZone(eventEndDate.getTimeZone());
        if (eventStartDate.get(Calendar.MONTH) == eventEndDate.get(Calendar.MONTH)
                && eventStartDate.get(Calendar.YEAR) == eventEndDate.get(Calendar.YEAR)) {
            publishDate = fmt.format(eventStartDate.getTime()) + "-" + eventEndDate.get(Calendar.DATE) + ", "
                    + eventEndDate.get(Calendar.YEAR);
            
        } else if (eventStartDate.get(Calendar.MONTH) != eventEndDate.get(Calendar.MONTH)
                && eventStartDate.get(Calendar.YEAR) == eventEndDate.get(Calendar.YEAR)) {
            publishDate = fmt.format(eventStartDate.getTime()) + "-" + fmt.format(eventEndDate.getTime()) + ", "
                    + eventEndDate.get(Calendar.YEAR);
        } else {
            publishDate = defaultFormet.format(eventStartDate.getTime()) + "-"
                    + defaultFormet.format(eventEndDate.getTime());
        }
        return publishDate;
        
    }
}
