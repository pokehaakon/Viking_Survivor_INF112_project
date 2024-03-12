# INF112 Project - *Viking Survivor*

* Team: *VirtualVikings* (Gruppe 6): *Haakon Osmundsen Benning, Hallvard Stensrud Hardang, Ingmar Aarsland Forsberg,
  Carl-Henrik Lien, Andreas Mærland, Eilif Eilifsen
* https://git.app.uib.no/virtuellviking/inf112.24v.libgdx-template

---
# A1
# Om spillet
*"Viking Vidar"* har blitt etterlatt i England, og må forsvare seg selv mot horder av engelskmenn og andre unaturlige
skapninger - Vidar sin skjebne er satt i stein, og steinen faller mot Valhalla, det eneste spørsmålet er hvor
sterk kan Vidar bli før han skal møte Odin.

# Kjøring
* kompilerer med 'mvn package'.
* Kjøres med 'java -jar target/gdx-app-1.0-SNAPSHOT-fat.jar'
* Krever Java 17

# Kontroller
* [W,A,S,D] for player movement, aswell as UI navigation
* mouselocation for projectile direction, aswell as UI navigation
* [Enter] and mouseclick for UI navigation
* [Spacebar] for special attack if this is possible to implement within the time-limit
* [Esc] for pause

# Kjente feil
Vidar er ikke komemt frem til England enda, og når spillet kjøres, viser bare Hello World Crocodile isteden.

# Credits
to be determined

---
# A2
# Konsept
* Spillerfigur som kan styres sett ovenfra, Kan bevege seg i X/Y retninger.
* Hovedmeny/startmeny
  * Velg arena å spille i.
    * Deretter velg karakter å spille.
  * Globale spillerstatistikkoppgraderinger
    * En bruker penger en samler gjennom å utslette fiender i spillet til å oppgradere spillerstatistikk.
  * Instillinger 
* Pausemeny
  * Vises ved esc-knappen når en spiller i arenaen. Viser spillerstatistikk, gå til hovedmeny. 

* Todimensjonal verden:
    - Arena – området/brett hvor spilleren kan bevege seg.
    - Avgrensning – vegg i enden av brettet på alle kanter hvor spiller ikke kan bevege seg utenfor.
    - Objekter - fordelt utover arenaen er det ulike objekter som f. eks trær, gjerder som spiller/fiender ikke kan gå gjennom. 
* Fiender som beveger seg mot spiller og er skadelig ved berøring. 
  * Nye bølger av fiender kommer ved visse tidspunkt (f. eks hver 2 minutt).
  * Disse bølgene kan inneholde mer og/eller nye fiender.
  * Elitefiender som er større og har mer liv/tar mer liv, legger fra seg skattekiste med penger/power-ups.
* Spillerfiguren skyter tilbake for å ødelegge fiender. Dette skjer automatisk og uten sikting fra spilleren. "Sikting" utføres ved spillerfigurens bevegelser.
  * Ulike våpen har ulik funksjon (f. eks skyter mot nærmeste fiende, skadeområde rundt spilleren, treffer tilfeldig fiende, osv.) 
* Spilleren kan samle xp points som fiender legger fra seg når de dør. 
Når spiller har samlet nok av disse vil spilleren øke i nivå og det blir presentert et valg mellom 3 power-ups.
* Power-ups er i form av våpen eller tilbehør, og spilleren har plass til 7(?) av hver.
  * Tilbehør øker spillerens kraft indirekte ved f. eks øke +5% all skade, +10% skuddstørrelse, eller +20% XP-innsamlingsradius.
  * Hvis en får et våpen/tilbehør en har fra før kan man velge det igjen, som fører til at det øker i nivå.
    (Dette øker f. eks skade/størrelse/antall skudd) Power-ups har maksimumsnivå.
* Utfordringen i spillet er å overleve lenger og mer utfordrende bølger av angrep fra fiender fram til tiden går ut.

---
# A3-1
# Prosses
Prosjekt-organiseringen følger **scrum** modellen. det blir holdt "daily" standups 2-3 ganger i uken på discord, 
og ett sprint review i uken som er fysisk. Kommunikasjon mellom møter skjer på gruppens discord "Virtual Vikings"

# Arbeidsfordeling
team lead velger arbeid fra bracklog, og distribuerer for sprint (en sprint er en 1 ukers tidsramme hvor teamet 
fokuserer på arbeidet gitt fra team lead, eks. uke 11 "enemy_type_bat implementering", hvor det videre deles opp
i arbeidet som ligger til grunn for å kunne implementere en flaggermus fiende. eks. sprite, testing, 
characteristics(eks. abilities, movement, drops), sound) til scrum devs. Høyt fokus på fleksibilitet, da det ikke er 
ønskelig at en scrum dev jobber 80% med graphics, og 20% med koding hver eneste sprint. 
Code/test reviews vil bli gjort underveis i sprinten via standups.

