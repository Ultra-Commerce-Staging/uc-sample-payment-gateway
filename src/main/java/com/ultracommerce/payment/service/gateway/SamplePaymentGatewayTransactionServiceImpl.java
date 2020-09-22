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
package com.ultracommerce.payment.service.gateway;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.CreditCardValidator;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.payment.CreditCardType;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.payment.PaymentGatewayRequestType;
import com.ultracommerce.common.payment.PaymentTransactionType;
import com.ultracommerce.common.payment.PaymentType;
import com.ultracommerce.common.payment.dto.CreditCardDTO;
import com.ultracommerce.common.payment.dto.PaymentRequestDTO;
import com.ultracommerce.common.payment.dto.PaymentResponseDTO;
import com.ultracommerce.common.payment.service.AbstractPaymentGatewayTransactionService;
import com.ultracommerce.common.payment.service.FailureCountExposable;
import com.ultracommerce.common.vendor.service.exception.PaymentException;
import com.ultracommerce.common.vendor.service.type.ServiceStatusType;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import com.ultracommerce.vendor.sample.service.payment.SamplePaymentGatewayType;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * This is an example implementation of a {@link com.ultracommerce.common.payment.service.PaymentGatewayTransactionService}.
 * This handles the scenario where the implementation is PCI-Compliant and
 * the server directly handles the Credit Card PAN. If so, this service should make
 * a server to server call to charge the card against the configured gateway.
 *
 * In order to use load this demo service, you will need to component scan
 * the package "com.ultracommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayTransactionService")
public class SamplePaymentGatewayTransactionServiceImpl extends AbstractPaymentGatewayTransactionService implements FailureCountExposable {

    protected Integer failureCount = 0;
    protected Boolean isUp = true;
    
