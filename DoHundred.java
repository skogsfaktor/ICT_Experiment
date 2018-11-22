package pandemic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/* Den här klassen kan användas för att köra 100 smittsmuleringar där varje 
simulering använder ett i en array med 100 slumptalsfrön under varje simulering. 
*/
public class DoHundred {
    //En array med 100 primtal som användas som slumptalsfrön i klassen Contamination. 
    //Används för att säkerställa statistiskt valida resultat. 
    static long[] primes = {961777351L, 961777367L, 961777381L, 961777391L, 961777417L, 961777441L, 961777451L, 961777459L, 
 961777493L, 961777507L, 961777529L, 961777541L, 961777589L, 961777711L, 961777717L, 961777723L, 
 961777753L, 961777783L, 961777807L, 961777811L, 961777813L, 961777823L, 961777829L, 961777841L, 
 961777871L, 961777897L, 961777903L, 961777937L, 961777969L, 961777981L, 961778023L, 961778087L, 
 961778093L, 961778101L, 961778137L, 961778159L, 961778161L, 961778171L, 961778173L, 961778267L, 
 961778269L, 961778273L, 961778357L, 961778387L, 961778429L, 961778453L, 961778471L, 961778473L, 
 961778483L, 961778509L, 961778527L, 961778563L, 961778581L, 961778593L, 961778599L, 961778659L, 
 961778681L, 961778693L, 961778717L, 961778743L, 961778749L, 961778761L, 961778773L, 961778789L, 
 961778801L, 961778827L, 961778837L, 961778843L, 961778891L, 961778897L, 961778899L, 961778911L, 
 961778921L, 961778929L, 961778941L, 961778957L, 961778977L, 961778989L, 961779011L, 961779043L, 
 961779197L, 961779241L, 961779257L, 961779263L, 961779361L, 961779367L, 961779383L, 961779397L, 
 961779421L, 961779463L, 961779493L, 961779547L, 961779571L, 961779587L, 961779613L, 961779647L, 
 961779649L, 961779683L, 961779719L, 961779727L, 961779757L, 961779769L, 961779787L, 961779823L, 
 961779857L, 961779859L, 961779883L, 961779893L};

    public static void main(String[] args) {
        //Antal simuleringar som ska köras för givna S, N, minDygn, maxDygn och L. 
        int times = 100;
        double S = 0.05;
        int N = 50;
        int minDygn = 3;
        int maxDygn = 9;
        double L = 0;
        
        //Kan ta in ett antal parametrar upp till 5 stycken via kommandoraden. 
        //Kör med skönsvärden om inga parametrar anges vid körning. 
        if (args.length == 2) {
            try {
                S = Double.parseDouble(args[0]);
                times = Integer.parseInt(args[1]);

            } catch (Exception ex) {
                System.out.println("Something went wrong. Please retry");
                System.exit(0);
            }
        } else if (args.length == 6) {
            try {
                S = Double.parseDouble(args[0]);
                N = Integer.parseInt(args[1]);
                minDygn = Integer.parseInt(args[2]);
                maxDygn = Integer.parseInt(args[3]);
                L = Double.parseDouble(args[4]);
                times = Integer.parseInt(args[5]);
            } catch (Exception ex) {
                System.out.println("Something went wrong. Please retry");
                System.exit(0);
            }
        } else {
            System.out.println("No arguments found, running default configuration times = " + times  +", S = " + S + ", N = " + N + ", minDygn = "
            + minDygn + ", maxDygn = " + maxDygn + ", L = " + L);
        }
        
        //loopvariabel. 
        int n = times;
        //Resultat array. Varje element kommer att innehålla antalet smittade
        //efter en simulering. 
        int[] results = new int[times];
        //ackumulerat antal infekterade efter times antal simuleringar. 
        int accInfected = 0;
        while (n > 0) {
            //Sätter ett slumptalsfrö för slumptalsgeneratorn i klassen Contamination. 
            Contamination.setRandomSeed(getPrime(n-1));
            //Gör en simulering och lagrar antalet smittade under den simuleringen
            //i ett element i results. 
            results[n-1] = Contamination.primaryTest(S, N, minDygn, maxDygn, L);
            accInfected += results[n-1];
            n--;
        }
        //Det aritmetiska medelvärdet av total antalet smittade under times antal
        //körningar. 
        double mean = accInfected/times;
        //Stickprovets standardavvikelse. 
        double dev = sampleStandardDeviation(results, mean);
        
        System.out.println("Mean: " + mean);
        System.out.println("Deviation: " + dev);
    }
    
    //returnerar standardavvikelsen för alla heltal i result givet medelvärdet
    //av alla tal i result. 
    public static double sampleStandardDeviation(int[] result, double mean){
        //sidan 288, boken. stickprovets standardavvikelse för mätdatan
        double sum = 0;
        //summan av skillnaden mellan varje mätvärde och medelvärdet i kvadrat. 
        for(int i : result){
            sum +=  Math.pow((i - mean), 2);
        }
        //Delar på antalet mätdata - 1. 
        double sTwo = sum/(result.length-1);
        return Math.sqrt(sTwo);
    } 
    
    //returnerar ett primtal från primes arrayen.  
    private static long getPrime(int index) {
        return primes[index];
    }
}
