package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GsonHelperTest {

    @Test
    public void castsStringWithFloatAsLong() {

    	JsonElement jsonElement = new JsonPrimitive("1554055579.006800");

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(jsonElement, 0L);

        assertThat(longOrDefaultValue).isEqualTo(1554055579L);

    }

    @Test
    public void castsStringWithIntegerAsLong() {

    	JsonElement jsonElement = new JsonPrimitive("1554055579");

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(jsonElement, 0L);

        assertThat(longOrDefaultValue).isEqualTo(1554055579L);

    }

    @Test(expected = NumberFormatException.class)
    public void failsOnInvalidString() {

    	JsonElement jsonElement = new JsonPrimitive("hello");

        GsonHelper.getLongOrDefaultValue(jsonElement, 815L);

    }

    @Test
    public void usesDefaultOnEmptyString() {

    	JsonElement jsonElement = new JsonPrimitive("");

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(jsonElement, 815L);

        assertThat(longOrDefaultValue).isEqualTo(815L);

    }


    @Test
    public void castsFloatToLong() {

    	JsonElement jsonElement = new JsonPrimitive(47.11);

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(jsonElement, 0L);

        assertThat(longOrDefaultValue).isEqualTo(47L);

    }

    @Test
    public void usesDefaultValueOnJsonNull() {

    	JsonElement jsonElement = JsonNull.INSTANCE;

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(jsonElement, 123L);

        assertThat(longOrDefaultValue).isEqualTo(123L);

    }

    @Test
    public void takeDefaultValueIfNoJsonElementGiven() {

        final Long longOrDefaultValue = GsonHelper.getLongOrDefaultValue(null, 123456789L);

        assertThat(longOrDefaultValue).isEqualTo(123456789L);

    }


}
