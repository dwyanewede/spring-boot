package com.sxs.web.http.converter.properties;

import com.alibaba.fastjson.JSON;
import com.sxs.web.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * {@link User} {@link HttpMessageConverter} 实现
 *
 * @author sxs
 * @since 2019/1/28
 */
public class UserHttpMessageConverter extends AbstractGenericHttpMessageConverter<User> {

    public UserHttpMessageConverter() {
        // 设置支持的 MediaType
        super(new MediaType("text", "user"));
    }

    @Override
    protected void writeInternal(User user, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // User -> String
        // OutputStream -> Writer
        OutputStream outputStream = outputMessage.getBody();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(user);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        outputStream.write(bytes);
        byte[] bytes = JSON.toJSONString(user).getBytes();
        outputStream.write(bytes);
    }

    @Override
    protected User readInternal(Class<? extends User> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        // 字符流 -> 字符编码
        // 从 请求头 Content-Type 解析编码
        HttpHeaders httpHeaders = inputMessage.getHeaders();
        MediaType mediaType = httpHeaders.getContentType();
        // 获取字符编码
        Charset charset = mediaType.getCharset();
        // 当 charset 不存在时，使用 UTF-8
        charset = charset == null ? Charset.forName("UTF-8") : charset;

        // 字节流
        InputStream inputStream = inputMessage.getBody();
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        User user = new User();
        // 加载字符流成为 Properties 对象
        Properties properties = new Properties();
        properties.load(reader);
        user.setId(Long.valueOf(properties.getProperty("id")));
        user.setName(String.valueOf(properties.getProperty("name")));
        return user;
    }

    @Override
    public User read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(null, inputMessage);
    }
}
