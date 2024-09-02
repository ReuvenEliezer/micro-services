package com.nice.services;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.nice.services.CustomTracingFilter.TracingFilterConstants.TRACING_FILTER_ORDER;


@Component
@Order(TRACING_FILTER_ORDER)
public class CustomTracingFilter extends GenericFilterBean {

    private static final Logger logger = LogManager.getLogger(CustomTracingFilter.class);

    private final Tracer tracer;

    CustomTracingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }
        TraceContext context = currentSpan.context();
        logger.info("traceId: {}, spanId: {}", context.traceIdString(), context.spanIdString());
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("SpanId", context.traceIdString());
        res.addHeader("TraceId", context.spanIdString());
        chain.doFilter(request, res);
    }

    static final class TracingFilterConstants {
        public static final int TRACING_FILTER_ORDER = -2147483642;
    }
}

