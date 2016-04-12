DROP TABLE IF EXISTS "mail" CASCADE;

CREATE TABLE "mail" (
    "id" BIGINT PRIMARY KEY,
    "sent" timestamp with time zone NOT NULL DEFAULT "now"(),
    "sender" VARCHAR(150) NOT NULL,
    "title" VARCHAR(150) NOT NULL,
    "body" TEXT NOT NULL
);