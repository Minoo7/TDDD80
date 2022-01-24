**Vad erbjuder Alchemy för fördelar när man arbetar med databaser?**
Vid skapande av databaser med koppling till python så kan en object relational mapper användas (ORM). Alchemy är en ORM skriven i python som ger ett enkelt interface för SQL i python. Med Alchemy kan python kod användas för att skapa och exekvera sql databaskod. Detta underlättar om man inte är bekant med sql kod och man slipper installera andra verktyg för att jobba med sql databaser om man inte använde Alchemy.

**Vad innebär modulär kod-struktur när det gäller Flask-kod? Hur kan cirkulära importer ställa till det? Har ni hittat någon bra lösning?**
Modulär kod-struktur är ett design system för kod där funktioner/funktionalitet tydligt separeras i olika utbytbara moduler (filer). När det gäller Flask-kod kan modulär kod-struktur användas m.h.a Flasks inbyggda funktion "Blueprints" 
databas kan också vara lagrad i annan fil...

**Vad finns det för problem (och sätt att lösa det) med att uppdatera _strukturen_ på en databas som redan innehåller data? Vad kommer ni att ha för strategi i denna fråga för er app-utveckling?**
