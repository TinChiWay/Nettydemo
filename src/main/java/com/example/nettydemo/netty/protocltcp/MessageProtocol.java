package com.example.nettydemo.netty.protocltcp;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageProtocol {
    private int len;
    private byte[] content;



}
