# Oversikt
- "Studer filene i inf101.v17.boulderdash.bdobjects først"
1. **Hvilke interface finnes? Hva er sammenhengen mellom dem og hva brukes de til?**

        Av interfaces i 'bdobjects' så finner man:
        - IBDObject: 
            Dette er 'hovedinterfacen' som alle objekt i spillet må implementere. Der objektet som nytter
            denne må implementere metodene som er oppgitt. Dette fungerer som en form forkontrakt. Det er da
            lett for den som vil bruke objekte å forholde seg til interfacen uten å tenke på "hvordan" de forskjellige
            metodene er implementert. Denne inneholder f.eks: 'getColor()', 'getMap()', 'getPosition' osv.
            Noe av det viktigeste er nok at alle objekt som skal bruke interfacen må ha en 'step()'-metode.
            Denne vil oppdatere objektet for et 'tidssteg' av kjøring av programmet. 
        - IBDMovingObject: 
            Dette er interfacen som alle objekt som skal ha bevegelse i spillet må implementere.
            Her inngår metoder som en sentrale for dette step-baserte spiller. Bevegelser blir da forberedet og 
            deretter gjennomført ved neste iterering av programmet.
        - IBDKillable:
            Dette er en interface som utvider 'IDBMovingObject'-interfacen. Alle objekt som implementerer må 
            han en metode som skal 'drepe' objektet. Siden denne interface utvider interfacen for bevegende objekt vil 
            det si at alle objekt som implementerer denne alltid kan bevege seg.
         
2. **Hva slags rolle spiller arv i designet til programmet?**
            
          Arv er gjennomgående i programmet. Dette vil si at man går fra det veldig 'abstrakte' (merk abstrakte klasser) 
          til et veldig konkrete som f.eks: BDPlayer. Koden går altså fra å være veldig generell til 
          veldig spesifikk når det gjelder hvordan gitte situasjoner skal 'behandlest'.
          Man ser denne arven via at en gitt klasse nøkkelord: 'extends' en annen klasse.
          F.eks: "AbstractBDMovingObject extends AbstractBDObject" og her arver AbstractBDMovingObject alle metodene
          som eksisterer i AbstractBDObject og utvider på dette med ny funksjonalitet i kontrakt med 
          interfacen IBDMovingObject.
   
3. **Det er flere abstrakte klasser i systemet. Hva slags funksjon har de? Hvorfor er de abstrakte?**

        Av abstrakte klasser finner man:
        - AbstractBDObject       
        - AbstractBDMovingObject          
        - AbstractBDKillingObject          
        - AbstractBDFallingObject
        
        Alle de abstrakte klassene implementerer metoder med grunnleggende logikk for de respektive objektene
        som skal nytte dem, basert på hvilket type objekt det er (altså: bevegende/dødlige, fallende, og generelle). 
            De er abstrakte fordi at klassene som skal utnytte de gjerne bygger på dem med ekstra funksjonalitet,
            og bruker metoder som er tilgjengelige i klassen de utvider (den abstrakte).           
   
4. **Hvor er hoveddelen av logikken til spillet er implementert? Få oversikt over metodene, hvor de er implementert, hvordan de kalles.**
   
        Hoveddelen av logikken til spillet er implementert i klassen BoulderDashGUI (under gui-pakken).
        Her finner vi metodene:
            - run(...)
            - start(...)
            - BoulderDashGUI(...)
            - step()
            - handle(...)
            
        Denne klassen utvider den abstrakte Application-klassen fra 'javafx'. Og tilfører logikken som gjør at man kan
        fremstille spillet grafisk ved hjelp av eksisterende metoder fra 'javafx' sin forskjellige klasser men også ved
        å implementere ekstra kode til disse.
            Man kan se at Main-klassen som har main-metoden kaller på 'run(...)' med et BDMap-objekt som allerede har blitt
            konstruert, feltvariabelen 'theMap' blir da satt til denne. Deretter følger launch()-metoden fra Application
            og starter opp javafx delen av programmet. Denne bruker 'start(...)' med et Stage-objekt og på dette kan flere
            javafx-objekt blir fremstilt, som en slags form for "container" å 'operere' på. Videre blir 'step()' kallt fra
            handle(...) inne i start(...). Handle(...) har ansvaret for hvor raskt spiller oppdaterer og relativt til dette
            kjører step metodene på objektene som oppbevarer seg i "mappen". handle(...)-metoden som er implementert på
            grunnlag av interfacen EventHandler evaluerer om spiller skal avsluttest, kjøres i fullskjerm eller gi input
            videre til evaluering av bevegelse for spiller. Denne metoden jobber sammen med javafx applikasjonen.  
        
