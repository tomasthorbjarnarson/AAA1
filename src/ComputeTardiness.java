import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ComputeTardiness {

    private static String[] rdds = {"0.2", "0.4", "0.6", "0.8", "1.0"};
    private static String[] tfs = {"0.2", "0.4", "0.6", "0.8", "1.0"};
    private static String[] ns = {"5", "10", "15", "20", "25", "30", "35", "40", "45",
            "50", "55", "60", "65", "70", "75", "80","85", "90", "95", "100"};

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

		if (args.length == 1) {
            TardNamic tard = new TardNamic(instance);
            int tardiness = tard.getTardiness();
            System.out.println(tardiness);
        }else if (args.length == 2 && args[1].equals("full")) {
		    fullMeasurement(instance);
        }else if (args.length == 2 && args[1].equals("compare")) {
		    compareMeasurements();
        }else if (args.length == 2 && args[1].equals("measure")) {
            for (int i = 0; i < 20; i++) {
                TardNamic tard = new TardNamic(instance);
                tard.getTardiness();
            }

            System.out.println("warmup done");

            for (String rdd: rdds) {
                for (String tf : tfs) {
                    for (String n : ns) {
                        System.out.println("rdd: " + rdd + " tf: " + tf + " n: " + n);
                        testMeasurements(rdd, tf, n);
                    }
                }
            }
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

        TardNamic deltaRestriction = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = deltaRestriction.getTardinessWithDeltaRestriction();
        stopTime = System.currentTimeMillis();
        System.out.println("--==Delta Restriction algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + deltaRestriction.getCacheHits());
        System.out.println("Cache size: " + deltaRestriction.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();

        TardNamic cache = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = cache.getTardinessWithCache();
        stopTime = System.currentTimeMillis();
        System.out.println("--==Cache algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + cache.getCacheHits());
        System.out.println("Cache size: " + cache.getCacheSize());
        System.out.println("Time : " + (stopTime - startTime));
        System.out.println();

        TardNamic minOptimisation = new TardNamic(instance);
        startTime = System.currentTimeMillis();
        tardiness = minOptimisation.getTardinessWithMinOptimisation();
        stopTime = System.currentTimeMillis();
        System.out.println("--==Min Optimisation algorithm==--");
        System.out.println("Tardiness: " + tardiness);
        System.out.println("Cache hits: " + minOptimisation.getCacheHits());
        System.out.println("Cache size: " + minOptimisation.getCacheSize());
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

    public static void testMeasurements(String rdd, String tf, String n) {
	    int reps = 20;
	    String filename = "../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#" + n + ".dat";
	    ProblemInstance instance = readInstance(filename);
        StringBuilder printval = new StringBuilder();DecimalFormat df = new DecimalFormat();
        rdd = rdd.replace(".", ",");
        tf = tf.replace(".", ",");

	    for (int i = 0; i < reps; i++) {
            TardNamic tard = new TardNamic(instance);
            long startTime = System.currentTimeMillis();
            int tardiness = tard.getTardiness();
            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            printval.append("\r\n").append(rdd).append(";").append(tf).append(";").append(n).append(";")
                    .append(df.format(runTime)).append(";").append(df.format(tardiness)).append(";");
        }
        try {
            FileWriter fw = new FileWriter("test.csv", true);

            fw.append(printval);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compareMeasurements() {
        try {
            FileWriter fw = new FileWriter("compare.csv");
            fw.append("RDD;TF;N;BestFirst;;Greedy;;Tardnamic");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String rdd : rdds) {
            for (String tf : tfs) {
                StringBuilder printval = new StringBuilder();
                ProblemInstance bestFirstInstance = readInstance("../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#10.dat");
                ProblemInstance greedyInstance = readInstance("../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#10.dat");
                ProblemInstance tardInstance = readInstance("../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#10.dat");

                BestFirst bestFirst = new BestFirst(bestFirstInstance);
                long startTime = System.currentTimeMillis();
                int bestFirstTardiness = bestFirst.getSchedule().getTardiness();
                long stopTime = System.currentTimeMillis();
                long bestFirstTime = stopTime-startTime;

                Greedy greedy = new Greedy(greedyInstance);
                startTime = System.currentTimeMillis();
                int greedyTardiness = greedy.getSchedule().getTardiness();
                stopTime = System.currentTimeMillis();
                long greedyTime = stopTime-startTime;

                TardNamic tard = new TardNamic(tardInstance);
                startTime = System.currentTimeMillis();
                int tardTardiness = tard.getTardiness();
                stopTime = System.currentTimeMillis();
                long tardTime = stopTime-startTime;

                printval.append("\r\n").append(rdd.replace(".", ",")).append(";")
                        .append(tf.replace(".", ",")).append(";").append("10;");
                printval.append(bestFirstTardiness).append(";").append(bestFirstTime).append(";");
                printval.append(greedyTardiness).append(";").append(greedyTime).append(";");
                printval.append(tardTardiness).append(";").append(tardTime);
                try {
                    FileWriter fw = new FileWriter("compare.csv", true);
                    fw.append(printval);
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String[] sizes = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};
        for (String rdd : rdds) {
            for (String tf : tfs) {
                for (String n : sizes) {
                    StringBuilder printval = new StringBuilder();
                    ProblemInstance greedyInstance = readInstance("../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#" + n + ".dat");
                    ProblemInstance tardInstance = readInstance("../test-set/instances/random_RDD=" + rdd + "_TF=" + tf + "_#" + n + ".dat");

                    Greedy greedy = new Greedy(greedyInstance);
                    long startTime = System.currentTimeMillis();
                    int greedyTardiness = greedy.getSchedule().getTardiness();
                    long stopTime = System.currentTimeMillis();
                    long greedyTime = stopTime-startTime;

                    TardNamic tard = new TardNamic(tardInstance);
                    startTime = System.currentTimeMillis();
                    int tardTardiness = tard.getTardiness();
                    stopTime = System.currentTimeMillis();
                    long tardTime = stopTime-startTime;

                    printval.append("\r\n").append(rdd.replace(".", ",")).append(";")
                            .append(tf.replace(".", ",")).append(";").append("10;");
                    printval.append("XXX;XXX;");
                    printval.append(greedyTardiness).append(";").append(greedyTime).append(";");
                    printval.append(tardTardiness).append(";").append(tardTime);
                    try {
                        FileWriter fw = new FileWriter("compare.csv", true);
                        fw.append(printval);
                        fw.flush();
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
