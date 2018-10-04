import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			int[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new int[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		ProblemInstance instance = readInstance(args[0]);
		instance.sortJobs();

		if (args.length == 1) {
            TardNamic tard = new TardNamic(instance);
            int tardiness = tard.getTardiness();
            System.out.println(tardiness);
        }else if (args.length == 2 && args[1].equals("full")) {
		    fullMeasurement(instance);
        }else if (args.length == 2 && args[1].equals("tests")) {
		    testMeasurements(instance, args[0]);
        }
	}

	public static void fullMeasurement(ProblemInstance instance) {
        TardNamic raw = new TardNamic(instance);
        long startTime = System.currentTimeMillis();
        int tardiness = raw.getTardinessRaw();
        long stopTime = System.currentTimeMillis();
        System.out.println("--==Raw algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + raw.getCacheHits());
        System.out.println("Cache size: " + raw.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();

        TardNamic noCache = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = noCache.getTardinessNoCache();
        stopTime = System.currentTimeMillis();
        System.out.println("--==NoCache algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + noCache.getCacheHits());
        System.out.println("Cache size: " + noCache.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();

        TardNamic noDeltaRestriction = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = noDeltaRestriction.getTardinessNoDeltaRestriction();
        stopTime = System.currentTimeMillis();
        System.out.println("--==NoDeltaRestriction algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + noDeltaRestriction.getCacheHits());
        System.out.println("Cache size: " + noDeltaRestriction.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();

        TardNamic tard = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = tard.getTardiness();
        stopTime = System.currentTimeMillis();
        System.out.println("--==TardNamic algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + tard.getCacheHits());
        System.out.println("Cache size: " + tard.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();
    }

    public static void testMeasurements(ProblemInstance instance, String fileName) {
	    long totalTime = 0;
	    int totalTardiness = 0;
	    int totalCacheHits = 0;
	    long totalCacheSize = 0;
	    int reps = 5;

	    for (int i = 0; i < reps; i++) {
            TardNamic tard = new TardNamic(instance);
            long startTime = System.currentTimeMillis();
            totalTardiness += tard.getTardiness();
            long stopTime = System.currentTimeMillis();
            totalTime += stopTime - startTime;
            totalCacheHits += tard.getCacheHits();
            totalCacheSize += tard.getCacheSize();
        }
        double aveTardiness = 1.0 * totalTardiness / reps;
        double aveTime = 1.0 * totalTime / reps;
        double aveCacheHits = 1.0 * totalCacheHits / reps;
        double aveCacheSize = 1.0 * totalCacheSize / reps;

        try {
            FileWriter fw = new FileWriter("test.csv", true);
            DecimalFormat df = new DecimalFormat();

            fileName = fileName.replace("..\\test-set\\instances\\","").replace(".dat", "");
            StringBuilder printval = new StringBuilder();
            printval.append("\r\n").append(fileName).append(";").append(df.format(aveTime)).append(";")
                    .append(df.format(aveTardiness)).append(";").append(df.format(aveCacheSize))
                    .append(";").append(df.format(aveCacheHits));
            fw.append(printval);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
