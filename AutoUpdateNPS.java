import java.sql.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class AutoUpdateNPS {
    private static final String JDBC_URL = "jdbc:mysql://my_database_url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TABLE_NAME = "my_table";
    private static final String NUMERIC_COLUMN = "numeric_column";

    private static final int[] surveyResponses = new int[10];
    private static final int NPS_UPDATE_INTERVAL_MS = 50000; // Update every 50 seconds

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTask(), 0, NPS_UPDATE_INTERVAL_MS);
    }

    private static class UpdateTask extends TimerTask {
        @Override
        public void run() {
            try {
                Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT " + NUMERIC_COLUMN +
                        " FROM " + TABLE_NAME +
                        " ORDER BY id DESC LIMIT 10");

                int index = 0;
                while (resultSet.next()) {
                    surveyResponses[index++] = resultSet.getInt(NUMERIC_COLUMN);
                }

                resultSet.close();
                statement.close();
                connection.close();

                System.out.println("Updated Survey Responses Array: " + Arrays.toString(surveyResponses));

                int nps = calculateNPS(surveyResponses);
                System.out.println("Net Promoter Score (NPS): " + nps);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static int calculateNPS(int[] surveyResponses) {
        int promoters = 0;
        int passives = 0;
        int detractors = 0;

        final int PROMOTER_THRESHOLD = 9;
        final int PASSIVE_THRESHOLD_LOW = 7;
        final int PASSIVE_THRESHOLD_HIGH = 8;
        final int DETRACTOR_THRESHOLD = 6;

        for (int response : surveyResponses) {
            if (response >= PROMOTER_THRESHOLD) {
                promoters++;
            } else if (response >= PASSIVE_THRESHOLD_LOW && response <= PASSIVE_THRESHOLD_HIGH) {
                passives++;
            } else if (response >= DETRACTOR_THRESHOLD) {
                detractors++;
            }
        }

        int totalResponses = surveyResponses.length;
        double nps = ((double) promoters / totalResponses) * 100 - ((double) detractors / totalResponses) * 100;

        return (int) nps;
    }
}

