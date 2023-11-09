package com.example.nettydemo.gupaoedu.rpc.registry;

import com.example.nettydemo.gupaoedu.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用保存所有可用的服务
     */
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();

    /**
     * 保存所有相关的服务类
     */
    private List<String> classNames = new ArrayList<>();

    public RegistryHandler() {
        // 完成递归扫描
        scannerClass("com.example.nettydemo.gupaoedu.rpc.provider");
        doRegister();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokerProtocol request = (InvokerProtocol) msg;
        // 当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        // 使用反射调用
        if (registryMap.containsKey(request.getClassName())) {
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName());

            result = method.invoke(clazz, request.getValues());
        }

        ctx.writeAndFlush(result);
        ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error in handler", cause);
        ctx.close();
    }

    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scannerClass(packageName + "," + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }

    }

    private void doRegister() {
        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }

}
