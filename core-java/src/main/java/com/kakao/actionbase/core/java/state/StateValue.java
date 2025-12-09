package com.kakao.actionbase.core.java.state;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.types.TypeValidator;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableStateValue.class)
@JsonDeserialize(as = ImmutableStateValue.class)
public interface StateValue {

  @Value.Parameter
  long version();

  @Value.Parameter
  @Nullable
  Object value();

  @Value.Check
  default void check() {
    if (value() != null) {
      TypeValidator.validateValue(value());
    }
  }

  @JsonIgnore
  default boolean isDeleted() {
    return SpecialStateValue.DELETED.code().equals(value());
  }

  @JsonIgnore
  default boolean isUnset() {
    return SpecialStateValue.UNSET.code().equals(value());
  }

  @JsonIgnore
  default boolean isPresent() {
    return value() != null && !isDeleted() && !isUnset();
  }

  static StateValue unsetOf(long version) {
    return ImmutableStateValue.builder()
        .version(version)
        .value(SpecialStateValue.UNSET.code())
        .build();
  }
}
