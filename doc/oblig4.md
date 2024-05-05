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

Prosjektet har vært en lærerik opplevelse. Vi kjente en god kjemi innad i gruppen helt fra starten av, og det virket som om alle var motitverte for å lage et solid sluttprodukt. Vi ble fort enige om at et spill inpirert av Vampire Surivors virket både gøy og oppnåelig.

I startfasen var arbeidet veldig upresist. Vi var flinke til å møte opp på fysiske møter, men ustrukturert kommunikasjon og vage retningslinjer førte til at disse møtene ble ganske ineffektive. Vi var lite aktive på discord, og vi var dårlige på å dele med resten av gruppen om hva den enkelte jobbet med. Vi møtte da opp på gruppene uten noen sans for hva som hadde blitt gjort siden sist, og hva som burde gjøres til neste gang.

Det tok ikke lang tid før det stakk seg ut enkelte medlemmer i gruppen som viste både et engasjement og arbeidsvilje på et ganske annet nivå enn resten. Avgjørelsene deretter føltes aldri ut som de ble gjort i felleskap , men mer av individuelle stemmer som ledet an og en noe passiv gruppe som fulgte etter. I tillegg skjønte gruppen fort at særlig ett individ skilte seg ut med en overlegen faglig kompetanse. Denne personen satte spillets rammeverk og struktur helt fra starten av, og i retrospekt var det nok litt unødvendig avansert med tanke på spillets visjon. Enkelte satte seg fort inn i denne mer avanserte kodestilen og kunne bidra selvstendig, men dessverre virket som at andre i gruppen datt litt av lasset allerede fra starten av, og kom seg aldri helt opp igjen.

Dette sporet fortsatte bare videre i prosjektperioden. Kontrastene i gruppen bare vokste, og dette ble veldig tydelig i gruppedynamikken. De fysiske møtene besto etter hvert av et par stykker som diskuterte implementasjoner og videre planlegging, mens resten hørte på. Dette var naturligvis ganske ugunstig med tanke på faglig læringsutbytte. Her ligger imidlertid ansvaret hos begge parter. De dominerende stemmene kunne hatt fellesskapet mer i fokus og latt andre slippe mer til, selv om det kunne bety å ofre mer "optimal" kode. Samtidig var det mangel på initiativ og lærelyst hos den andre parten.

Det betyr selvsagt ikke at ikke alle har bidratt. Det har blitt utført mye tidkrevende arbeid i form av UI og visuell design, men som ikke nødvendigvis gjenspeiles i antall linjer med kode eller commits. Likevel er dette en veldig viktig del av helhetsinntrykket.

Resultatet er likevel en ganske åpenbar skeivfordeling i hva den enkelte har bidratt med. Vi har hatt flere måneder på å snu denne utviklingen, men det har virket som hvert enkelt medlem  rett og slett har vært ganske tilfreds med slik situasjonen har utartet seg.

Sluttproduktet er vi imidlertid fornøyd med. Spillet flyter godt uten lagg, og strukturen gjør det lett å eskalere og legge til objekter og funksjonalitet.  Vi skulle nok ønske at vi fikk mer tid til å legge til flere kule funksjoner, levler etc, for å skape et enda mer gjennomført produkt.



