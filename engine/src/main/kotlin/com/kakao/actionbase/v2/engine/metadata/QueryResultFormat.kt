package com.kakao.actionbase.v2.engine.metadata

enum class QueryResultFormat {
    /**
     *  {
     *      "meta":
     *      [
     *          {
     *              "name": "num",
     *              "type": "Int32"
     *          },
     *          {
     *              "name": "str",
     *              "type": "String"
     *          },
     *          {
     *              "name": "arr",
     *              "type": "Array(UInt8)"
     *          }
     *      ],
     *
     *      "data":
     *      [
     *          {
     *              "num": 42,
     *              "str": "hello",
     *              "arr": [0,1]
     *          },
     *          {
     *              "num": 43,
     *              "str": "hello",
     *              "arr": [0,1,2]
     *          },
     *          {
     *              "num": 44,
     *              "str": "hello",
     *              "arr": [0,1,2,3]
     *          }
     *      ],
     *
     *      "rows": 3,
     *
     *      "rows_before_limit_at_least": 3,
     *
     *      "statistics":
     *      {
     *          "elapsed": 0.001137687,
     *          "rows_read": 3,
     *          "bytes_read": 24
     *      }
     *  }
     */
    JSON,

    /**
     * {
     *     "num": [42, 43, 44],
     *     "str": ["hello", "hello", "hello"],
     *     "arr": [[0,1], [0,1,2], [0,1,2,3]]
     * }
     */
    JSON_COLUMNS,

    /**
     *  {
     *      "meta":
     *      [
     *          {
     *              "name": "num",
     *              "type": "Int32"
     *          },
     *          {
     *              "name": "str",
     *              "type": "String"
     *          },
     *
     *          {
     *              "name": "arr",
     *              "type": "Array(UInt8)"
     *          }
     *      ],
     *
     *      "data":
     *      {
     *          "num": [42, 43, 44],
     *          "str": ["hello", "hello", "hello"],
     *          "arr": [[0,1], [0,1,2], [0,1,2,3]]
     *      },
     *
     *      "rows": 3,
     *
     *      "rows_before_limit_at_least": 3,
     *
     *      "statistics":
     *      {
     *          "elapsed": 0.000272376,
     *          "rows_read": 3,
     *          "bytes_read": 24
     *      }
     *  }
     */
    JSON_COLUMNS_WITH_METADATA,

    /**
     *  {
     *      "meta":
     *      [
     *          {
     *              "name": "num",
     *              "type": "Int32"
     *          },
     *          {
     *              "name": "str",
     *              "type": "String"
     *          },
     *          {
     *              "name": "arr",
     *              "type": "Array(UInt8)"
     *          }
     *      ],
     *
     *      "data":
     *      [
     *          [42, "hello", [0,1]],
     *          [43, "hello", [0,1,2]],
     *          [44, "hello", [0,1,2,3]]
     *      ],
     *
     *      "rows": 3,
     *
     *      "rows_before_limit_at_least": 3,
     *
     *      "statistics":
     *      {
     *          "elapsed": 0.001222069,
     *          "rows_read": 3,
     *          "bytes_read": 24
     *      }
     *  }
     */
    JSON_COMPACT,

    /**
     * [
     *   [42, 43, 44],
     *   ["hello", "hello", "hello"],
     *   [[0,1], [0,1,2], [0,1,2,3]]
     * ]
     */
    JSON_COMPACT_COLUMNS,
}
