
public class Code {
	/*
	 MyAsynTaskBuildingId task1 = new MyAsynTaskBuildingId();
     task1.execute(String.valueOf(latitude), String.valueOf(longitude));
     MyAsynTaskPathId task2 = new MyAsynTaskPathId();
     task2.execute();
     MyAsynTaskCoordId task3 = new MyAsynTaskCoordId();
     task3.execute();
     MyAsynTaskPlaceId task4 = new MyAsynTaskPlaceId();
     task4.execute(String.valueOf(latitude), String.valueOf(longitude));
     MyAsynTaskLastId task5 = new MyAsynTaskLastId();
     task5.execute(String.valueOf(id_client));
	 */
	/*
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String date = String.valueOf(timestamp);
    latitude = iaLocation.getLatitude();
    longitude = iaLocation.getLongitude();
    MyAsynTaskNewPath newPath = new MyAsynTaskNewPath();
    newPath.execute(id_path, id_coord, String.valueOf(latitude), String.valueOf(longitude), date, String.valueOf(id_client), building_id);
	*/
	
	/*
	public class MyAsynTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
        	String id_user = arg0[0];
        	ArrayList<String> list_predictions = new ArrayList<String>();
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        	String prediction = "";
            try {
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/predict/" + id_user);

                InputStream inputStream = request.sendRequest(url);
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    list_predictions = gson.fromJson(reader, listType);
                    return list_predictions;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    */
}
