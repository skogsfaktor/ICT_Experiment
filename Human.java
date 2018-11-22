package pandemic;

//En instans av den här klassen representerar en individ med en tillfriskningstid och 
//hälsostatus. Flera funktioner för att ändra tillfriskningstid och hälsostatus finns
//tillgängliga. 
public class Human {
    
    //En individs olika möjliga sjukdomstillstånd är fördefinerade. 
    public static enum HealthStatus {
        
        //Frisk. En frisk individ kan bli smittad. 
        healthy(0),
        //Smittad. En smittad kan inte smitta andra eller dö. 
        contracted(1),
        //Sjuk. En sjuk individ kan smitta andra individer och dö. 
        sick(2),
        //Immun. En immun individ kan inte bli smittad.
        immune(3),
        //Död. En död individ kan inte smitta andra individer.  
        dead(4);

        public final byte id;

        private HealthStatus(int id) {
            this.id = (byte) id;
        }
    }

    //En individs status. 
    private HealthStatus status;
    //En individs tillsfriskningstid. 
    private int sickTime;
    
    //skapar en ny frisk individ. 
    public Human() {
        status = HealthStatus.healthy;
    }
    
    public HealthStatus getStatus() {
        return status;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }
    
    //Gör en individ sjuk och sätter en tillfriskningstid. 
    public void setSick(int sickTime) {
        this.status = HealthStatus.sick;
        this.sickTime = sickTime;
    }
    
    public int getSickTime() {
        return this.sickTime;
    }
    
    //Minskar en individs tillfriskningstid med en dag. 
    public void decrementSickTime() {
        sickTime--;
    }

    public void kill() {
        this.status = HealthStatus.dead;
    }
}
