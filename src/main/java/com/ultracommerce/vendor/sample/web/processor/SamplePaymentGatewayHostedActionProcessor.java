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

import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.PaymentGatewayHostedService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraAttributeModifierProcessor;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>A Thymeleaf processor that will generate a Mock Hosted Link given a passed in PaymentRequestDTO.</p>
 *
 * <pre><code>
 * <form blc:sample_payment_hosted_action="${paymentRequestDTO}" complete_checkout="${false}" method="POST">
 *   <input type="image" src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" align="left" style="margin-right:7px;" alt="Submit Form" />
 * </form>
 * </code></pre>
 *
 * In order to use this sample processor, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("ucSamplePaymentGatewayHostedActionProcessor")
@ConditionalOnTemplating
public class SamplePaymentGatewayHostedActionProcessor extends AbstractUltraAttributeModifierProcessor {

    @Resource(name = "ucSamplePaymentGatewayHostedService")
    private PaymentGatewayHostedService paymentGatewayHostedService;

    @Override
    public String getName() {
        return "sample_payment_hosted_action";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }
    
    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, UltraTemplateContext context) {
        PaymentRequestDTO requestDTO = (PaymentRequestDTO) context.parseExpression(attributeValue);
        String url = "";
        Map<String, String> newAttributes = new HashMap<>();
        List<String> removedAttributes = new ArrayList<>();
        if (requestDTO != null) {
            if (tagAttributes.get("complete_checkout") != null) {
                Boolean completeCheckout = (Boolean) context.parseExpression(tagAttributes.get("complete_checkout"));
                removedAttributes.add("complete_checkout");
                requestDTO.completeCheckoutOnCallback(completeCheckout);
            }

            try {
                PaymentResponseDTO responseDTO = paymentGatewayHostedService.requestHostedEndpoint(requestDTO);
                url = responseDTO.getResponseMap().get(SamplePaymentGatewayConstants.HOSTED_REDIRECT_URL).toString();
            } catch (PaymentException e) {
                throw new RuntimeException("Unable to Create Sample Payment Gateway Hosted Link", e);
            }
        }
        newAttributes.put("action", url);
        return new UltraAttributeModifier(newAttributes, removedAttributes);
    }
}
