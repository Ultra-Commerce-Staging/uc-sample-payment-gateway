/*
 * #%L
 * UltraCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2015 Ultra Commerce
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
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponseService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.common.web.payment.controller.CustomerPaymentGatewayAbstractController;
import com.ultracommerce.payment.service.gateway.SamplePaymentGatewayConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller("ucSamplePaymentGatewayCustomerPaymentOMSController")
@RequestMapping("/" + SamplePaymentGatewayCustomerPaymentOMSController.GATEWAY_CONTEXT_KEY)
public class SamplePaymentGatewayCustomerPaymentOMSController extends CustomerPaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(SamplePaymentGatewayCustomerPaymentOMSController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "sample-customer-payment-oms";

    protected static final String CUSTOMER_PAYMENT_ERROR = "CUSTOMER_PAYMENT_OMS_ERROR";
    protected static String customerPaymentErrorMessage = "customerPaymentOMSErrorMessage";

    @Resource(name = "ucSamplePaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "ucSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return paymentGatewayConfiguration;
    }

    @Override
    public String getCustomerPaymentViewRedirect(String customerPaymentId) {
        return "redirect:/oms-csrtools/checkout";
    }

    @Override
    public String getCustomerPaymentErrorRedirect() {
        return "redirect:/oms-csrtools/payment-methods/add";
    }

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(CUSTOMER_PAYMENT_ERROR, customerPaymentErrorMessage);
    }

    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public String returnEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                 Map<String, String> pathVars) throws PaymentException {
        return super.createCustomerPayment(model, request, redirectAttributes);
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
        redirectAttributes.addAttribute(CUSTOMER_PAYMENT_ERROR,
                request.getParameter(CUSTOMER_PAYMENT_ERROR));
        return getCustomerPaymentErrorRedirect();
    }

}
