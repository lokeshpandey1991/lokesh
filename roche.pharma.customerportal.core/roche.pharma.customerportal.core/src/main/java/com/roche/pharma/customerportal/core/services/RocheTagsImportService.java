package com.roche.pharma.customerportal.core.services;

import com.roche.pharma.customerportal.core.beans.Data;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

/**
 * This service is used to create roche tags under etc/tags/customerportal/pim/product structure and creates basic
 * structure if not present
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface RocheTagsImportService {
    ServiceResponse<Data> getAllTags() throws WebserviceException;
}
