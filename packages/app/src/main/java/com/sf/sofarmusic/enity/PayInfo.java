package com.sf.sofarmusic.enity;

import java.io.Serializable;

/**
 * Created by sufan on 17/6/16.
 * 支付方式相关信息
 */

public class PayInfo implements Serializable {

    public String bankName;
    public String bankType;
    public String bankCard;
    public boolean isSelected;
}
