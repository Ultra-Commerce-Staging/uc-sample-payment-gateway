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

import com.ultracommerce.common.payment.service.PaymentGatewayConfiguration;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public interface SamplePaymentGatewayConfiguration extends PaymentGatewayConfiguration {

    public String getTransparentRedirectUrl();

    public String getTransparentRedirectReturnUrl();
    
    public String getCustomerPaymentTransparentRedirectUrl();
    
    public String getCustomerPaymentTransparentRedirectReturnUrl();

}
