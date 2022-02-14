**1. Vad är Heroku för typ av tjänst?**

Heroku är ett verktyg för att skriva applikationer enkelt i "molnet".
Heroku är en moln baserat platform as a service (PaaS). Tjänsten stödjer flertal programmeringsspråk och används främst i syfte att skapa, hantera och
köra appar. Heroku kombinerar flera programmeringsspråk för att möjliggöra detta. Heroku är väldigt lämpad för att skala appar då applikationen kan utvecklas utan att appens server behöver "startas om" vid ny kod till exempel.
Grundtanken för Heroku är att utvecklaren ska kunna fokusera på att skapa och utveckla sin app och slippa tänka på infrastrukturen runt om kring appen (servern, hårdvara, management, databas) etc.
I heroku lägger du enkelt ut inställningarna/ramen för hur applikation ska struktureras när det kommer till data lagring t.ex och sedan löser Heroku resten.


**2. Varför rekommenderas ni köra en annan typ av databas än SQLite, dvs. vad finns det för fördelar med att köra PostgreSQL istället för SQLite på Heroku?**

PostgreSQL erbjuder flera datatyper och är bättre på att hantera användare och dess rättigheter. SQLite är en enklare version med mindre funktioner som fungerar bäst på lokal nivå men eftersom vi ska arbeta med server databaser som ska nås överallt så är PostgreSQL ett bättre val. PostgreSQL har inbyggd säkerhet som användaren slipper implementera, vilket är något som är viktigt för databaser med användar uppgifter som vi kommer t.ex att ha.
SQLite har mer lämpliga användningsområden där lokal lagring behövs. Till exempel en mobil som ska hålla koll på filer som ligger lokalt så kan SQLite vara ett bättre val än PostgreSQL både eftersom det lokala inte ska vara kopplat till någon server och eftersom SQlite är snabbare.

**3. Att genomföra under/efter laborationeör något begrepp/problem som ni har behövt googla på under arbetet med labb3 (inkludera länk(ar) till de svar som ni tyckte var mest informativa).**

"*table does not exist*":
Detta var ett fel som uppstod när jag försökte uppnå mer funktionalitet i min kod och delade upp den. T.ex så flyttade jag SQLAlchemy config inställningarna till en ny fil 'config.py' och försökte ladda in inställningarna från en fil istället. Problemet var att jag hade försökt ladda in inställningarna efter databasen skapades
```
app = Flask(__name__)
db = SQLAlchemy(app)
app.config.from_pyfile('config.py')
```
och jag behövde istället ladda config filen innan:
```
app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)
```
**Svarslänk: https://stackoverflow.com/questions/51046562/sqlalchemy-error-table-not-found/51054088**

---
"*postgresql ENUM type requires a name*": 
Detta var ett problem jag fick sitta med länge då man inte kunde använda samma Enum datatyp när de gällde postgreSQL som innan och fick helt enkelt hitta ett alternativt sätt att implementera Enum datatypen som även fungerade för postgreSQL.

**Svarslänk: https://stackoverflow.com/questions/2676133/best-way-to-do-enum-in-sqlalchemy**

---
"*postgress error type modifier is not allowed for Text*": Detta felet fick jag när jag försökte använda SQL datatypen Text istället för String för ett fält i en klass som skulle innehålla mobilnummer, min tanke var att Text var ett bättre datatypsval eftersom siffror (int) som stoppas in i en String i SQL kan omkonverteras till en VARCHAR och då ville jag använda Text för att motverka det då Text ser allting som en sträng och inte gör någon omkonvertering. Problemet uppstod då jag ville att fältet bara skulle få innehålla max 20 tecken men detta var inte något som stödjes i postgreSQL:s datatyp av Text (att ange max antal tecken) ```Text(20)```. Så jag fick helt enkelt använda ```String(20)``` och sedan i framtiden göra någon form av kontroll av tecknen som skickades in.

**Svarslänk: https://github.com/eNMS-automation/eNMS/issues/138**