package com.kakao.actionbase.core.java.metadata.v3.common;

import static org.junit.jupiter.api.Assertions.*;

import com.kakao.actionbase.core.java.codec.common.hbase.Order;
import com.kakao.actionbase.core.java.jackson.ActionbaseObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("IndexField Domain Tests")
class IndexFieldTest {

  private static final String TEST_FIELD = "test_field";
  private static final Order TEST_ORDER = Order.ASC;

  @Nested
  @DisplayName("Construction")
  class ConstructionTests {

    @Test
    @DisplayName("Can create IndexField with field and order")
    void testConstruction() {
      // Create using factory method
      IndexField indexField = ImmutableIndexField.of(TEST_FIELD, TEST_ORDER);

      // Verify field values
      assertEquals(TEST_FIELD, indexField.field());
      assertEquals(TEST_ORDER, indexField.order());
    }
  }

  @Nested
  @DisplayName("Data Conversion")
  class DataConversionTests {

    @Test
    @DisplayName("JSON serialization/deserialization")
    void testJsonSerialization() {
      // Create original IndexField
      IndexField original = ImmutableIndexField.of(TEST_FIELD, TEST_ORDER);

      // Convert to JSON
      String json = ActionbaseObjectMapper.toJson(original);

      // Convert back from JSON
      IndexField fromJson = ActionbaseObjectMapper.fromJson(json, IndexField.class);

      // Verify field values
      assertEquals(original.field(), fromJson.field());
      assertEquals(original.order(), fromJson.order());
    }
  }

  @Nested
  @DisplayName("Domain Rules")
  class DomainRuleTests {

    @Test
    @DisplayName("IndexField contains field and order")
    void testDomainRules() {
      // Create IndexField
      IndexField indexField = ImmutableIndexField.of(TEST_FIELD, TEST_ORDER);

      // Verify required fields
      assertNotNull(indexField.field());
      assertNotNull(indexField.order());
    }
  }
}
