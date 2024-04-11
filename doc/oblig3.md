# Rapport – innlevering 3
**Team:** VirtuellVikings – Hallvard, Ingmar, Eilif, Andreas, Håkon

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

