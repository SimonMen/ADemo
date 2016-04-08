package com.cedarwood.ademo.model;


public class BaseResponse extends BaseModel {



    /**
	 * 
	 */
    private static final long serialVersionUID = -4287356049599724848L;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }



}
