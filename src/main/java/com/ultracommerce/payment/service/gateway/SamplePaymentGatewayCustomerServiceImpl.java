/*
 * #%L
 * UltraCommerce Sample Payment Gateway
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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

import com.ultracommerce.common.payment.CreditCardType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.GatewayCustomerDTO;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayCustomerService;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.vendor.sample.service.payment.PaymentTransactionType;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Random;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Service("ucSamplePaymentGatewayCustomerService")
public class SamplePaymentGatewayCustomerServiceImpl extends AbstractPaymentGatewayCustomerService {

    public static final String RESPONSE_MAP_KEY_PREFEIX = "customer";

    @Override
    public PaymentResponseDTO createGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.customerPopulated(),
                "The Gateway Customer information on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Add payment nonce to the request
        // 3. Make the request
        // 4. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.CREATE_CUSTOMER);
    }

    @Override
    public PaymentResponseDTO updateGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.customerPopulated()
                        && requestDTO.getCustomer().getCustomerId() != null,
                "The Gateway Customer information on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Add payment nonce to the request
        // 3. Make the request
        // 4. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.UPDATE_CUSTOMER);
    }

    @Override
    public PaymentResponseDTO deleteGatewayCustomer(PaymentRequestDTO requestDTO) throws PaymentException {
        Assert.isTrue(requestDTO.getCustomer() != null && requestDTO.getCustomer().getCustomerId() != null,
                "The Gateway Customer Customer ID on the Payment Request DTO must not be null and must be populated");

        // Items to do in realistic implementation:
        // 1. Use requestDTO#getCustomer() to populate a request to the payment gateway
        // 2. Make the request
        // 3. Parse the request into a PaymentResponseDTO

        return buildSampleGatewayResponse(requestDTO, PaymentTransactionType.DELETE_CUSTOMER);
    }

    protected PaymentResponseDTO buildSampleGatewayResponse(PaymentRequestDTO requestDTO, PaymentTransactionType transactionType) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY)
                .paymentTransactionType(transactionType)
                .successful(true);

        parseCustomer(responseDTO, requestDTO);
        parseCreditCard(responseDTO, requestDTO);
        parsePaymentToken(responseDTO);

        return responseDTO;
    }

    protected void parseCustomer(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        GatewayCustomerDTO<PaymentRequestDTO> customer = requestDTO.getCustomer();

        responseDTO.customer().customerId(customer.getCustomerId());

        responseDTO.responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "id", customer.getCustomerId())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "firstName", customer.getFirstName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "lastName", customer.getLastName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "emailAddress", customer.getEmail())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "phoneNumber", customer.getPhone())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "company", customer.getCompanyName())
                .responseMap(RESPONSE_MAP_KEY_PREFEIX + "." + "website", customer.getWebsite());
    }

    protected void parseCreditCard(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        GatewayCustomerDTO<PaymentRequestDTO> customer = requestDTO.getCustomer();

        responseDTO.creditCard()
                .creditCardHolderName(customer.getFirstName() + " " + customer.getLastName())
                .creditCardLastFour("1111")
                .creditCardType(CreditCardType.MASTERCARD.getType())
                .creditCardExpDate("01/99");
    }

    protected void parsePaymentToken(PaymentResponseDTO responseDTO) {
        Random rnd = new Random();
        int randomNumber = 100000 + rnd.nextInt(90000000);

        responseDTO.paymentToken("SAMPLE_PAYMENT_GATEWAY_MULTI_USE_TOKEN_" + randomNumber);
    }
}
