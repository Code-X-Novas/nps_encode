public class npsLogic {

    public static void main(String[] args) {
        // this is the example
        int[] surveyResponses = {9, 8, 7, 6, 9, 10, 8, 7, 9, 10};

        int nps = calculateNPS(surveyResponses);

        // Displaying  NPS here
        System.out.println("Net Promoter Score (NPS): " + nps);
    }

    private static int calculateNPS(int[] surveyResponses) {
        int promoters = 0;
        int passives = 0;
        int detractors = 0;

        // Counting the number of promoters, passives, and detractors
        for (int response : surveyResponses) {
            if (response >= 9 && response <= 10) {
                promoters++;
            } else if (response >= 7 && response <= 8) {
                passives++;
            } else if (response >= 0 && response <= 6) {
                detractors++;
            }

        }

        // Calculate NPS as the percentage of promoters minus detractors
        int totalResponses = surveyResponses.length;
        double nps = ((double) promoters / totalResponses) * 100 - ((double) detractors / totalResponses) * 100;

        return (int) nps;
    }

}
