package org.decembrist.spring.interfacebean;

import org.springframework.beans.BeansException;

public class InterfaceBeanException extends BeansException {
    public InterfaceBeanException(String msg) {
        super(msg);
    }
}
