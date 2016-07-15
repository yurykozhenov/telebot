BEGIN TRANSACTION;

DROP TABLE IF EXISTS "jokes" CASCADE;
DROP SEQUENCE IF EXISTS "jokes_seq" CASCADE;

CREATE SEQUENCE "jokes_seq";

CREATE TABLE "jokes" (
    "joke_id"   BIGINT PRIMARY KEY DEFAULT "nextval"('"jokes_seq"'),
    "timestamp" timestamp with time zone NOT NULL DEFAULT "now"(),
    "from_name" VARCHAR(150) NOT NULL,
    "from_id"   BIGINT,
    "joke_text" TEXT NOT NULL
);

INSERT INTO "jokes" ("from_name", "from_id", "joke_text") VALUES ('Finne Trolle', 90659919, 'Rent macht frei');

END TRANSACTION;
