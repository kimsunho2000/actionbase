package com.kakao.actionbase.core.java.information;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableExtraInformation.class)
@JsonDeserialize(as = ImmutableExtraInformation.class)
public interface ExtraInformation {

  V2CompatInformation v2();
}
