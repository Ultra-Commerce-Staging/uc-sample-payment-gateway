/*
 * #%L
 * BroadleafCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt).
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Broadleaf Commerce, LLC
 * The intellectual and technical concepts contained
 * herein are proprietary to Broadleaf Commerce, LLC
 * and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Broadleaf Commerce, LLC.
 * #L%
 */
package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayHostedService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * This is an example implementation of a {@link PaymentGatewayHostedService}.
 * This is just a sample that mimics what URL a real hosted payment gateway implementation
 * might generate. This mimics implementations like PayPal Express Checkout
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blSamplePaymentGatewayHostedService")
public class SamplePaymentGatewayHostedServiceImpl extends AbstractPaymentGatewayHostedService {

    @Resource(name = "blSamplePaymentGatewayHostedConfiguration")
    protected SamplePaymentGatewayHostedConfiguration configuration;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO requestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                SamplePaymentGatewayType.NULL_HOSTED_GATEWAY)
                .completeCheckoutOnCallback(requestDTO.isCompleteCheckoutOnCallback())
                .responseMap(SamplePaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId())
                .responseMap(SamplePaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal())
                .responseMap(SamplePaymentGatewayConstants.HOSTED_REDIRECT_URL,
                        configuration.getHostedRedirectUrl());
        return responseDTO;
    }

}
