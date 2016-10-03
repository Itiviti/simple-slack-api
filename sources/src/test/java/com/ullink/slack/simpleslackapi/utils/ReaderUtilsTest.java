package com.ullink.slack.simpleslackapi.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReaderUtilsTest {
    
    @Test
    public void readAll() throws Exception {
        // Given
        final String example = "this is a test!";
        final InputStream inputStream = new ByteArrayInputStream(example.getBytes(Charset.forName("UTF-8")));

        // When
        final String readResult = ReaderUtils.readAll(new InputStreamReader(inputStream));

        // Then
        assertThat(readResult, is(equalTo(example)));
    }

}