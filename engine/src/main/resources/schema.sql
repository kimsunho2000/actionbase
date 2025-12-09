CREATE TABLE `kc_graph_metadata`
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT,
    `k`            varchar(512)  NOT NULL COMMENT 'Unified metadata management key',
    `v`            text          NOT NULL COMMENT 'Unified metadata management value',
    `created_at`   datetime(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `created_by`   varchar(45)  NOT NULL,
    `modified_at`  datetime(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `modified_by`  varchar(45)  NOT NULL,
    `update_ts`    datetime(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_kc_graph_metadata_k` (k),
    KEY `idx_kc_graph_metadata_createdat` (`created_at`),
    KEY `idx_kc_graph_metadata_updatets` (`update_ts`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='Table for KC-GRAPH unified metadata management';
