package com.kakao.actionbase.core.java.metadata.v2;

import com.kakao.actionbase.core.java.metadata.v2.common.Index;
import com.kakao.actionbase.core.java.metadata.v2.common.LabelType;
import com.kakao.actionbase.core.java.metadata.v2.common.MutationMode;
import com.kakao.actionbase.core.java.metadata.v2.common.Schema;
import com.kakao.actionbase.core.java.metadata.v3.common.DirectionType;
import com.kakao.actionbase.core.metadata.common.Group;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableLabelCreatePayload.class)
@JsonDeserialize(as = ImmutableLabelCreatePayload.class)
public interface LabelCreatePayload {

  String desc();

  LabelType type();

  Schema schema();

  DirectionType dirType();

  String storage();

  List<Group> groups();

  List<Index> indices();

  boolean event();

  boolean readOnly();

  MutationMode mode();
}
