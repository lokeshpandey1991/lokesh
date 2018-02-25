package com.roche.pharma.customerportal.core.services.models;

/**
 * The Class Header.
 *
 * @author Avinash kumar
 */
public class Header {

    /** The token. */
    private String token;

    /** The password reset token. */
    private String passwordResetToken;

    /** The unique identifier. */
    private String uniqueIdentifier;

    /**
     * Gets the token.
     *
     * @return the token
     */
    public final String getToken() {
        return token;
    }

    /**
     * Sets the token header.
     *
     * @param token the new token header
     */
    public final void setTokenHeader(final String token) {
        this.token = token;
    }

    /**
     * Gets the password reset token.
     *
     * @return the password reset token
     */
    public final String getPasswordResetToken() {
        return passwordResetToken;
    }

    /**
     * Sets the password reset token.
     *
     * @param passwordResetToken the new password reset token
     */
    public final void setPasswordResetToken(final String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    /**
     * Gets the unique identifier.
     *
     * @return the unique identifier
     */
    public final String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Sets the unique identifier.
     *
     * @param uniqueIdentifier the new unique identifier
     */
    public final void setUniqueIdentifier(final String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

}
