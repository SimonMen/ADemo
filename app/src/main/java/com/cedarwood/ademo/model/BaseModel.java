package com.cedarwood.ademo.model;

import java.io.Serializable;


public class BaseModel implements Serializable {



    /**
	 * 
	 */
    private static final long serialVersionUID = -3177239267199493926L;

    private int errorCode = 0;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


}
