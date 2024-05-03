# Rapport – innlevering 4
**Team:** VirtuellVikings – Hallvard, Ingmar, Eilif, Andreas, Haakon

**ProsjektRapport**

Teamroller:
Det er ikke skjedd en endring i komposisjonen.
Team lead har vært god på å sette opp møtested og møtetid, men avgjørelser anngående selve prosjektet har blitt tatt av andre.
Tech lead har laget meste av koden og logikken til prosjektet, noe som har ført til at medlemmer har hatt noe problemer med å skrive kode som samsvarer med logikken.
teamet har to fullstack-devs som har bidratt noe ulikt, og teamet skulle gjerne sett til at teammedlemmer med like roller bidrar likt
Graphics designer har stått som hovedansvarlig ovenfor sprites
UX designer har laget main menu.

prosjektmetodikk:
teamet er enig om at prosjektet har blitt litt for komplisert, og det har vært vanskelig å kunne bidra med kode.
Hadde vi gjort dette fra begynnelsen av, ville teamet ha avklart et kompleksitetsnivå hvor alle kan delta forholdsvis likt.

gruppedynamikk:
dynamikken er ubalansert, da det er medlemmer med ulik ekspertise og ulik interesse for prosjektet. På den ene siden er det noen
medlemmer som viser stor entusiasme ovenfor prosjektet, mens på den andre siden, har vi medlemmer som ikke virker interessert i gruppen i det hele tatt.
uenigheter i gruppen er koderelatert, og oppstår ofte.

kommunikasjon:
kommunikasjon fungerer noe bedre enn tidligere, men fremdeles veldig dårlig.
lite tilbakemelding på discord-serveren, uklarhet om hva de andre holder på med før det er ferdig implementert.

prosjektstruktur:
til nå har vi klart å:
- møte ukentlig
- skrive referat under hvert møte
- planlegge og fordele arbeidsoppgaver
- løse uenigheter

commits:
det er ofte mye som blir commitet på de fysiske gruppetimene av en person, da tech lead hjelper å setter sammen og fikser bugs/errors på samme maskin.

retrospekt:
hva har gruppen gjort bra?
- klart å lage et spill, hvor hvert medlem har bidratt med noe.
- skapt et positivt arbeidsmiljø
- laget ukentlig slagplan (endog dårlig oppfølging)
gjort annerledes:
- partial milestones som teamet kunne nådd
- holdt koden til en kompleksitetsnivå som alle forstår
- fordele oppgaver inn i mindre biter, og bedre oppfølging av oppgavene
- presise commits

# møtereferat
## 18.04
Tilstede: Hallvard, Ingmar, Eilif, Andreas, Haakon
Under møtet ble det diskutert hva som må gjenstår før produktet er ferdig.
-pickup items?
-map object spawn
-leveling
    - enemy xp
    - player levelup
-implementere ppm
-factory uten hardcoding
Oppgaver ble fordelt til neste gang
## 25.04
Tilstede: Hallvard, Eilif, Andreas, Haakon
under møtet ble det diskutert hvordan teamet ligger ann i forhold til kravene for det ferdige produktet.
-lyd
-levelup
-keybindings
-tests
-javadoc
-sources
## 30.04
Tilstede: Hallvard, Eilif, Ingmar, Haakon
Under møtet ble det diskutert krav som ikke er ferdigimplementert
-soundeffect
-flere tester
-javadoc
-sources
-player levelup scaling -> høyere lvl = høyere dmg
-pickups
-map spawn timer
## 02.05
Tilstede: Hallvard, Eilif, Ingmar, Haakon, Andreas
under møtet ble kode satt sammen, slik at prosjektet skal likne et sluttresultat.


**Krav og spesifikasjon**

Prioriteringslisten har endret seg litt underveis som vi har oppdaget at enkelte ting er vanskeligere å implementere enn annet. Vi har for eksempel ikke et like avansert level-up system som vi kanskje hadde sett for oss da vi begynnte.
Slike endringer må man være forberedt på i et stort gruppeprojekt som dette, og vi synes ikke det har vært noe stort hinder.

Når det kommer til brukerhistoriene som ble laget i starten, så har vi oppfylt dette nå.
Brukerhistorie 1: Karakterbevegelse, Brukerhistorie 2: Angrepsmekanikk og Brukerhistorie 3: Oppgraderinger er alle implementert i spillet.

I form av akseptanskriterier så har vi laget en spiller som kan bevege seg fritt på brettet, en spiller som kan angripe fiender og en spiller som kan oppgradere ferdigheter, samt en rekke med tester for diverse av spill logikken.

Vi hadde en god fordeling av arbeidsoppgavene knyttet til de satte kravene, men etterhvert som ting ble mer avansert og folk har forskjellig erfaring/ferdighetsnivå, har noen gjort mere enn andre. Hvis vi kunne gjort dette igjen, burde vi vært strengere på hvem som skal gjøre hva. 
Dette hadde vært en god forbedring for å unngå at noen faller litt bak mens andre gjør for mye. 
