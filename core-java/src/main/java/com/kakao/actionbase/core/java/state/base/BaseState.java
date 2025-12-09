package com.kakao.actionbase.core.java.state.base;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.SpecialStateValue;
import com.kakao.actionbase.core.java.state.State;
import com.kakao.actionbase.core.java.state.StateValue;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableBaseState.class)
@JsonDeserialize(as = ImmutableBaseState.class)
public interface BaseState extends State {

  @Override
  boolean active();

  @Override
  @Value.Default
  default long version() {
    return Long.MIN_VALUE;
  }

  @Override
  Map<String, StateValue> properties();

  @Override
  @Nullable
  Long createdAt();

  @Override
  @Nullable
  Long deletedAt();

  abstract class Builder {
    // TODO: avoid using this method directly, use checkAndBuild instead
    abstract BaseState build();

    public BaseState checkAndBuild(StructType schema) {
      return checkPropertiesThenReturn(build(), schema);
    }

    private BaseState checkPropertiesThenReturn(BaseState state, StructType schema) {
      // Validate active state
      boolean shouldBeActive = false;
      if (state.createdAt() != null) {
        if (state.deletedAt() == null) {
          shouldBeActive = true;
        } else {
          shouldBeActive = state.createdAt() > state.deletedAt();
        }
      }

      // Field validation
      if (state.active()) {
        if (state.createdAt() == null) {
          throw new IllegalArgumentException("createdAt must be provided when active is true");
        }

        for (String fieldName : schema.fieldNames()) {
          StateValue entry = state.properties().get(fieldName);
          boolean isInvalid = entry == null || entry.value() == null;

          // Added SpecialStateValue validation
          if (!isInvalid && entry.value() instanceof String) {
            String strValue = (String) entry.value();
            if (SpecialStateValue.isSpecialStateValue(strValue)) {
              isInvalid = true;
            }
          }

          if (isInvalid && !schema.getField(fieldName).nullable()) {
            throw new IllegalArgumentException(fieldName + " must be provided");
          }
        }

        // Verify that active state matches shouldBeActive
        if (state.active() != shouldBeActive) {
          throw new IllegalArgumentException(
              "active state is inconsistent with timestamps: active="
                  + state.active()
                  + ", createdAt="
                  + state.createdAt()
                  + ", deletedAt="
                  + state.deletedAt());
        }
      }
      return state;
    }
  }
}