5. **Hva slags rolle spiller abstraksjon i dette programmet?**
        
        I programstrukturen gjør abstraksjon at implementeringen av div. funksjonalitet får en hierarkisk struktur. 
        De vil si at man går fra definere hva et objekt som skal oppholde seg i spillet/BDMap må ha --> til å få definere 
        hva forskjellige og mer konkrete typer objekt må ha. Dette skjer ved hjelp av at mer spesifikke abstrakte klasser 
        utvider mer generelle abstrakte klasser og blir definert med innhold ved hjelp av de forskjellige interfacene. 
        'BD-...'-objektene får da viktig funksjonalitet fra disse. Det som er viktig å merke seg er at en abstrakt klasse 
        ikke kan initsialiseres og funksjonaliteten brukes bare gjennom de ulike 'BD-...'-objektene. Så når man da bruker 
        de ulike objektene slipper man å forholde seg til den komplekse strukturen og heller vet at en gitt type vil 
        ha en gitt metode. Man prøver altså å skjule detaljer om data representasjonen.
            Kort sagt: Abstrakte klasser -> Et gitt objekt må ha denne funksjonaliteten, men klassen 
            er fortsatt abstrakt og trenger ekstra funksjonalitet derfor skal den abstrakte initsialiseres.
        
6. **Hvordan kunne man lagt til en ny type felt?**
        
        Svar:
        
7. **Hvordan er det implementert at en diamant faller?**
        
        BDDiamond arver fra AbstractBDFallingObject som inneholder step() og fall(). Denne klassen inneholder logikken
        til alle fallende objekter som skal vere med i spillet. I dette tilfellet vil det si at både diamanter og steiner.
        Denne metoden inneholder flere 'skjekker' som tester for å se at det fallende objektet i det hele tatt har lov 
        til å falle basert på: hva som befinner seg under, til sidene og diagonalt ned på hver side, koordinater i forhold
        til størrelsen på BDMap, om det har gått nok tid for at den skal begynne å falle.
        Om det viser seg at objekte som kan falle nå har lov til dette utifra 'skjekkene' vil metoden prøve å forberede
        bevegelse i en gitt rettning med "prepareMoveTo(...)" metoden fra AbstractBDMovingObject slik at objektet forandrer
        posisjon ved neste iterering av programmet (step).
        
   
3.1     **Spørsmål** //TODO?
        
        - Hvorfor trenger vi getPosition-metoden? Kunne vi like gjerne ha lagret posisjonen i hvert enkelt objekt?
          
        - Hvis vi bruker et hashmap til å forenkle jobben med å finne posisjoner, har vi laget en sammenheng mellom to 
          av feltvariablene; grid-et og hashmap-et. Hva er sammenhengen? (Her har vi en datainvariant, en begrensing på 
          datarepresentasjonen i objektet.)
          
        - Ville vi fått en tilsvarende sammenheng mellom grid-et i BDMap og BD-objektene våre om vi lagret posisjonen i 
          hvert enkelt objekt? Kan det være problematisk?
          

   #Credits:
   **Music**: "Pixelland Kevin MacLeod (incompetech.com)
           Licensed under Creative Commons: By Attribution 3.0 License
           http://creativecommons.org/licenses/by/3.0/"
           
   **Sprites**: "INSERT HERE".