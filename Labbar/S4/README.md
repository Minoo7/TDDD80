**1. Varför är det bra att salta ett lösenord innan den hashas och lagras i en databas?**

Ju större en app till exempel blir så är det oundvikligt att ett lösenord kommer upprepas flera gånger hos olika användare, detta skapar ett problem då hashningen för de lösenorden kommer bli likadana. Detta skulle kunna leda t.ex till en hash-table attack. För att motverka detta "saltas" lösenord innan de hashas. Detta innebär att en slumpmässig sträng av tecken placeras (oftast innan) lösenordet och därefter hashas lösenordet. Detta resulterar i att två personer med samma lösenord kommer ha olika hashade lösenord i databasen.

**2. Vad är det för fördelar med att använda någon form av digital signature, JWS (JSON Web Signature), eller JWT (JSON Web Token) när man ska kommunicera mellan server och klient i samband med t.ex. autentisering?**

En JSON Web Token är något skickas med när klienten ska kommunicera med servern där de behövs ett säkerhetsskydd, autentisering t.ex om klienten ska skicka ett meddelande. JSON Web Signature är uppbyggd på det sättet att anrop/kommunikation är signerad med en private key som sedan kan bli verifierad av servern med en hemlig public key. Om denna signering lyckas så säkerställer servern att kommunikationen som skett mellan servern och klienten inte har ändrats eller blivit påverkat av något slags intrång. JWS bör bara används för data som är icke-sensitive. JWS är utmärkt för web kommunikation då JWS formatet är optimiserat för URI queries och kan skickas med och förstås av många olika språk/strukturer. Användningen av dessa system sparar även tid då databasen inte behöver hålla koll på saker som användarens token redan har koll på.


**3. Att genomföra under/efter laborationen: Redogör för det mest centrala begrepp/problem som ni har behövt googla på under arbetet med labb4 (inkludera länk(ar) till de svar som ni tyckte var mest informativa).**

Det mesta jag behövt googla för denna labb är problem jag fick med flask_jwt_extended då versionen jag hade installerat var för ny och flertal kommandon saknades i denna version (t.ex get_jwt) jag lyckades lösa detta med användning av andra kommandon som get_raw_jwt i detta fall.

För att förklara skillnaden mellan JWT och JWS tyckte jag denna artikel formulerade det väldigt bra: 

[What is JWT, JWS, JWE, and JWK? When we should use which token in our business applications.](https://medium.com/@goynikhil/what-is-jwt-jws-jwe-and-jwk-when-we-should-use-which-token-in-our-business-applications-74ae91f7c96b)