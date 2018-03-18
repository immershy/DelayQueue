package com.zw.self;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zhengwei on 2018/3/17.
 */
@Getter
@Setter
@NoArgsConstructor
public class DelayMessage {
    /**
     * 消息编号
     */
    private String msgId;

    /**
     * 消息体
     */
    private String body;

    /**
     * 延迟时间
     */
    private int delayMs;

    public DelayMessage(String msg) {
        this.msgId = getMsgId(msg);
        this.body = getMsgBody(msg);
        this.delayMs = getMsgDelay(msg);
    }

    @Override
    public String toString() {
        return msgId + "," + delayMs + "," + body;
    }

    public String getMsgId(String msg) {
        return msg == null ? null : msg.split(",")[0];
    }


    public int getMsgDelay(String msg) {
        return msg == null ? 0 : Integer.valueOf(msg.split(",")[1]);
    }

    public String getMsgBody(String msg) {
        return msg == null ? null : msg.split(",")[2];
    }
}
