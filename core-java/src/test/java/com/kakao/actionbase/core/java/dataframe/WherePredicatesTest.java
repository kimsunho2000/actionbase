package com.kakao.actionbase.core.java.dataframe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("WherePredicates Test")
class WherePredicatesTest {

  @Nested
  @DisplayName("parse/toString test by predicate type")
  class PredicateTypeTest {

    @Test
    @DisplayName("parse and toString test for eq condition")
    void testEqPredicate() {
      // Given
      String originalFilter = "status:eq:active";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.Eq.class, predicate, "Parsed condition should be Eq type");
      WherePredicate.Eq eqPredicate = (WherePredicate.Eq) predicate;
      assertEquals("status", eqPredicate.key(), "key value should be parsed correctly");
      assertEquals("active", eqPredicate.value(), "value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for in condition")
    void testInPredicate() {
      // Given
      String originalFilter = "category:in:food,beverage,snack";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.In.class, predicate, "Parsed condition should be In type");
      WherePredicate.In inPredicate = (WherePredicate.In) predicate;
      assertEquals("category", inPredicate.key(), "key value should be parsed correctly");
      assertEquals(3, inPredicate.values().size(), "values size should be parsed correctly");
      assertTrue(
          inPredicate.values().containsAll(Arrays.asList("food", "beverage", "snack")),
          "values content should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for gt condition")
    void testGtPredicate() {
      // Given
      String originalFilter = "price:gt:1000";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.Gt.class, predicate, "Parsed condition should be Gt type");
      WherePredicate.Gt gtPredicate = (WherePredicate.Gt) predicate;
      assertEquals("price", gtPredicate.key(), "key value should be parsed correctly");
      assertEquals("1000", gtPredicate.value(), "value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for gte condition")
    void testGtePredicate() {
      // Given
      String originalFilter = "age:gte:18";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.Gte.class, predicate, "Parsed condition should be Gte type");
      WherePredicate.Gte gtePredicate = (WherePredicate.Gte) predicate;
      assertEquals("age", gtePredicate.key(), "key value should be parsed correctly");
      assertEquals("18", gtePredicate.value(), "value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for lt condition")
    void testLtPredicate() {
      // Given
      String originalFilter = "temperature:lt:30";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.Lt.class, predicate, "Parsed condition should be Lt type");
      WherePredicate.Lt ltPredicate = (WherePredicate.Lt) predicate;
      assertEquals("temperature", ltPredicate.key(), "key value should be parsed correctly");
      assertEquals("30", ltPredicate.value(), "value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for lte condition")
    void testLtePredicate() {
      // Given
      String originalFilter = "weight:lte:100";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(WherePredicate.Lte.class, predicate, "Parsed condition should be Lte type");
      WherePredicate.Lte ltePredicate = (WherePredicate.Lte) predicate;
      assertEquals("weight", ltePredicate.key(), "key value should be parsed correctly");
      assertEquals("100", ltePredicate.value(), "value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for between condition")
    void testBetweenPredicate() {
      // Given
      String originalFilter = "date:bt:2023-01-01,2023-12-31";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(
          WherePredicate.Between.class, predicate, "Parsed condition should be Between type");
      WherePredicate.Between betweenPredicate = (WherePredicate.Between) predicate;
      assertEquals("date", betweenPredicate.key(), "key value should be parsed correctly");
      assertEquals(
          "2023-01-01", betweenPredicate.fromValue(), "fromValue should be parsed correctly");
      assertEquals("2023-12-31", betweenPredicate.toValue(), "toValue should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(
          "date:bt:2023-01-01,2023-12-31", filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for isNull condition")
    void testIsNullPredicate() {
      // Given
      String originalFilter = "deletedAt:is_null:null";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(1, parsedFilters.size(), "One condition should be parsed");
      WherePredicate predicate = parsedFilters.get(0);
      assertInstanceOf(
          WherePredicate.IsNull.class, predicate, "Parsed condition should be IsNull type");
      WherePredicate.IsNull isNullPredicate = (WherePredicate.IsNull) predicate;
      assertEquals("deletedAt", isNullPredicate.key(), "key value should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("parse and toString test for multiple conditions")
    void testMultiplePredicates() {
      // Given
      String originalFilter = "status:eq:active;price:gt:1000;category:in:food,beverage";

      // When - Parse
      List<WherePredicate> parsedFilters = WherePredicates.parse(originalFilter);

      // Then - Parse
      assertEquals(3, parsedFilters.size(), "Three conditions should be parsed");

      // Verify first condition
      WherePredicate firstPredicate = parsedFilters.get(0);
      assertInstanceOf(
          WherePredicate.Eq.class, firstPredicate, "First condition should be Eq type");
      WherePredicate.Eq eqPredicate = (WherePredicate.Eq) firstPredicate;
      assertEquals("status", eqPredicate.key(), "key value should be parsed correctly");
      assertEquals("active", eqPredicate.value(), "value should be parsed correctly");

      // Verify second condition
      WherePredicate secondPredicate = parsedFilters.get(1);
      assertInstanceOf(
          WherePredicate.Gt.class, secondPredicate, "Second condition should be Gt type");
      WherePredicate.Gt gtPredicate = (WherePredicate.Gt) secondPredicate;
      assertEquals("price", gtPredicate.key(), "key value should be parsed correctly");
      assertEquals("1000", gtPredicate.value(), "value should be parsed correctly");

      // Verify third condition
      WherePredicate thirdPredicate = parsedFilters.get(2);
      assertInstanceOf(
          WherePredicate.In.class, thirdPredicate, "Third condition should be In type");
      WherePredicate.In inPredicate = (WherePredicate.In) thirdPredicate;
      assertEquals("category", inPredicate.key(), "key value should be parsed correctly");
      assertEquals(2, inPredicate.values().size(), "values size should be parsed correctly");
      assertTrue(
          inPredicate.values().containsAll(Arrays.asList("food", "beverage")),
          "values content should be parsed correctly");

      // When - ToString
      String filter = WherePredicates.toString(parsedFilters);

      // Then - ToString
      assertEquals(originalFilter, filter, "Encoded filter string should match original");
    }

    @Test
    @DisplayName("Should throw exception for invalid operator")
    void shouldThrowExceptionForInvalidOperator() {
      // Given
      String originalFilter = "status:invalid:active";

      // When & Then
      assertThrows(
          IllegalArgumentException.class,
          () -> WherePredicates.parse(originalFilter),
          "IllegalArgumentException should be thrown for unsupported operator");
    }
  }
}
