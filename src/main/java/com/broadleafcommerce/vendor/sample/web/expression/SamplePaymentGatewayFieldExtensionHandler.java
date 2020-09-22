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

package org.broadleafcommerce.vendor.sample.web.expression;

import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.web.payment.expression.AbstractPaymentGatewayFieldExtensionHandler;
import org.broadleafcommerce.common.web.payment.expression.PaymentGatewayFieldExtensionManager;
import org.broadleafcommerce.vendor.sample.service.payment.SamplePaymentGatewayConstants;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This sample handler will add itself to the {@link PaymentGatewayFieldExtensionManager}
 * and will output the input NAMEs that are required by the
 * {@link org.broadleafcommerce.vendor.sample.web.controller.mock.processor.SampleMockProcessorController}
 *
 * Note, we don't want this loaded into the extension manager
 * if a real payment gateway is used, so make sure to not scan this class when
 * using a real implementation.
 *
 * In order to use this sample extension handler, you will need to component scan
 * the package "com.broadleafcommerce".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blSamplePaymentGatewayFieldExtensionHandler")
public class SamplePaymentGatewayFieldExtensionHandler extends AbstractPaymentGatewayFieldExtensionHandler {

    @Resource(name = "blPaymentGatewayFieldExtensionManager")
    protected PaymentGatewayFieldExtensionManager extensionManager;

    @Resource(name = "blSamplePaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration paymentGatewayConfiguration;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getCreditCardHolderName() {
        return SamplePaymentGatewayConstants.CREDIT_CARD_NAME;
    }

    @Override
    public String getCreditCardType() {
        return null;
    }

    @Override
    public String getCreditCardNum() {
        return SamplePaymentGatewayConstants.CREDIT_CARD_NUMBER;
    }

    @Override
    public String getCreditCardExpDate() {
        return SamplePaymentGatewayConstants.CREDIT_CARD_EXP_DATE;
    }

    @Override
    public String getCreditCardExpMonth() {
        return null;
    }

    @Override
    public String getCreditCardExpYear() {
        return null;
    }

    @Override
    public String getCreditCardCvv() {
        return SamplePaymentGatewayConstants.CREDIT_CARD_CVV;
    }

    @Override
    public String getBillToAddressFirstName() {
        return SamplePaymentGatewayConstants.BILLING_FIRST_NAME;
    }

    @Override
    public String getBillToAddressLastName() {
        return SamplePaymentGatewayConstants.BILLING_LAST_NAME;
    }

    @Override
    public String getBillToAddressCompanyName() {
        return SamplePaymentGatewayConstants.BILLING_COMPANY_NAME;
    }

    @Override
    public String getBillToAddressLine1() {
        return SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE1;
    }

    @Override
    public String getBillToAddressLine2() {
        return SamplePaymentGatewayConstants.BILLING_ADDRESS_LINE2;
    }

    @Override
    public String getBillToAddressCityLocality() {
        return SamplePaymentGatewayConstants.BILLING_CITY;
    }

    @Override
    public String getBillToAddressStateRegion() {
        return SamplePaymentGatewayConstants.BILLING_STATE;
    }

    @Override
    public String getBillToAddressPostalCode() {
        return SamplePaymentGatewayConstants.BILLING_ZIP;
    }

    @Override
    public String getBillToAddressCountryCode() {
        return SamplePaymentGatewayConstants.BILLING_COUNTRY;
    }

    @Override
    public String getBillToAddressPhone() {
        return SamplePaymentGatewayConstants.BILLING_PHONE;
    }

    @Override
    public String getBillToAddressEmail() {
        return SamplePaymentGatewayConstants.BILLING_EMAIL;
    }

    @Override
    public String getShipToAddressFirstName() {
        return null;
    }

    @Override
    public String getShipToAddressLastName() {
        return null;
    }

    @Override
    public String getShipToAddressCompanyName() {
        return null;
    }

    @Override
    public String getShipToAddressLine1() {
        return null;
    }

    @Override
    public String getShipToAddressLine2() {
        return null;
    }

    @Override
    public String getShipToAddressCityLocality() {
        return null;
    }

    @Override
    public String getShipToAddressStateRegion() {
        return null;
    }

    @Override
    public String getShipToAddressPostalCode() {
        return null;
    }

    @Override
    public String getShipToAddressCountryCode() {
        return null;
    }

    @Override
    public String getShipToAddressPhone() {
        return null;
    }

    @Override
    public String getShipToAddressEmail() {
        return null;
    }

    @Override
    public PaymentGatewayType getHandlerType() {
        return paymentGatewayConfiguration.getGatewayType();
    }
}
