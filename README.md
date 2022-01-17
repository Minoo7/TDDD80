**1. Vad skiljer ett ramverk (framework) från ett bibliotek (library)?**

Skillnaden är att ett ramverk bestämmer hela strukturen för koden och koden skrivs för ramverket istället för att ramverket skrivs eller används för själva koden. Ett ramverk kan inte användas i ett projekt som redan finns utan används från början när projektet skapas. Ett bibliotek innehåller istället ett flertal funktioner som är till för att användas utav annan kod/program och kan importeras när som helst i koden. En tydlig jämförelse mellan ramverk och bibliotek som kan göras är att du kallar på koden från biblioteket och resultatet returneras medans ramverket kallar på din kod.

**2. Vilka fördelar erbjuder ramverk, och specifikt Flask, vid utveckling av server-kod jämfört med ren python?**

Ett ramverk har redan byggt upp ett system som vi vill använda så då slipper vi skriva detta system från grunden, fler personer har också redan använt detta system så det finns tillgång till mer support/felsökning av kod. När det kommer specifikt till Flask så har Flask t.ex en inbyggd debuggare och många redan förskriven funktionalitet som inte vi behöver sitta och tänka över. Flask har också bättre säkerhet jämfört om vi hade skrivit kod för en server i python själva.

**3. Vad är en session i server-sammanhang? Och hur fungerar en 'session' i Flask?**

En session i server sammanhang är ett samman-kopplat serie av skickad kod, i detta fall t.ex webbläsar requests där denna serie kommer från en och samma klient. Det behövs på grund av att HTTP är en "stateless" protocol, vilket innebär att utan extern hjälp så har inte själva HTTP protocolet information om tidigare gjorde requests. I en session kan hänvisningar till tidigare requests tillexempel göras, det finns alltså en sorts historik där data lagras i sessionen. Inom webbprogrammering används ofta antingen klient sessions eller server sessions. Klient sessions lagrar datan/historiken som cookies hos webbläsaren medans server sessions lagrar datan på själva servern och kopplar den till rätt klient m.h.a någon identifier, detta används till exempel vid inloggningar på hemsidor där data behöver nås även på olika webbläsare. I Flask så används klient session systemet. En webbläsare som kopplas till en flask server lagrar alltså datan som cookies på webbläsaren och datan kan enkelt tas bort. Det finns flertal fördelar och nackdelar med både klient sessioner och server sessioner.
