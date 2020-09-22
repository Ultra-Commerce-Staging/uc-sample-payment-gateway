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

import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayWebResponseService;
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponseService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayHostedWebResponseService")
public class SamplePaymentGatewayHostedWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService {

    @Resource(name = "ucPaymentGatewayWebResponsePrintService")
    protected PaymentGatewayWebResponsePrintService webResponsePrintService;

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT,
                SamplePaymentGatewayType.NULL_HOSTED_GATEWAY)
                .rawResponse(webResponsePrintService.printRequest(request));

        Map<String,String[]> paramMap = request.getParameterMap();

        Money amount = Money.ZERO;
        if (paramMap.containsKey(SamplePaymentGatewayConstants.TRANSACTION_AMT)) {
            String amt = paramMap.get(SamplePaymentGatewayConstants.TRANSACTION_AMT)[0];
            amount = new Money(amt);
        }

        responseDTO.successful(true)
                .completeCheckoutOnCallback(Boolean.parseBoolean(paramMap.get(SamplePaymentGatewayConstants.COMPLETE_CHECKOUT_ON_CALLBACK)[0]))
                .amount(amount)
                .paymentTransactionType(PaymentTransactionType.UNCONFIRMED)
                .orderId(paramMap.get(SamplePaymentGatewayConstants.ORDER_ID)[0])
                .responseMap(SamplePaymentGatewayConstants.RESULT_MESSAGE,
                        paramMap.get(SamplePaymentGatewayConstants.RESULT_MESSAGE)[0]);

        return responseDTO;
    }

}
