**1. Vad är Androids 'R' för något, vad innehåller den för information, hur genereras den? Hur används den i Android-kod, t.ex. i onCreate i en Activity?**

Androids 'R' är en klass som innehåller åtkomst för applikationspaketets resurser. R genereras automatiskt av aapt (Android Asset Packaging Tool). R klassen innehåller pekare (resource IDs) på det som finns i "res/" mappen och alla resurser/komponenter i activity.xml filen för en specifik Activity. I en onCreate i en Activity kan R klassen användas för att t.ex få fram ett specifikt objekt från Activity:ien. Till exempel kan findViewById(R.id.<ID_NAMN>) användas för att nå ett specifikt objekt från en Activity.

**2. Vad är Gradle? Vad innebär Gradle sync?**

Gradle är ett byggverktyg som används för att bygga ihop ett Android projekt till apk-filer (Android Application Package). Apk filen innehåller byte kod samt resurser för koden som t.ex bilder. Gradle stödjer flera språk men vi använder den för Java. Gradle sync är en uppgift som Gradle utför där den tittar igenom build.gradle filen för att ladda ner alla "dependencies" och deras version som behövs för att bygga projektet. Den liknar en requirements fil i pythons pip.

**3. Vad är Instant Run i Android Studio?**

Instant run är ett körningssätt som sparar tid genom att bara byta ut den uppdatera delen av koden som byts för enheten som kör apk filen jämfört med en vanlig build+run där hela APK filen skickas om till filen.