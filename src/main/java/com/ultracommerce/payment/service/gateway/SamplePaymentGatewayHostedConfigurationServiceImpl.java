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
/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ultracommerce.payment.service.gateway;

import com.ultracommerce.common.payment.service.AbstractPaymentGatewayConfigurationService;
import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;
import com.ultracommerce.common.payment.service.PaymentGatewayConfigurationService;
import com.ultracommerce.common.payment.service.PaymentGatewayCreditCardService;
import com.ultracommerce.common.payment.service.PaymentGatewayCustomerService;
import com.ultracommerce.common.payment.service.PaymentGatewayFraudService;
import com.ultracommerce.common.payment.service.PaymentGatewayHostedService;
import com.ultracommerce.common.payment.service.PaymentGatewayReportingService;
import com.ultracommerce.common.payment.service.PaymentGatewayRollbackService;
import com.ultracommerce.common.payment.service.PaymentGatewaySubscriptionService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionConfirmationService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransactionService;
import com.ultracommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import com.ultracommerce.common.payment.service.PaymentGatewayWebResponseService;
import com.ultracommerce.common.web.payment.expression.PaymentGatewayFieldExtensionHandler;
import com.ultracommerce.common.web.payment.processor.CreditCardTypesExtensionHandler;
import com.ultracommerce.common.web.payment.processor.TRCreditCardExtensionHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Service("ucSamplePaymentGatewayHostedConfigurationService")
public class SamplePaymentGatewayHostedConfigurationServiceImpl extends AbstractPaymentGatewayConfigurationService {

    @Resource(name = "ucSamplePaymentGatewayHostedConfiguration")
    protected SamplePaymentGatewayHostedConfiguration configuration;

    @Resource(name = "ucSamplePaymentGatewayHostedRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "ucSamplePaymentGatewayHostedService")
    protected PaymentGatewayHostedService hostedService;

    @Resource(name = "ucSamplePaymentGatewayHostedTransactionConfirmationService")
    protected PaymentGatewayTransactionConfirmationService transactionConfirmationService;

    @Resource(name = "ucSamplePaymentGatewayHostedWebResponseService")
    protected PaymentGatewayWebResponseService webResponseService;

    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    public PaymentGatewayTransactionService getTransactionService() {
        return null;
    }

    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return transactionConfirmationService;
    }

    public PaymentGatewayReportingService getReportingService() {
        return null;
    }

    public PaymentGatewayCreditCardService getCreditCardService() {
        return null;
    }

    public PaymentGatewayCustomerService getCustomerService() {
        return null;
    }

    public PaymentGatewaySubscriptionService getSubscriptionService() {
        return null;
    }

    public PaymentGatewayFraudService getFraudService() {
        return null;
    }

    public PaymentGatewayHostedService getHostedService() {
        return hostedService;
    }

    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }

    public PaymentGatewayWebResponseService getWebResponseService() {
        return webResponseService;
    }

    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return null;
    }

    public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
        return null;
    }

    public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
        return null;
    }

    public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
        return null;
    }

}
