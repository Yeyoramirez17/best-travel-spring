package com.apptravel.apitravel.infraestructure.helpers;

import com.apptravel.apitravel.util.exceptions.ForbiddenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {

    public void isInBlackListCustomer(String customerId) {
        if (customerId.equals("VIKI771012HMCRG093")) {
            throw new ForbiddenCustomerException();
        }
    }
}
