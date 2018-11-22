package pandemic;

import static pandemic.Human.HealthStatus.*;
import java.util.Random;
import java.util.Scanner;


/*
 Utför en simulering med villkorligt valfria inparametrar 0<=S<=1, N>0, minDygn>=0, maxDygn>=0,
 0<=L<=1. Simuleringen avslutas när inga sjuka individer finns kvar i populationen. 
 */
public class Contamination {

    //slumptalsgenerator som används för att bestämma vilka som ska bli infekterade, 
    //vilka som ska dö och hur många dagar som en individ ska vara sjuk. 
    private static Random rand = new Random();
    //Variabel som används för att kontrollera när simuleringen ska ta slut. 
    //Är lika med antalet smittade + antalet sjuka. 
    private static int infected;
    //Hur många som har tillfrisknat under en viss dag. 
    private static int dailyRecovered;
    //Hur många som har avlidit under en viss dag. 
    private static int dailyDeaths;
    //Hur många som har smittats under en viss dag. 
    private static int dailyInfected;

    //Utför en simulering. 
    public static void main(String[] args) {
        double S = 0.7;
        int N = 50;
        int minDygn = 3;
        int maxDygn = 9;
        double L = 0;
        Human[][] humans = initialize(N);

        //Kan ta in parametrar via konsolen. 
        try {
            if (args.length == 0) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Run simulation with default values(demo)(Y/N)");
                String s = sc.next();
                if (s.equals("Y") || s.equals("y")) {
                    humans[N / 2][N / 2].setSick(rand.nextInt(maxDygn - minDygn) + minDygn);
                    infected = 1;
                } else if (s.equals("N") || s.equals("n")) {
                    System.out.println("Antal individer i populationen N*N:");
                    N = sc.nextInt();
                    humans = initialize(N);

                    //skapa en population med friska människor i matrisform. 
                    System.out.println("Smittsannolikheten S för varje dag. Ange 0-1:");
                    S = sc.nextDouble();

                    System.out.println("Minimala antalet dygn som en individ är sjuk:");
                    minDygn = sc.nextInt();

                    System.out.println("Maximala antalet dygn som en individ kan vara sjuk:");
                    maxDygn = sc.nextInt();

                    System.out.println("Dödssannolikheten per dag L. Ange 0-1:");
                    L = sc.nextDouble();

                    System.out.println("Hur många som är sjuka initialt:");
                    infected = sc.nextInt();
                    dailyInfected = infected;

                    for (int i = 1; i <= infected; i++) {
                        System.out.println("Position i x-led för person " + i + "(0-" + N + "): ");
                        int x = sc.nextInt();

                        System.out.println("Position i y-led för person " + i + "(0-" + N + "): ");
                        int y = sc.nextInt();

                        //dem ska vara sjuka, inte smittade!!
                        humans[x][y].setSick(rand.nextInt(maxDygn - minDygn) + minDygn);
                    }
                } else {
                    System.out.println("Wrong answer!, exiting");
                    System.exit(0);
                }
            } else if (args.length == 1) {
                S = Double.parseDouble(args[0]);
            } else if (args.length >= 4) {
                S = Double.parseDouble(args[0]);
                N = Integer.parseInt(args[1]);
                minDygn = Integer.parseInt(args[2]);
                maxDygn = Integer.parseInt(args[3]);
                if (args.length == 5) {
                    L = Double.parseDouble(args[4]);
                }
            } else {
                System.out.println("Arguments are strange. Hmm... Exiting");
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.println("No chance! Not working...");
            System.exit(0);
        }

        //Variabler som används för att generera utdata. 
        dailyInfected = infected;
        int day = 0;
        int accumulatedInfected = 0;
        int accumulatedDeaths = 0;

        //Simuleringen ska ta slut när inga sjuka individer återfinns i populationen. 
        //Även en smittad räknas som sjuk. 
        while (infected > 0) {
            update(S, N, minDygn, maxDygn, L, humans);
            //Uppdatera den visuella representationen. 

            //dailyInfected och dailyDeaths uppdateras i update metoden.
            accumulatedInfected += dailyInfected;
            accumulatedDeaths += dailyDeaths;

            //Utdata. 
            System.out.println("Day: " + day);
            System.out.println("");
            System.out.println("Antal smittade: " + dailyInfected);
            System.out.println("Antal avlidna: " + dailyDeaths);
            System.out.println("Antal tillfrisknade: " + dailyRecovered);
            System.out.println("Ackumulerat antal smittade: " + accumulatedInfected);
            System.out.println("Ackumulerat antal avlidna: " + accumulatedDeaths);
            System.out.println("Kontrollvariabel: Infected: " + infected);
            System.out.println("-----------------------------------------");

            //Återställer dagliga variabler.   
            dailyRecovered = 0;
            dailyDeaths = 0;
            dailyInfected = 0;

            day++;
        }
        //Simulationen är slut här. 
        System.out.println("Humanity has been served!");
        System.exit(0);
    }

