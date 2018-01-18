package com.ullink.slack.simpleslackapi.impl;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/**
 * Custom implementation of the {@link ServiceUnavailableRetryStrategy} interface
 * that retries {@code 429} (Too Many Requests) responses for a fixed number of times
 * at a default or the 'Retry-After' header configured interval.
 */
@Contract
public class SlackRateLimitRetryStrategy implements ServiceUnavailableRetryStrategy {

    /**
     * HTTP status code for Too Many Requests response.
     */
    private static final int SC_TOO_MANY_REQUESTS = 429;

    /**
     * Default HTTP request retry interval, in milliseconds.
     */
    private static final long DEFAULT_RETRY_INTERVAL = 3000L;

    /**
     * Default maximum number of retries.
     */
    private static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * Maximum number of allowed retries if the server responds with a HTTP code
     * in our retry code list. Default value is 3.
     */
    private final int maxRetries;

    /**
     * Retry interval between subsequent requests, in milliseconds.
     */
    private long retryInterval;

    public SlackRateLimitRetryStrategy() {
        this(DEFAULT_MAX_RETRIES, DEFAULT_RETRY_INTERVAL);
    }

    public SlackRateLimitRetryStrategy(final int maxRetries, final long retryInterval) {
        this.maxRetries = Args.positive(maxRetries, "Max retries");
        this.retryInterval = Args.positive(retryInterval, "Retry interval");
    }

    @Override
    public boolean retryRequest(final HttpResponse response, final int executionCount, final HttpContext context) {

        if (executionCount > maxRetries || response.getStatusLine().getStatusCode() != SC_TOO_MANY_REQUESTS) {
            return false;
        }

        retryInterval = DEFAULT_RETRY_INTERVAL;
        try {
            final Header header = response.getFirstHeader(HttpHeaders.RETRY_AFTER);
            if (header != null) {
                retryInterval = Integer.parseInt(header.getValue()) * 1000L;
            }
        } catch (NumberFormatException ignore) {
        }

        return true;
    }

    @Override
    public long getRetryInterval() {
        return retryInterval;
    }
}
