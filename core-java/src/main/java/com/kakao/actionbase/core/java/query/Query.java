package com.kakao.actionbase.core.java.query;

import com.kakao.actionbase.core.java.jackson.ActionbaseObjectMapper;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableQuery.class)
@JsonDeserialize(as = ImmutableQuery.class)
public interface Query {

  List<QueryStep> steps();

  static ImmutableQuery.Builder builder() {
    return ImmutableQuery.builder();
  }

  default String toJson() throws JsonProcessingException {
    return ActionbaseObjectMapper.INSTANCE.writeValueAsString(this);
  }

  static Query fromJson(String json) throws JsonProcessingException {
    return ActionbaseObjectMapper.INSTANCE.readValue(json, Query.class);
  }
}
