# Rapport – innlevering 3
**Team:** VirtuellVikings – Hallvard, Ingmar, Eilif, Andreas, Haakon

# Møtereferat

### Torsdag 14.03
Tilstede: Haakon, Andreas, Ingmar, Eilif

Under møtet:
Diskuterte fremtidige gjøremål

til neste gang:
Ingmar: box2d Compatible enemies
Andreas: fullføre meny med funksjon
Hallvard / Eilif: oppdatere Player klassen, lage Weapon klasse

### Torsdag 21.03
Tilstede: Andreas, Eilif, Ingmar, Haakon, Hallvard

under møtet:
oppsummering av forrige ukes gjøremål, diskuterte map layout, og ønsker til map/player funksjonalitet.

til neste gang:
Andreas: player info UI
Ingmar: map sprites
Eilif: weapon klasse
Hallvard: enemy/weapon sprites
Haakon: parsing av tekst filer

### Tirsdag 9.04
Tilstede: Eilif, Haakon, Ingmar, Hallvard

under møtet:
la plan for å komme i mål med oblig3
merging av branches, og se hvordan early prototype candidate project ser ut

til neste gang:
fullføre prototypen til oblig3

### Torsdag 11.04
Tilstede: Hallvard, Ingmar, Eilif, Haakon

under møtet:
merge inn det som mangler for å gjøre koden klar til oblig3
fordele skriveoppgaver til oblig3

# Prosjektrapport

Hvordan fungerer rollene i teamet?/Trenger dere andre roller?
* ingen endring i rollefordeling.

Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
* dårlig kommunikasjon fører til overlapping i arbeid
* oppgavefordelingene som blir holdt hvert møte funker bra som en plan, men teamet har tendens til å svaie litt fra oppgavene som ble distribuert

Hvordan er gruppedynamikken? Er det uenigheter som bør løses?
* gruppedynamikken er grei, men litt ubalansert.
* uenigheter skjer fra tid til annen oftest anngående programmerings logikk, som blir løst via diskurs.

Hvordan fungerer kommunikasjon for dere?
* kommunikasjon på møtene er veldig bra, problemene er med kommunikasjon via discord, hvor ikke alle oppdaterer hva de jobber med, hvor langt de er kommet og om det er oppstått problemer.

### Retrospekt

Hva har vi klart?
* ukentlige fysiske oppmøter
* bra dialog og veiledgning under møtene

Ting som må forbedres?
* kommunikasjon er noe manglende forutenom de fysiske møtene
* ikke alle er like flinke til å skrive meningsfulle commits
* teammedlemmene har noe problemer med å bryte ut av rollefordelingen

### Forbedringspunkt
Kommunikasjon:
* teammedlemmer *må* bli bedre til å bruke discord-kanalen for å melde fra om arbeid og oppmøte
  Prosjektfordeling:
* teamet må bli bedre til å bidra jevnt, og ikke bli fastlåst i rollefordelingene


# Krav og spesifikasjon:
Hva har vi prioritert?

Vi har prioritert å lage en implementasjon av hvordan ulike enemies 
skal spawne, da dette er en ganske sentral del av spillet og lagt til flere enemies i tillegg til sprites for de.
Vi har finpusset phsysics og kollisjon både mellom spilleren og enemies, og mellom våpen og enemies. Fysikken 
vi har implementert er en direkte forbedring fra forrige innlevering. 
Vi har også laget et map med terrain som per nå spawner ulike objecter tilfeldig.
Spillet vårt er nå kommet forbi MVP prototypen og vi har lagt til flere features som gjør spillet mer komplett.
Man kan nå angripe og drepe enemies og faktisk dø selv. 

Prioriteringer fremover:

Vi er nødt til å lage en logikk for når og hvor mange enemies skal spawne. 
Det samme gjelder for terrain objekter, da dette per nå spawnes random. Vi 
skal også prioritere et pickup system som gir spilleren mulighet til å tjene
opp XP og få "level ups", for å "unlocke" nye våpen og abilities.
Dette må også legges inn i UIen.


Ble resultatet som forventet?

Kollosjonsfysikken fungerer som den skal 
og man kan nå angripe/ forsvare seg selv samt å dø. 
Når man dør kommer det en game over skjerm og spillet stoppes. 
Sprites for flere nye enemies er også laget og spawningen deres som vi kan
kontrollere som vi vil. UI-en og diverse menyer er ikke helt ferdig men det nærmer seg ferdig produkt. 
Med disse tingene på plass er vi fornøyde med resultatet og man kan si at 
det alt i alt har gått som forventet.

Bugs? 

En bug vi sto ovenfor var at da enemies spawnet oppsto det en del lag.
Dette fikk vi fikset opp i ved å endre på hvordan enemies spawner og ved 
å fjerne en del unødvendig kode. Igjen for å unngå bugs i fremtiden er vi klar 
over at det er viktig med gode tester og oversiktlig kode slik at det er 
lettere å debugge.


