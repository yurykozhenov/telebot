BEGIN TRANSACTION;

DROP TABLE IF EXISTS "pilot_options" CASCADE;
DROP TABLE IF EXISTS "quest_options" CASCADE;
DROP TABLE IF EXISTS "quests" CASCADE;

CREATE TABLE "quests" (
  "quest_id" TEXT PRIMARY KEY,
  "author" INT NOT NULL,
  "text" TEXT,
  "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT "now"(),
  "expires_at" TIMESTAMP WITH TIME ZONE,
  "group_name" TEXT
);

CREATE TABLE "quest_options" (
  "quest_option_id" TEXT PRIMARY KEY,
  "text" TEXT,
  "quest_id" TEXT NOT NULL REFERENCES "quests"("quest_id")
);

CREATE TABLE "pilot_options" (
  "quest_option_id" TEXT REFERENCES "quest_options"("quest_option_id"),
  "id" INT REFERENCES "pilots"("id")
);

END TRANSACTION;