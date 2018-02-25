package com.roche.pharma.customerportal.core.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.roche.pharma.customerportal.core.dto.AssayMenuResponse;
import com.roche.pharma.customerportal.core.framework.ErrorDTO;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.exception.JsonParsingException;
import com.roche.pharma.customerportal.core.services.utils.JsonConverter;
import com.roche.pharma.customerportal.core.services.utils.WebServiceJsonUtil;

public class WebServiceJsonUtilTest {

    @Test
    public void testFilterInput() {

        final String filterInput = WebServiceJsonUtil.filterInput("<script></script>");
        Assert.assertFalse(filterInput.contains("<"));

    }

    @Test
    public void testJsonConvertor() {

        final String input = JsonConverter.convertToJson(new HashMap<String,String>());
        Assert.assertTrue(StringUtils.isNotEmpty(input));

    }

    @Test
    public void testJsonParsingException() {
        final Type type = new TypeToken<ServiceResponse<AssayMenuResponse>>() {}.getType();
        try {
            JsonConverter.convertFromJson("[]", type);
            Assert.assertTrue(false);
        } catch (final JsonParsingException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void testJsonResponseException() {
         ServiceResponse<AssayMenuResponse> value = new ServiceResponse<AssayMenuResponse>();
         final List<ErrorDTO> list = new ArrayList<ErrorDTO>();
         final ErrorDTO error = new ErrorDTO();
         error.setCode("L200");
         error.setMessage("Error in response");
         list.add(error);
        final Type type = new TypeToken<ServiceResponse<AssayMenuResponse>>() {}.getType();
        try {
            value = JsonConverter.convertFromJson("[]", type);
            Assert.assertTrue(false);
        } catch (final JsonParsingException e) {
            value.setErrorMsg("JSON Parsing Exception");
            value.setErrors(list);
            value.setResponseCode(500);
            value.setSuccessMsg("");
            Assert.assertTrue(true);
        }


    }

    @Test
    public void testNullInput() {
        final String filterInput = WebServiceJsonUtil.filterInput(null);
        Assert.assertFalse(filterInput.contains("<"));

    }

    @Test
    public void testJsonObject() {

        final String filterInput = WebServiceJsonUtil.getJsonStringFromObject(new HashMap<String,String>());
        Assert.assertFalse(filterInput.contains("<"));

    }
}
