package com.roche.pharma.customerportal.core.framework;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The Class Response.
 *
 * @author Avinash kumar
 */
public class Response {

    /** The errors. */
    private List<ErrorDTO> errors;

    /** The error msg. */
    @SerializedName("error_message")
    private String errorMsg;

    /** The success msg. */
    @SerializedName("sucess_message")
    private String successMsg;

    /** The response code. */
    @SerializedName("responseCode")
    private Integer responseCode;

    /**
     * Gets the response code.
     *
     * @return the response code
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the response code.
     *
     * @param responseCode the new response code
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Gets the errors.
     *
     * @return the errors
     */
    public final List<ErrorDTO> getErrors() {
        return errors;
    }

    /**
     * Sets the errors.
     *
     * @param errors the new errors
     */
    public final void setErrors(final List<ErrorDTO> errors) {
        this.errors = errors;
    }

    /**
     * Gets the error msg.
     *
     * @return the error msg
     */
    public final String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the error msg.
     *
     * @param errorMsg the new error msg
     */
    public final void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Gets the success msg.
     *
     * @return the success msg
     */
    public final String getSuccessMsg() {
        return successMsg;
    }

    /**
     * Sets the success msg.
     *
     * @param successMsg the new success msg
     */
    public final void setSuccessMsg(final String successMsg) {
        this.successMsg = successMsg;
    }

}
