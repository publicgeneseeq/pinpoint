package com.nhn.pinpoint.profiler.modifier.redis.interceptor;

import java.util.Map;

import com.nhn.pinpoint.bootstrap.context.RecordableTrace;
import com.nhn.pinpoint.bootstrap.interceptor.SpanEventSimpleAroundInterceptor;
import com.nhn.pinpoint.bootstrap.interceptor.TargetClassLoader;
import com.nhn.pinpoint.bootstrap.interceptor.tracevalue.MapTraceValue;
import com.nhn.pinpoint.common.ServiceType;

/**
 * Jedis (redis client) method interceptor
 * 
 * @author jaehong.kim
 *
 */
public class JedisMethodInterceptor extends SpanEventSimpleAroundInterceptor implements TargetClassLoader {

    public JedisMethodInterceptor() {
        super(JedisMethodInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args) {
        trace.markBeforeTime();
    }

    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        String endPoint = null;
        if (target instanceof MapTraceValue) {
            final Map<String, Object> traceValue = ((MapTraceValue) target).__getTraceBindValue();
            if (traceValue != null) {
                endPoint = (String) traceValue.get("endPoint");
            }
        }

        trace.recordApi(getMethodDescriptor());
        trace.recordEndPoint(endPoint != null ? endPoint : "Unknown");
        trace.recordDestinationId(ServiceType.REDIS.toString());
        trace.recordServiceType(ServiceType.REDIS);
        trace.recordException(throwable);
        trace.markAfterTime();
    }
}