    //Sätter slumptalsfrö för pseudo slumptalsgeneratorn som används för att 
    //bestämma om en viss individ ska bli smittad eller dö.
    public static void setRandomSeed(long prime) {
        rand = new Random(prime);
    }

    //En kopia av main metoden fast utan utdata. Returnerar
    //ackumulerat antalet infekterade under en simulering. Anropas från DoHundred.
    //Metoden används för att göra 100 körningar. 
    static int primaryTest(double S, int N, int minDygn, int maxDygn, double L) {
        Human[][] humans = initialize(N);
        humans[N / 2][N / 2].setSick(rand.nextInt(maxDygn - minDygn) + minDygn);
        int day = 0;
        int accumulatedInfected = 0;
        infected = 1;
        dailyInfected = infected;
        //Simuleringen ska ta slut när inga sjuka individer finns kvar i 
        //populationen. 
        while (infected > 0) {
            update(S, N, minDygn, maxDygn, L, humans);

            accumulatedInfected += dailyInfected;

            //Här resettar vi daily variabler. 
            dailyInfected = 0;

            day++;
        }
        return accumulatedInfected;
    }

    //skapar en ny dubbelarray och fyller alla element med en frisk människa. 
    static Human[][] initialize(int N) {
        Human[][] humans = new Human[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                humans[x][y] = new Human();
            }
        }
        return humans;
    }

    //För varje körning av den här funktionen uppdateras statusen för varje individ i populationen
    //en gång. Statusen uppdateras sekventiellt d.v.s. individ för invdivid och rad för rad. 
    static void update(double S, int N, int minDygn, int maxDygn, double L, Human[][] humans) {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                //Om människan är sjuk finns det risk för att den dör innan
                //den hinner infektera någon annan. 
                if (humans[x][y].getStatus() == sick) {
                    //kolla om individen ska dö. 
                    if (rand.nextDouble() < L) {
                        humans[x][y].kill();
                        infected--;
                        dailyDeaths++;
                    } else {
                        //Itererar igenom alla direkta grannar till den nuvarande
                        //individen och undersöker om grannen ska bli infekterad
                        //eller inte. Börjar klockan 9. Går medsols.
                        infect(humans, x - 1, y, N, S, rand.nextDouble());
                        infect(humans, x - 1, y - 1, N, S, rand.nextDouble());
                        infect(humans, x, y - 1, N, S, rand.nextDouble());
                        infect(humans, x + 1, y - 1, N, S, rand.nextDouble());
                        infect(humans, x + 1, y, N, S, rand.nextDouble());
                        infect(humans, x + 1, y + 1, N, S, rand.nextDouble());
                        infect(humans, x, y + 1, N, S, rand.nextDouble());
                        infect(humans, x - 1, y + 1, N, S, rand.nextDouble());

                        humans[x][y].decrementSickTime();
                        //Om individen har varit sjuk i sickTime dagar
                        //ska den bli immun. 
                        if (humans[x][y].getSickTime() <= 0) {
                            humans[x][y].setStatus(Human.HealthStatus.immune);
                            infected--;
                            dailyRecovered++;
                        }
                    }
                }
            }
        }

        //När dagen är över ska alla smittade individer övergå till att bli sjuka.
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (humans[x][y].getStatus() == contracted) {
                    //Människorna är sjuka ett antal dagar som är rektangulär
                    //fördelat i intervallet [minDygn, maxDygn]. 
                    humans[x][y].setSick(rand.nextInt(maxDygn - minDygn) + minDygn);
                }
            }
        }

    }

    //Undersöker om en viss frisk människa ska bli infekterad genom att undersöka 
    //om ett rektangulärfördelat slumptal [0,1] är mindre än smittsannolikheten
    //S. Om den är det förändras människans status till smittad. 
    static void infect(Human[][] humans, int x, int y, int N, double S, double random) {
        //Om människan befinner sig utanför populationsmatrisens gränser ska 
        //inget hända. 
        if (x < 0 || x >= N || y < 0 || y >= N) {
            return;
        }
        //Kolla om människan ska bli smittad. 
        if (random < S) {
            //Människan kan bara bli smittad om den är frisk. 
            if (humans[x][y].getStatus() == Human.HealthStatus.healthy) {
                humans[x][y].setStatus(Human.HealthStatus.contracted);
                dailyInfected++;
                infected++;
            }
        }
    }
}
