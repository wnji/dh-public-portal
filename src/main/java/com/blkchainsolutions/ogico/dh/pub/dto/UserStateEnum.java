package com.blkchainsolutions.ogico.dh.pub.dto;


public enum UserStateEnum {
    //0 pending 1 approved 2 disapproved 3 disabled

    PENDING_STATE(0), APPROVED_STATE(1),DISAPPROVED_STATE(2),DISABLED_STATE(3),LOGIN_FAILED_COUNT(2);
    private int state;
    UserStateEnum(int i) {
        this.state=i;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
