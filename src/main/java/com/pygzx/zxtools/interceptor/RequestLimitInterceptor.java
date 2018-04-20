package com.pygzx.zxtools.interceptor;

import com.pygzx.zxtools.annotation.RequestLimit;
import com.pygzx.zxtools.util.IOUtil;
import com.quickutil.platform.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class RequestLimitInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLimitInterceptor.class);

	private String prefix;
	private StringRedisTemplate redisTemplate;

	public RequestLimitInterceptor(String source, StringRedisTemplate redisTemplate) {
		if (StringUtils.isEmpty(source)) {
			throw new NullPointerException("Source can not be null");
		}
		if (redisTemplate == null) {
			throw new NullPointerException("RedisTemplate can not be null");
		}
		this.prefix = source + ":request:limit";
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		if (o instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) o;
			RequestLimit annotation = handlerMethod.getMethodAnnotation(RequestLimit.class);
			if (annotation != null) {
				int limit = annotation.value();
				int interval = annotation.interval();

				String ip = ContextUtil.getIp();
				String path = request.getRequestURI();
				String key = String.join(":", this.prefix, ip, path);
				long count = redisTemplate.opsForValue().increment(key, 1);
				if (count == 1) {
					redisTemplate.expire(key, interval, TimeUnit.SECONDS);
				}
				LOGGER.info("limit={}, interval={}, key={}, count={}", limit, interval, key, count);
				if (count > limit) {
					response.setIntHeader("Retry-After", interval);
					response.setStatus(429);
					response.setContentType(MediaType.TEXT_HTML_VALUE);
					ServletOutputStream out = null;
					try {
						out = response.getOutputStream();
						out.write(String.format("<html><head><title>Too Many Requests</title></head><body> <h1>Too Many Requests</h1> <p>I only allow %d requests per %d second to this Web site per logged in user. Try again soon.</p></body></html>", limit, interval).getBytes(StandardCharsets.UTF_8));
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						IOUtil.closeQuietly(out);
					}
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