# Oppfølging av arbeid
oppfølgingen skjer via "daily" standups, hvor progresjon eller oppståtte problemer blir rapportert, evt. forslag 
til mulige endringer blir lagt frem. 1 møte fysisk ukentlig for sprint review, hvor det sees på hva som er blitt gjort,
hva som må gjøres, evt. om noe tar lenger tid enn forventet. også feedback på ting som er gjort bra.

---
# A3-2
# En kort beskrivelse av det overordnede målet for applikasjonen
Målet med vår applikasjon, Viking survivor, er å tilby en engasjerende og underholdende spillopplevelse, som 
kombinerer elementer av overlevelse og progresjon i en norrøn-inspirert verden. Spilleren vil kontrollere en karakter 
som må overleve mot stadig sterkere bølger av fiender, samtidig som spilleren samler inn ressurser og oppgraderer 
ferdigheter. Målet er å overleve så lenge som mulig og oppnå høyest mulig poengsum.

# MVP - Minimum viable product:
**MVP i prioritert rekkefølge**
1. Vise et spillebrett og spilleren
2. Kunne bevege spilleren fritt på en begrenset bane.
3. Et utvalg av grunnleggende fiender som angriper spilleren i bølger.
4. Et poengsystem basert på antall fiender beseiret og overlevelsestid.
5. Enkel oppgraderingsmekanikk for karakterens ferdigheter eller våpen.
6. Et brukergrensesnitt (UI) som viser spillerens helse, poengsum, og nåværende våpen/ferdigheter.
7. En enkel "game over"-mekanikk med mulighet til å restarte spillet, samt en main menu.
8. Et utvalg av karakterer som brukeren kan velge å spille som.

# En liste over brukerhistorier
*brukerhistorie 1:* 
- Som en spiller ønsker jeg å kunne bevege karakteren min, slik at jeg kan unngå fiender og samle ressurser.
- Akseptansekriterier: Karakteren kan bevege seg i alle retninger. Bevegelse er intuitiv og responsiv.
- Arbeidsoppgaver: Implementere karakterkontroller, teste på forskjellige enheter.

*Brukerhistorie 2:* 
- Som en spiller ønsker jeg å kunne angripe fiender, slik at jeg kan forsvare meg og tjene poeng.
- Akseptansekriterier: Karakteren har et grunnleggende angrep som kan brukes mot fiender. Fiender reagerer på angrep og kan bli beseiret.
- Arbeidsoppgaver: Designe og implementere angrepsmekanikk, opprette fiendens helse- og skadesystem.

*Brukerhistorie 3:*
- Som en spiller ønsker jeg å kunne oppgradere mine ferdigheter, slik at jeg kan overleve lengre og oppnå høyere poengsum.
- Akseptansekriterier: Spilleren kan velge oppgraderinger etter visse kriterier er oppfylt (f.eks. antall beseirede fiender).
- Arbeidsoppgaver: Utvikle oppgraderingssystem, integrere med spillets poengsystem.

# Prioritert liste over brukerhistoriene
1. Brukerhistorie 1: Karakterbevegelse - Dette er grunnlaget for spillopplevelsen og må prioriteres høyt.
2. Brukerhistorie 2: Angrepsmekanikk - Nødvendig for å skape interaksjon med spillet og introdusere utfordringer.
3. Brukerhistorie 3: Oppgraderinger - For å legge til dybde og progresjon i spillet, men dette kan startes etter at de to første historiene er på plass.

---
# A4
Alle på teamet har prøvd ut rammeverket libGDX, og laget libGDX's tutorial "simple game" Drop 
(https://libgdx.com/wiki/start/a-simple-game), samt en egen mutasjon som ligner mer på modellen Vampire Survivor.

---
# A5
Det som gikk bra:
- Enighet om at rammeverket libGDX er hensiktsmessig
- Enighet om spillvisjon, alle synes ideen er god
- God kommunikasjon i teamet  - vi lytter til hverandre og lar alle komme til ordet
- Alle har møtt til satt tidspunkt,evnt gitt beskjed dersom vedkommende ikke kunne stille fysisk

Det vi må jobbe med:
- Sette oss inn i Git-funksjonalitet
- Problemer med forståelse av push, pull, merge og andre Git-begreper

Inndeling av teamroller
- Ikke alle har klare preferanser/ferdigheter innen spesifikke områder
- Vi er tidlig i forløpet, ikke så lett å vite hvilke roller som kreves
- Er ikke like åpenbart å skjønne funskjonen til alle rollene