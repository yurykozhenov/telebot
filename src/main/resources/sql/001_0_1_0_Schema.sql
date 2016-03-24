--BEGIN TRANSACTION;

DROP TABLE IF EXISTS "pilots" CASCADE;

CREATE TABLE "pilots" (
    "id" INT PRIMARY KEY,
    "first_name" VARCHAR(150),
    "last_name" VARCHAR(150),
    "username" VARCHAR(150),
    "api_key" INTEGER NOT NULL,
    "v_code" VARCHAR(64) NOT NULL,
    "character_name" VARCHAR(100) NOT NULL,
    "character_id" BIGINT NOT NULL,
    "moderator" BOOLEAN DEFAULT FALSE,
    "renegade" BOOLEAN DEFAULT FALSE
);

--END TRANSACTION;