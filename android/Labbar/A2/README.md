**1. När kan man behöva använda/ärva från AppCompatActivity itsf Activity?**

AppCompatActivity är ett bibliotek som möjliggör användning av nyare versioners kod/funktioner i äldre versioner av Android. M.h.a AppCompatActivity kan då android kod som är för gammal för att använda nyare funktioner, använda dessa funktioner. AppCompatActivity stödjer alla versioner från och med Android version 2.1. AppCompatActivity till mestadels används för att nya versioner av kod i en android app ska fungera på äldre enheter. 


**2. Vad är det för skillnad i funktionalitet mellan Activity, Fragment, och View?**
Acitivity är själva appen/programmet som enheten kör som förser enheten med en skärm. Inuti en activity kan det finnas fragments som representerar något specifikt eller en del av skärmen med specifik användbarhet, fragments kan enkelt navigeras emellan och vara nästlade. För att fragment ska kunna visas behövs en activity som visar den. Både activity och fragment kan innehålle flertal olika typer av views. Views representerar både input och output delar av skärmen och innehålla views inuti i sig beroende på vilken view det handlar om. En view representerar det vi ser på skärmen och kan interagera med.


**3. Vad är den viktigaste, mest centrala frågan, som ni har behövt Googla på under laborationens gång?**

Den viktigaste mest centrala frågan jag behövt googla är hur androids navigation system fungerar och fick väldigt mycket problem med att implementera "argument skickande" då det strulade mycket med dependencies och build.gradle filen.