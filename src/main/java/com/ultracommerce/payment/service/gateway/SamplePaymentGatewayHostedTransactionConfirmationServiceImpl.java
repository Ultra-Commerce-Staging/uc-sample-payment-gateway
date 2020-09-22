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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayTransactionConfirmationService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionConfirmationService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayHostedTransactionConfirmationService")
public class SamplePaymentGatewayHostedTransactionConfirmationServiceImpl extends AbstractPaymentGatewayTransactionConfirmationService {

    protected static final Log LOG = LogFactory.getLog(SamplePaymentGatewayHostedTransactionConfirmationServiceImpl.class);

    @Resource(name = "ucSamplePaymentGatewayHostedConfiguration")
    protected SamplePaymentGatewayHostedConfiguration configuration;

    @Override
    public PaymentResponseDTO confirmTransaction(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sample Payment Hosted Gateway - Confirming Transaction with amount: " + paymentRequestDTO.getTransactionTotal());
        }

        PaymentTransactionType type = PaymentTransactionType.AUTHORIZE_AND_CAPTURE;
        if (!configuration.isPerformAuthorizeAndCapture()) {
            type = PaymentTransactionType.AUTHORIZE;
        }

        return new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                SamplePaymentGatewayType.NULL_HOSTED_GATEWAY)
                .rawResponse("confirmation - successful")
                .successful(true)
                .paymentTransactionType(type)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()));
    }

}
