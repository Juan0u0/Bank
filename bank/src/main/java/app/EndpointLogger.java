package app;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndpointLogger extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(EndpointLogger.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String timestamp = LocalDateTime.now().format(FORMATTER);

        // Log incoming request
        String fullUri = queryString != null ? uri + "?" + queryString : uri;
        logger.info("[{}] {} {} - INICIADO", timestamp, method, fullUri);

        try {
            // Continue with the request
            filterChain.doFilter(request, response);
        } finally {
            // Log completed request with execution time
            long executionTime = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String statusMessage = getStatusMessage(status);

            logger.info("[{}] {} {} - {} - {} ms ({})",
                    timestamp,
                    method,
                    fullUri,
                    status,
                    executionTime,
                    statusMessage);
        }
    }

    private String getStatusMessage(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "CREATED";
            case 204 -> "NO CONTENT";
            case 400 -> "BAD REQUEST";
            case 401 -> "UNAUTHORIZED";
            case 403 -> "FORBIDDEN";
            case 404 -> "NOT FOUND";
            case 409 -> "CONFLICT";
            case 500 -> "INTERNAL SERVER ERROR";
            case 503 -> "SERVICE UNAVAILABLE";
            default -> "UNKNOWN";
        };
    }
}
