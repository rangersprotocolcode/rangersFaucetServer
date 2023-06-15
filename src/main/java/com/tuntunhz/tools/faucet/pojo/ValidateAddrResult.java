package com.tuntunhz.tools.faucet.pojo;

public class ValidateAddrResult {

    private boolean valid;

    private String reason;

    private String troubleAddress;


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTroubleAddress() {
        return troubleAddress;
    }

    public void setTroubleAddress(String troubleAddress) {
        this.troubleAddress = troubleAddress;
    }


    public String getReturnMessage() {
        return this.reason + ":" + this.troubleAddress;
    }
}
