package com.kakao.actionbase.core.java.information;

import com.kakao.actionbase.core.java.metadata.v3.DatastoreDescriptor;
import com.kakao.actionbase.core.java.metadata.v3.TenantDescriptor;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTenantInformation.class)
@JsonDeserialize(as = ImmutableTenantInformation.class)
public interface TenantInformation {

  TenantDescriptor tenant();

  List<DatastoreDescriptor> datastores();

  PipelineInformation pipeline();

  ExtraInformation extra();
}
