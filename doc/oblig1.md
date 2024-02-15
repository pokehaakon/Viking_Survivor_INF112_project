# Rapport – innlevering 1
<<<<<<<<< Temporary merge branch 1

A1:
Teamnavn: virtualviking
Gruppenummer: 6
Prosjektnavn: Viking Survivors

Beskrivelse:
2D roguelite spill...
Styrer viking/annet, prøve å overleve lengst mulig
Få våpen og powerups underveis
Fiendene blir gradvis vanskeligere/mer avansert
Går opp levler underveis

Kontroller:
Styrer med piltaster/WASD. Skyter med space. Bruker mus til diverse annet.

A2
* Spillerfigur som kan styres sett ovenfra, Kan bevege seg i alle retninger.--
* Todimensjonal verden:
    - Arena – området/brett hvor spilleren kan bevege seg.
    - Avgrensning – vegg i enden av brettet på alle kanter hvor spiller ikke kan bevege seg utenfor.
* Fiender som beveger seg mot spiller og er skadelig ved berøring.
* Spilleren skyter tilbake for å ødelegge fiender.
* Spilleren kan samle xp points som fiender legger fra seg når de dør. Når spiller har samlet nok av disse vil det
  bli presentert et valg mellom 3 power-ups.
* Utfordringen i spillet er å overleve mer og mer utfordrende bølger av angrep fra fiender fram til tiden går ut.



A3-1
Prosjekt-organiseringen følger **scrum** modellen
"daily" standups 2-3 ganer i uken på discord, 1 sprint review i uken som er fysisk
kommunikasjon mellom møter skjer på gruppens discord "Vampyre Survivors"

**Arbeidsfordeling**:
    team lead velger arbeid fra bracklog, og distribuerer for sprint (en sprint er en 1 ukers tidsramme hvor teamet 
fokuserer på arbeidet gitt fra team lead, eks. uke 11 "enemy_type_bat implementering", hvor det videre deles opp
i arbeidet som ligger til grunn for å kunne implementere en flaggermus fiende. eks. sprite, testing, 
characteristics(eks. abilities, movement, drops), sound) til scrum devs. Høyt fokus på fleksibilitet, da det ikke er 
ønskelig at en scrum dev jobber 80% med graphics, og 20% med koding hver eneste sprint. code/test reviews vil bli 
gjort underveis i sprinten via standups. (evt "parprogrammering", team lead velger backlog arbeid og gir 
arbeidet gruppevis, slik at 2 og 2 jobber på de ulike arbeidsoppgavene sammen, sikrer konstant code/test reviewing)

**Oppfølging av arbeid**:
    oppfølgingen skjer via "daily" standups, hvor progresjon eller oppståtte problemer blir rapportert, evt. forslag 
til mulige endringer blir lagt frem. 1 møte fysisk ukentlig for sprint review, hvor det sees på hva som er blitt gjort,
hva som må gjøres, evt. om noe tar lenger tid enn forventet. også feedback på ting som er gjort bra. Her er det fokus
på å skape et miljø hvor man ikke er redd for å be om hjelp, da "dårlig kode" ikke er noe annet enn en mulighet for å
lære. (fokus på miljø blablabla, trenger en solid HR beskrivelse av arbeidsmiljøfokus)

Møtereferater etter hvert "daily standup" og sprint review.
Møteleder -> referant -> 
Google docs for møtereferat
codebase -> gitlab
Google docs for sprint backlog
project board -> ?
=========
# Oppgave A3, få oversikt over forventet produkt

# En kort beskrivelse av det overordnede målet for applikasjonen
Målet med vår applikasjon, Viking survivors, 
er å tilby en engasjerende og underholdende spillopplevelse 
som kombinerer elementer av overlevelse og progresjon i en 
norrøn-inspirert verden. Spilleren vil kontrollere en 
karakter som må overleve mot stadig sterkere bølger 
av fiender, samtidig som de samler ressurser og oppgraderer 
ferdigheter. Målet er å overleve så lenge som mulig 
og oppnå høyest mulig poengsum.


# MVP - Minimum viable product:
MVP i prioritert rekkefølge
1. Vise et spillebrett og spilleren
2. Kunne bevege spilleren fritt på en begrenset bane.
3. Et utvalg av grunnleggende fiender som angriper spilleren i bølger.
4. Et poengsystem basert på antall fiender beseiret og overlevelsestid.
5. Enkel oppgraderingsmekanikk for karakterens ferdigheter eller våpen.
6. Et brukergrensesnitt (UI) som viser spillerens helse, poengsum, og nåværende våpen/ferdigheter.
7. En enkel "game over"-mekanikk med mulighet til å restarte spillet, samt en main menu.
8. Et utvalg av karakterer som brukeren kan velge å spille som.

# En liste over brukerhistorier
brukerhistorie 1: 
- Som en spiller ønsker jeg å kunne bevege karakteren min, slik at jeg kan unngå fiender og samle ressurser.
- Akseptansekriterier: Karakteren kan bevege seg i alle retninger. Bevegelse er intuitiv og responsiv.
- Arbeidsoppgaver: Implementere karakterkontroller, teste på forskjellige enheter.

Brukerhistorie 2: 
- Som en spiller ønsker jeg å kunne angripe fiender, slik at jeg kan forsvare meg og tjene poeng.
- Akseptansekriterier: Karakteren har et grunnleggende angrep som kan brukes mot fiender. Fiender reagerer på angrep og kan bli beseiret.
- Arbeidsoppgaver: Designe og implementere angrepsmekanikk, opprette fiendens helse- og skadesystem.

Brukerhistorie 3: 
- Som en spiller ønsker jeg å kunne oppgradere mine ferdigheter, slik at jeg kan overleve lengre og oppnå høyere poengsum.
- Akseptansekriterier: Spilleren kan velge oppgraderinger etter visse kriterier er oppfylt (f.eks. antall beseirede fiender).
- Arbeidsoppgaver: Utvikle oppgraderingssystem, integrere med spillets poengsystem.

# Prioritert liste over brukerhistoriene
1. Brukerhistorie 1: Karakterbevegelse - Dette er grunnlaget for spillopplevelsen og må prioriteres høyt.
2. Brukerhistorie 2: Angrepsmekanikk - Nødvendig for å skape interaksjon med spillet og introdusere utfordringer.
3. Brukerhistorie 3: Oppgraderinger - For å legge til dybde og progresjon i spillet, men dette kan startes etter at de to første historiene er på plass.
>>>>>>>>> Temporary merge branch 2
