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

import org.apache.commons.lang.ArrayUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
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
 * This is an example implementation of a {@link PaymentGatewayWebResponseService}.
 * This will translate the Post information back from
 * {@link com.ultracommerce.vendor.sample.web.controller.mock.processor.SampleMockProcessorController}
 * into a PaymentResponseDTO for processing in the Ultra System.
 *
 * Replace with a real Payment Gateway Integration like Braintree or PayPal PayFlow.
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayWebResponseService")
public class SamplePaymentGatewayWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService {

    @Resource(name = "ucPaymentGatewayWebResponsePrintService")
    protected PaymentGatewayWebResponsePrintService webResponsePrintService;

    @Resource(name = "ucSamplePaymentGatewayConfiguration")
    protected SamplePaymentGatewayConfiguration configuration;

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
                SamplePaymentGatewayType.NULL_GATEWAY)
                .rawResponse(webResponsePrintService.printRequest(request));

        Map<String,String[]> paramMap = request.getParameterMap();

        Money amount = Money.ZERO;
        if (paramMap.containsKey(SamplePaymentGatewayConstants.TRANSACTION_AMT)) {
            String amt = paramMap.get(SamplePaymentGatewayConstants.TRANSACTION_AMT)[0];
            amount = new Money(amt);
        }

        boolean approved = false;
        if (paramMap.containsKey(SamplePaymentGatewayConstants.RESULT_SUCCESS)) {
            String[] msg = paramMap.get(SamplePaymentGatewayConstants.RESULT_SUCCESS);
            if (ArrayUtils.contains(msg, "true")) {
                approved = true;
            }
        }

        PaymentTransactionType type = PaymentTransactionType.AUTHORIZE_AND_CAPTURE;
        if (!configuration.isPerformAuthorizeAndCapture()) {
            type = PaymentTransactionType.AUTHORIZE;
        }

        responseDTO.successful(approved)
                .amount(amount)
                .paymentTransactionType(type)
                .orderId(parse(paramMap, SamplePaymentGatewayConstants.ORDER_ID))
                .customer()
                    .customerId(parse(paramMap, SamplePaymentGatewayConstants.CUSTOMER_ID))
                    .done()
                .paymentToken(parse(paramMap, SamplePaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(SamplePaymentGatewayConstants.GATEWAY_TRANSACTION_ID,
                        parse(paramMap, SamplePaymentGatewayConstants.GATEWAY_TRANSACTION_ID))
                .responseMap(SamplePaymentGatewayConstants.RESULT_MESSAGE,
                        parse(paramMap, SamplePaymentGatewayConstants.RESULT_MESSAGE))
                .responseMap(PaymentAdditionalFieldType.TOKEN.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.PAYMENT_TOKEN_ID))
                .responseMap(PaymentAdditionalFieldType.LAST_FOUR.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                .responseMap(PaymentAdditionalFieldType.CARD_TYPE.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_TYPE))
                .responseMap(PaymentAdditionalFieldType.NAME_ON_CARD.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_NAME))
                .responseMap(PaymentAdditionalFieldType.EXP_DATE.getType(),
                        parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
                .billTo()
                    .addressFirstName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_FIRST_NAME))
                    .addressLastName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_LAST_NAME))
                    .addressLine1(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1))
                    .addressLine2(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2))
                    .addressCityLocality(parse(paramMap, SamplePaymentGatewayConstants.BILLING_CITY))
                    .addressStateRegion(parse(paramMap, SamplePaymentGatewayConstants.BILLING_STATE))
                    .addressPostalCode(parse(paramMap, SamplePaymentGatewayConstants.BILLING_ZIP))
                    .addressCountryCode(parse(paramMap, SamplePaymentGatewayConstants.BILLING_COUNTRY))
                    .addressPhone(parse(paramMap, SamplePaymentGatewayConstants.BILLING_PHONE))
                    .addressEmail(parse(paramMap, SamplePaymentGatewayConstants.BILLING_EMAIL))
                    .addressCompanyName(parse(paramMap, SamplePaymentGatewayConstants.BILLING_COMPANY_NAME))
                    .done()
                .creditCard()
                    .creditCardHolderName(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_NAME))
                    .creditCardLastFour(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_LAST_FOUR))
                    .creditCardType(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_TYPE))
                    .creditCardExpDate(parse(paramMap, SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE))
                    .done();

        return responseDTO;

    }

    protected String parse(Map<String,String[]> paramMap, String param) {
        if (paramMap.containsKey(param)) {
            return paramMap.get(param)[0];
        }

        return null;
    }


}
