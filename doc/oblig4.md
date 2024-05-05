# Rapport – innlevering 4
**Team:** VirtuellVikings – Hallvard, Ingmar, Eilif, Andreas, Haakon

**ProsjektRapport**

Teamroller:

Det er ikke skjedd en endring i komposisjonen.

Team lead har vært god på å sette opp møtested og møtetid, men avgjørelser anngående selve prosjektet har blitt tatt av andre.

Tech lead har laget meste av koden og logikken til prosjektet, noe som har ført til at medlemmer har hatt noe problemer med å skrive kode som samsvarer med logikken.

Teamet har to fullstack-devs som har bidratt noe ulikt, og teamet skulle gjerne sett til at teammedlemmer med like roller bidrar likt.

Graphics designer har stått som hovedansvarlig ovenfor sprites.

UX designer har laget main menu.

Prosjektmetodikk:

Teamet er enig om at prosjektet har blitt litt for komplisert, og det har vært vanskelig å kunne bidra med kode.

Hadde vi gjort dette fra begynnelsen av, ville teamet ha avklart et kompleksitetsnivå hvor alle kan delta forholdsvis likt.

Gruppedynamikk:

Dynamikken er ubalansert, da det er medlemmer med ulik ekspertise og ulik interesse for prosjektet. På den ene siden er det noen
medlemmer som viser stor entusiasme ovenfor prosjektet, mens på den andre siden, har vi medlemmer som ikke virker interessert i gruppen i det hele tatt.

Uenigheter i gruppen er koderelatert, og oppstår ofte.

Kommunikasjon:

Kommunikasjon fungerer noe bedre enn tidligere, men fremdeles veldig dårlig.

Lite tilbakemelding på discord-serveren, uklarhet om hva de andre holder på med før det er ferdig implementert.

Prosjektstruktur:

til nå har vi klart å:
- møte ukentlig
- skrive referat under hvert møte
- planlegge og fordele arbeidsoppgaver
- løse uenigheter

Commits:

Det er ofte mye som blir commitet på de fysiske gruppetimene av en person, da tech lead hjelper å setter sammen og fikser bugs/errors på samme maskin.

Retrospekt:

Hva har gruppen gjort bra?
- klart å lage et spill, hvor hvert medlem har bidratt med noe.
- skapt et positivt arbeidsmiljø
- laget ukentlig slagplan (endog dårlig oppfølging)

Gjort annerledes:
- partial milestones som teamet kunne nådd
- holdt koden til en kompleksitetsnivå som alle forstår
- fordele oppgaver inn i mindre biter, og bedre oppfølging av oppgavene
- presise commits

# møtereferat
## 18.04
Tilstede: Hallvard, Ingmar, Eilif, Andreas, Haakon

Under møtet ble det diskutert hva som må gjenstår før produktet er ferdig.
- pickup items?
- map object spawn
- leveling
    - enemy xp
    - player levelup
- implementere ppm
- factory uten hardcoding
Oppgaver ble fordelt til neste gang
## 25.04
Tilstede: Hallvard, Eilif, Andreas, Haakon

Under møtet ble det diskutert hvordan teamet ligger ann i forhold til kravene for det ferdige produktet.
- lyd
- levelup
- keybindings
- tests
- javadoc
- sources
## 30.04
Tilstede: Hallvard, Eilif, Ingmar, Haakon

Under møtet ble det diskutert krav som ikke er ferdigimplementert
- soundeffect
- flere tester
- javadoc
- sources
- player levelup scaling -> høyere lvl = høyere dmg
- pickups
- map spawn timer
## 02.05
Tilstede: Hallvard, Eilif, Ingmar, Haakon, Andreas

Under møtet ble kode satt sammen, slik at prosjektet skal likne et sluttresultat.


**Krav og spesifikasjon**

Prioriteringslisten har endret seg litt underveis som vi har oppdaget at enkelte ting er vanskeligere å implementere enn annet. Vi har for eksempel ikke et like avansert level-up system som vi kanskje hadde sett for oss da vi begynnte.
Slike endringer må man være forberedt på i et stort gruppeprojekt som dette, og vi synes ikke det har vært noe stort hinder.

Når det kommer til brukerhistoriene som ble laget i starten, så har vi oppfylt dette nå.
Brukerhistorie 1: Karakterbevegelse, Brukerhistorie 2: Angrepsmekanikk og Brukerhistorie 3: Oppgraderinger er alle implementert i spillet.

I form av akseptanskriterier så har vi laget en spiller som kan bevege seg fritt på brettet, en spiller som kan angripe fiender og en spiller som kan oppgradere ferdigheter, samt en rekke med tester for diverse av spill logikken.

Vi hadde en god fordeling av arbeidsoppgavene knyttet til de satte kravene, men etterhvert som ting ble mer avansert og folk har forskjellig erfaring/ferdighetsnivå, har noen gjort mere enn andre. Hvis vi kunne gjort dette igjen, burde vi vært strengere på hvem som skal gjøre hva. 
Dette hadde vært en god forbedring for å unngå at noen faller litt bak mens andre gjør for mye. 


**Retrospektiv: Hvordan har prosjektet gått?**


Prosjektet har vært en lærerik opplevelse. Vi startet med et godt utgangspunkt og god gruppekjemi, og alle var motiverte til å jobbe hardt. Vi ble fort enige om at et spill inpirert av Vampire Surivors virket både gøy og oppnåelig. 

I begynnelsen var arbeidet litt upresist. Mangel på erfaring med prosjektarbeid førte til ustrukturert kommunikasjon og vage retningslinjer, som resulterte i noen ineffektive møter.

Som tiden gikk  opplevde vi bedre noe bedre flyt og engasjement, etterhvert som vi ble mer kjent med de konkrete verktøyene og struktuerene som var nødvendig for å lage spillet. Dette gjorde det lettere å ta ansvar for våre roller.

Fordelingen av arbeidsoppgaver var naturligvis utfordrende, da det var variasjon i timeplaner, faglig kunnskap og engasjement. Likevel følte alle seg involvert og bidro på sin måte.

Vi er tilfredse med sluttresultatet. Selv om det var mange endringer underveis, landet vi på et spill vi er stolte av. Rammeverket og strukturen gjør det veldig lett å eskalere spillet, og vi skulle ønsket vi fikk litt bedre tid til å legge til flere kule funksjoner og spillobjekter.

Vi erkjenner at det er ting vi kunne gjort annerledes. Vi burde ha vært tydeligere med oppgaver og bedre med dokumentasjon og commits. Dette ville unngått overlapping i arbeidet.

Det har også vært en utfordring å finne balansen mellom god og konsis kode, samt respektere ulike skrivestiler. Vi måtte unngå å bli for rigid med en bestemt kodepraksis og heller la hver enkelt få uttrykke seg innenfor funksjonelle rammer.


