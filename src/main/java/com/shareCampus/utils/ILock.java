package com.shareCampus.utils;

public interface ILock {
    boolean tryLock(long timeoutSec);

    void unlock();
}
