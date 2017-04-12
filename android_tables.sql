CREATE TABLE android.users
(
    id_user SERIAL PRIMARY KEY,
    pseudo character(30),
    password character(30)
);

CREATE TABLE android.profiles
(
    firstname character(30),
    lastname character(30),
    email character(30),
    user_id integer,
	FOREIGN KEY (user_id) REFERENCES android.users(id_user)
);

CREATE TABLE android.buildings
(
    id_building SERIAL PRIMARY KEY,
    name_main character(30),
    name_specific character(50)
);

CREATE TABLE android.places
(
    id_place SERIAL PRIMARY KEY,
    name_place character(30),
    area geometry,
	walls geometry,
    building_id integer,
    FOREIGN KEY (building_id) REFERENCES android.buildings(id_building)
);

CREATE TABLE android.paths
(
    id_path SERIAL PRIMARY KEY,
    coordinates geometry,
    date_time timestamp without time zone NOT NULL,
	user_id integer,
    building_id integer,
    FOREIGN KEY (user_id) REFERENCES android.users(id_user),
    FOREIGN KEY (building_id) REFERENCES android.buildings(id_building)
);