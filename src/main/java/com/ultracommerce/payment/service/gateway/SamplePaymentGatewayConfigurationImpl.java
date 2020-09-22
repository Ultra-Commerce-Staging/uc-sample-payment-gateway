/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2013 Ultra Commerce
 * %%
 * Licensed under the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt).
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Ultra Commerce, LLC
 * The intellectual and technical concepts contained
 * herein are proprietary to Ultra Commerce, LLC
 * and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Ultra Commerce, LLC.
 * #L%
 */

package com.ultracommerce.payment.service.gateway;

import com.ultracommerce.common.payment.PaymentGatewayType;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayConfiguration;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

/**
 * In order to use load this demo service, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayConfiguration")
public class SamplePaymentGatewayConfigurationImpl extends AbstractPaymentGatewayConfiguration implements SamplePaymentGatewayConfiguration {

    protected int failureReportingThreshold = 1;

    protected boolean performAuthorizeAndCapture = false;

    @Override
    public String getTransparentRedirectUrl() {
        return UltraRequestContext.getUltraRequestContext().getWebRequest().getContextPath() + "/sample-checkout/process";
    }

    @Override
    public String getTransparentRedirectReturnUrl() {
        return UltraRequestContext.getUltraRequestContext().getWebRequest().getContextPath() + "/sample-checkout/return";
    }
    
    @Override
    public String getCustomerPaymentTransparentRedirectUrl() {
        return UltraRequestContext.getUltraRequestContext().getWebRequest().getContextPath() + "/sample-customer-payment/process";
    }
    
    @Override
    public String getCustomerPaymentTransparentRedirectReturnUrl() {
        return UltraRequestContext.getUltraRequestContext().getWebRequest().getContextPath() + "/sample-customer-payment/return";
    }


    @Override
    public boolean isPerformAuthorizeAndCapture() {
        return true;
    }

    @Override
    public void setPerformAuthorizeAndCapture(boolean performAuthorizeAndCapture) {
        this.performAuthorizeAndCapture = performAuthorizeAndCapture;
    }

    @Override
    public int getFailureReportingThreshold() {
        return failureReportingThreshold;
    }

    @Override
    public void setFailureReportingThreshold(int failureReportingThreshold) {
        this.failureReportingThreshold = failureReportingThreshold;
    }

    @Override
    public boolean handlesAuthorize() {
        return true;
    }

    @Override
    public boolean handlesCapture() {
        return false;
    }

    @Override
    public boolean handlesAuthorizeAndCapture() {
        return true;
    }

    @Override
    public boolean handlesReverseAuthorize() {
        return false;
    }

    @Override
    public boolean handlesVoid() {
        return false;
    }

    @Override
    public boolean handlesRefund() {
        return false;
    }

    @Override
    public boolean handlesPartialCapture() {
        return false;
    }

    @Override
    public boolean handlesMultipleShipment() {
        return false;
    }

    @Override
    public boolean handlesRecurringPayment() {
        return false;
    }

    @Override
    public boolean handlesSavedCustomerPayment() {
        return false;
    }

    @Override
    public boolean handlesMultiplePayments() {
        return false;
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return SamplePaymentGatewayType.NULL_GATEWAY;
    }
}
