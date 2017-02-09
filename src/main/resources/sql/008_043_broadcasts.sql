BEGIN TRANSACTION;

DROP TABLE IF EXISTS "broadcasts" CASCADE;
DROP SEQUENCE IF EXISTS "broadcasts_seq" CASCADE;

CREATE SEQUENCE "broadcasts_seq";

CREATE TABLE "broadcasts" (
  "broadcast_id"   BIGINT PRIMARY KEY DEFAULT "nextval"('"broadcasts_seq"'),
  "timestamp" timestamp with time zone NOT NULL DEFAULT "now"(),
  "from_name" TEXT NOT NULL,
  "to_group"  TEXT,
  "message" TEXT,
  "receivers_count" BIGINT
);

END TRANSACTION;