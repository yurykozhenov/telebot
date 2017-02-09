BEGIN TRANSACTION;

DROP TABLE IF EXISTS "meetings" CASCADE;

CREATE TABLE "meetings" (

  "meeting_id" TEXT PRIMARY KEY,
  "from_tele_id" BIGINT NOT NULL,
  "to_tele_id" BIGINT NOT NULL,
  "timestamp" timestamp with time zone NOT NULL DEFAULT "now"(),
  "result" TEXT

);

END TRANSACTION;