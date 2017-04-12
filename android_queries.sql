-- Return name of the rooms where the user's already gone

SELECT DISTINCT pl.name_place
FROM android.places AS pl, android.buildings AS b, android.paths AS pa
WHERE pa.user_id=1 AND b.id_building=pa.building_id AND b.id_building=pl.building_id
    AND ST_Within(pa.coordinates, pl.area)='true';
	
