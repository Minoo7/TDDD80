**1. Hur inkluderar man externa bibliotek, t.ex. Volley, i ett Android-projekt?**
Externa bibliotek kan i android Gradle projekt inkluderas genom att lägga till det i gradles build fil genom implementation i dependencies. Där specifierar man även vilken version av biblioteket man ska använd a sig av. Gradle gör sedan en build sync för att implementera biblioteken, då internet anslutning krävs om de ej finns lokalt.

**2. Vad är Java beans för något? Vad är det för fördel med att använda biblioteket gson, gentemot den inbyggda json-parsern?**
Javabeans är en standard i java som används för at enkelt kapsla in flera objekt till ett objekt som kan sedan enkelt återanvändas i flera olika miljöer, till exempel i Json hantering av java objekt. Standarden för att en java klass ska kunna användas som en java bean är att:
* Alla fält är privata och har getters och setters.
* Inga argument i konstruktorn
* Ska implementera java.io.Serializable

Gson är bra för hantering av json-objekt då gson automatiskt konverterar java klasser från och till json-objekt som vi annars vi manuellt skulle behöva göra med den inbyggda json-parsern. Gson hanterar även felhantering.

**3. Vad är den viktigaste, mest centrala frågan, som ni har behövt Googla på under laborationens gång?**
Det mesta jag satt och googla på för denna laboration var hur jag skulle använda mig av retrofit för att göra network requests.