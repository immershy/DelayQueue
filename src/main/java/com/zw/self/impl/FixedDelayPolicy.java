package com.zw.self.impl;

import com.zw.self.DelayPolicy;

/**
 * @author zhengwei
 * @date 2018/3/17
 */
public class FixedDelayPolicy implements DelayPolicy {
    @Override
    public int nextDelayTime(int delayTime) {
        return delayTime;
    }
}
