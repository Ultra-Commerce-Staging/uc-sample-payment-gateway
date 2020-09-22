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

package com.ultracommerce.vendor.sample.service.payment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chris Kittrell (ckittrell)
 */
public class PaymentTransactionType extends com.ultracommerce.common.payment.PaymentTransactionType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PaymentTransactionType> TYPES = new LinkedHashMap<>();

    public static final PaymentTransactionType CREATE_CUSTOMER = new PaymentTransactionType("CREATE_CUSTOMER", "Create Customer");
    public static final PaymentTransactionType UPDATE_CUSTOMER = new PaymentTransactionType("UPDATE_CUSTOMER", "Update Customer");
    public static final PaymentTransactionType DELETE_CUSTOMER = new PaymentTransactionType("DELETE_CUSTOMER", "Delete Customer");

    public static PaymentTransactionType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PaymentTransactionType() {
        //do nothing
    }

    public PaymentTransactionType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        PaymentTransactionType other = (PaymentTransactionType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
