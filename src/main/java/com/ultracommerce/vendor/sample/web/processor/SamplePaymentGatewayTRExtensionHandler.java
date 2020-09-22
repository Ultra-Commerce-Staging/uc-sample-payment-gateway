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

package com.ultracommerce.vendor.sample.web.processor;

import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import com.ultracommerce.common.web.payment.processor.AbstractTRCreditCardExtensionHandler;
import com.ultracommerce.common.web.payment.processor.TRCreditCardExtensionManager;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This sample handler will add itself to the {@link TRCreditCardExtensionManager}
 * and will add some default hidden parameters/form POST URL for our fake
 * {@link com.ultracommerce.vendor.sample.web.controller.mock.processor.SampleMockProcessorController}
 *
 * Note, we don't want this loaded into the extension manager
 * if a real payment gateway is used, so make sure to not scan this class when
 * using a real implementation. This is for demo purposes only.
 *
 * In order to use this sample extension handler, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayTRExtensionHandler")
public class SamplePaymentGatewayTRExtensionHandler extends AbstractTRCreditCardExtensionHandler {

    public static final String FORM_ACTION_URL = SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
    public static final String FORM_HIDDEN_PARAMS = "FORM_HIDDEN_PARAMS";

    @Resource(name = "ucTRCreditCardExtensionManager")
    protected TRCreditCardExtensionManager extensionManager;

    @Resource(name = "ucSamplePaymentGatewayTransparentRedirectService")
    protected PaymentGatewayTransparentRedirectService transparentRedirectService;

    @Resource(name = "ucSamplePaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getFormActionURLKey() {
        return FORM_ACTION_URL;
    }

    @Override
    public String getHiddenParamsKey() {
        return FORM_HIDDEN_PARAMS;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return transparentRedirectService;
    }

    @Override
    public void populateFormParameters(Map<String, Map<String, String>> formParameters, PaymentResponseDTO responseDTO) {
        String actionUrl = (String) responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL);
        Map<String, String> actionValue = new HashMap<String, String>();
        actionValue.put(getFormActionURLKey(), actionUrl);
        formParameters.put(getFormActionURLKey(), actionValue);

        Map<String, String> hiddenFields = new HashMap<String, String>();

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.TRANSPARENT_REDIRECT_RETURN_URL).toString());
        }
        
        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.TRANSACTION_AMT) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.TRANSACTION_AMT,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.TRANSACTION_AMT).toString());
        }
        
        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.ORDER_ID) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.ORDER_ID,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.ORDER_ID).toString());
        }
        
        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.CUSTOMER_ID) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.CUSTOMER_ID,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.CUSTOMER_ID).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_FIRST_NAME) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_FIRST_NAME,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_LAST_NAME) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_LAST_NAME,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_CITY) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_CITY,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_STATE) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_STATE,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ZIP) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_ZIP,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_COUNTRY) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.BILLING_COUNTRY,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.BILLING_COUNTRY).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_LAST_NAME) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_LAST_NAME,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_CITY) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_CITY,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_STATE) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_STATE,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ZIP) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_ZIP,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_COUNTRY) != null) {
            hiddenFields.put(SamplePaymentGatewayConstants.SHIPPING_COUNTRY,
                    responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.SHIPPING_COUNTRY).toString());
        }

        formParameters.put(getHiddenParamsKey(), hiddenFields);
    }
}