    @Override
    public PaymentResponseDTO authorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE);
    }

    @Override
    public PaymentResponseDTO capture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.CAPTURE)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Capture")
                .successful(true);

        return responseDTO;
    }

    @Override
    public PaymentResponseDTO authorizeAndCapture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
    }

    @Override
    public PaymentResponseDTO reverseAuthorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.REVERSE_AUTH)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Reverse Authorization")
                .successful(true);

        return responseDTO;
    }

    @Override
    public PaymentResponseDTO refund(PaymentRequestDTO paymentRequestDTO) throws PaymentException {

        //This null gateway implementation will mimic the ability to support a DETACHED_CREDIT refund.
        if (paymentRequestDTO.getAdditionalFields().containsKey(PaymentGatewayRequestType.DETACHED_CREDIT_REFUND.getType())){
            PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
            responseDTO.valid(true)
                    .paymentTransactionType(PaymentTransactionType.DETACHED_CREDIT)
                    .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                    .rawResponse("Successful Detached Credit")
                    .successful(true);

            return responseDTO;
        } else {
            //This is a normal "follow-on" refund request
            PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
            responseDTO.valid(true)
                    .paymentTransactionType(PaymentTransactionType.REFUND)
                    .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                    .rawResponse("Successful Refund")
                    .successful(true);

            return responseDTO;
        }

    }

    @Override
    public PaymentResponseDTO voidPayment(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(PaymentTransactionType.VOID)
                .amount(new Money(paymentRequestDTO.getTransactionTotal()))
                .rawResponse("Successful Reverse Authorization")
                .successful(true);

        return responseDTO;
    }

    /**
     * Does minimal Credit Card Validation (luhn check and expiration date is after today).
     * Mimics the Response of a real Payment Gateway.
     *
     * @param requestDTO
     * @param paymentTransactionType
     * @return
     */
    protected PaymentResponseDTO commonCreditCardProcessing(PaymentRequestDTO requestDTO, PaymentTransactionType paymentTransactionType) {
        setupNoncePaymentRequest(requestDTO);
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD, SamplePaymentGatewayType.NULL_GATEWAY);
        responseDTO.valid(true)
                .paymentTransactionType(paymentTransactionType);

        CreditCardDTO creditCardDTO = requestDTO.getCreditCard();
        String transactionAmount = requestDTO.getTransactionTotal();
        String paymentToken = (String) requestDTO.getAdditionalFields().get(PaymentAdditionalFieldType.TOKEN.getType());

        CreditCardValidator visaValidator = new CreditCardValidator(CreditCardValidator.VISA);
        CreditCardValidator amexValidator = new CreditCardValidator(CreditCardValidator.AMEX);
        CreditCardValidator mcValidator = new CreditCardValidator(CreditCardValidator.MASTERCARD);
        CreditCardValidator discoverValidator = new CreditCardValidator(CreditCardValidator.DISCOVER);

        if (StringUtils.isNotBlank(transactionAmount) && StringUtils.isNotBlank(paymentToken)) {
            // auto assume that if a token is passed in it is valid and will mock a success.
            responseDTO.amount(new Money(requestDTO.getTransactionTotal()))
                    .rawResponse("Success!")
                    .successful(true);
        } else if (StringUtils.isNotBlank(transactionAmount) &&
                creditCardDTO != null &&
                StringUtils.isNotBlank(creditCardDTO.getCreditCardNum()) &&
                (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpDate()) ||
                        (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpMonth()) &&
                                StringUtils.isNotBlank(creditCardDTO.getCreditCardExpYear())))) {

            boolean validCard = false;
            if (visaValidator.isValid(creditCardDTO.getCreditCardNum())){
                validCard = true;
            } else if (amexValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            } else if (mcValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            } else if (discoverValidator.isValid(creditCardDTO.getCreditCardNum())) {
                validCard = true;
            }

            boolean validDateFormat = false;
            boolean validDate = false;
            String[] parsedDate = null;
            if (StringUtils.isNotBlank(creditCardDTO.getCreditCardExpDate())) {
                parsedDate = creditCardDTO.getCreditCardExpDate().split("/");
            } else {
                parsedDate = new String[2];
                parsedDate[0] = creditCardDTO.getCreditCardExpMonth();
                parsedDate[1] = creditCardDTO.getCreditCardExpYear();
            }

            if (parsedDate.length == 2) {
                String expMonth = parsedDate[0];
                String expYear = parsedDate[1];
                try {
                    DateTime expirationDate = new DateTime(Integer.parseInt("20"+expYear), Integer.parseInt(expMonth), 1, 0, 0);
                    expirationDate = expirationDate.dayOfMonth().withMaximumValue();
                    validDate = expirationDate.isAfterNow();
                    validDateFormat = true;
                } catch (Exception e) {
                    //invalid date format
                }
            }

            if (!validDate || !validDateFormat) {
                responseDTO.amount(new Money(0))
                        .rawResponse("cart.payment.expiration.invalid")
                        .successful(false);
            } else if (!validCard) {
                responseDTO.amount(new Money(0))
                        .rawResponse("cart.payment.card.invalid")
                        .successful(false);
            } else {
                populateResponseDTO(responseDTO, requestDTO);

                responseDTO.amount(new Money(requestDTO.getTransactionTotal()))
                        .rawResponse("Success!")
                        .successful(true);
            }

        } else {
            responseDTO.amount(new Money(0))
                    .rawResponse("cart.payment.invalid")
                    .successful(false);
        }

        return responseDTO;
    }

    protected void setupNoncePaymentRequest(PaymentRequestDTO requestDTO) {
        String nonce = (String) requestDTO.getAdditionalFields().get(SamplePaymentGatewayConstants.PAYMENT_METHOD_NONCE);
        if (nonce != null) {
            String[] fields = nonce.split("\\|");

            String lastFour = (fields[0] == null) ? null : fields[0].substring(fields[0].length() - 4);

            requestDTO.creditCard()
                    .creditCardType(CreditCardType.MASTERCARD.getType())
                    .creditCardNum(fields[0])
                    .creditCardLastFour(lastFour)
                    .creditCardHolderName(fields[1])
                    .creditCardExpDate(fields[2])
                    .creditCardCvv(fields[3]);
        }
    }

    protected void populateResponseDTO(PaymentResponseDTO responseDTO, PaymentRequestDTO requestDTO) {
        Map<String, String> additionalResponseItems = new HashMap<>();

        CreditCardDTO<PaymentRequestDTO> creditCardDTO = requestDTO.getCreditCard();
        additionalResponseItems.put(PaymentAdditionalFieldType.CARD_TYPE.getType(), creditCardDTO.getCreditCardType());
        additionalResponseItems.put(PaymentAdditionalFieldType.NAME_ON_CARD.getType(), creditCardDTO.getCreditCardHolderName());
        additionalResponseItems.put(PaymentAdditionalFieldType.LAST_FOUR.getType(), creditCardDTO.getCreditCardLastFour());
        additionalResponseItems.put(PaymentAdditionalFieldType.EXP_DATE.getType(), creditCardDTO.getCreditCardExpDate());

        responseDTO.getResponseMap().putAll(additionalResponseItems);
    }

    @Override
    public ServiceStatusType getServiceStatus() {
        if (isUp) {
            return ServiceStatusType.UP;
        } else {
            return ServiceStatusType.DOWN;
        }
    }

    @Override
    public synchronized void clearStatus() {
        isUp = true;
        failureCount = 0;
    }

    /**
     * arbitrarily set a failure threshold value of "3"
     */
    @Override
    public synchronized void incrementFailure() {
        if (failureCount >= getFailureReportingThreshold()) {
            isUp = false;
        } else {
            failureCount++;
        }
    }

    @Override
    public Integer getFailureReportingThreshold() {
        return 3;
    }

}
