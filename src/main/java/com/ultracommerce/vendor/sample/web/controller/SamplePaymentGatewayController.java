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

package com.ultracommerce.vendor.sample.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponseService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.common.web.payment.controller.PaymentGatewayAbstractController;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a sample implementation of {@link PaymentGatewayAbstractController}
 * that mimics what a real Payment Gateway Controller might look like.
 * This will handle translating the "fake response" from our SamplePaymentGateway
 * so that it can be processed in the system.
 *
 * In order to use this sample controller, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Controller("ucSamplePaymentGatewayController")
@RequestMapping("/" + SamplePaymentGatewayController.GATEWAY_CONTEXT_KEY)
public class SamplePaymentGatewayController extends PaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(SamplePaymentGatewayController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "sample-checkout";

    @Resource(name = "ucSamplePaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "ucSamplePaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, getProcessingErrorMessage());
    }

    @Override
    public void handleUnsuccessfulTransaction(Model model, RedirectAttributes redirectAttributes,
                                              PaymentResponseDTO responseDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("The Transaction was unsuccessful for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Errors to Redirect Attributes.");
        }
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.RESULT_MESSAGE));
    }

    @Override
    public String getGatewayContextKey() {
        return GATEWAY_CONTEXT_KEY;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return paymentGatewayConfiguration;
    }

    @Override
    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public String returnEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                 Map<String, String> pathVars) throws PaymentException {
        return super.process(model, request, redirectAttributes);
    }

    @Override
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                request.getParameter(PAYMENT_PROCESSING_ERROR));
        return getOrderReviewRedirect();
    }


}
