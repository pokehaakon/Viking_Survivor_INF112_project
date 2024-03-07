# Rapport – innlevering 2

# Møterefereater


### torsdag 15.2

Tilstede: Andreas, Carl, Ingmar, Hallvard, Haakon, Eilif

Under møtet:
Teamet hadde landet på en variant av Vampire Survivors. Grunnen til dette var  at spillfysikken er lett å skjønne og relativt overkommelig å implementere, og det er forholdsvis lett å eskalere/nedskalere ved å legge til eller fjerne funksjonalitet.

Det ble foreslått vikingtema, og foreløpig tittel ble Viking Survivors.

Videre samarbeidet teamet med siste finpuss av del A. I denne prosessen ble det også diskutert GIT-funksjonalitet. Det ble bestemt at hvert medlem lager sin egen branch der de kan implementere egen kode, før vi merger til en felles branch når tiden er inne.

Det ble reflektert over utfordringen om å skape sine egne implementasjoner som skal samhandle gjennom et felles rammeverk og med deler av spillet som man ikke har kjennskap til enda. I tillegg snakket teamet om utfordringen rundt det å sørge for at alle bidrar noenlunde likt, når vi er såpass mange.

Det ble bestemt at spillet skulle utvikles i libGDX.

I tillegg ble det også fordelt følgende foreløpige roller:

- Team Lead/Grafisk design: Hallvard
Team Lead ble valgt gjennom loddtrekning. Ingen hadde et stort ønske om være leder, men vi var enige om at dette likevel er nødvendig for å knytte teamet sammen.
- Tech Lead: Haakon
Haakon har mest erfaring og kunnskap innen informatikk, derfor var dette naturlig.

- UX design: Andreas
Andreas var interessert i å blant annet utvikle UI og menyer, derfor fikk han et spesielt ansvar på dette punktet.

- Fullstack dev: Ingmar, Eilif og Carl
Ingmar, Eilif og Carl hadde ingen spesifikke ønsker eller preferanser, derfor er deres rolle en mer generell og allsidig utviklerrolle.

Hjemmelekse til neste møte: bli kjent med rammeverket i libGdx.

Plan for neste møte: starte på et klassediagram.

### torsdag 22. 2

Tilstede: Andreas, Carl, Ingmar, Hallvard, Haakon

Under møtet:
Teamet lagde en liste over klasser/interfacer som kan være relevant i et Vampire Survivor-inspirert spill.
Noen av klassene som ble diskutert var:

Interfacet IGameObject: inneholder metoder felles for alle spillobjekter, som draw(), destroy(), update().
Actor: en abstrakt klasse som implementerer IGameObjekt som alle bevegelige spillobjekter skal utvide. Diskuterte metoder i denne klassen: move(), attack(), destroy(), draw().
Enemy: representer fiender, utvider Actor.
Player: representer fiender, utvider Actor.

Tech lead Haakon hadde utviklet et forslag til en prototype for et rammeverk med input processing og context managing. Her hadde han benyttet seg av Box2D, noe et fåtall av de andre medlemmene var kjent med.

Hjemmelekse til neste møte: Teamet skulle sette seg inn i rammeverket til Haakon og bli litt kjent med box2D.

Plan for neste møte: Fordele arbeidsoppgaver iht til klassediagrammet..



### fredag 23.02
Tilstede: Andreas, Carl, Ingmar, Hallvard, Haakon, Eilif

Under møtet:
Haakon ga en liten intro til rammeverket sitt på storskjerm.

Videre følte gruppen at de trengte noe konkret å gjøre til neste gang, derfor fikk alle spesifikke arbeidsoppgaver.

Det ble fordelt følgende oppgaver til neste møte:

Ingmar: starte på en EnemyFactory
Hallvard: starte på animasjoner/sprites
Eilif: sette seg inn i config files og saves
Haakon: justere og rydde opp i egen kode/rammeverk
Andreas: meny/UI
Carl: Enemy-klasse



Hjemmelekse til neste møte: alle starter på en implementasjonsskisse av de fordelte oppgavene.

Plan for neste møte: teamet viser fram det de har gjort siden sist.


### torsdag 29.02
Tilstede: Andreas, Carl, Ingmar, Hallvard, Eilif

Under møtet:
Medlemmene viste fram det de hadde jobbet på siden forrige gang.

Ingmar:

Viste fram en skisse til en EnemyFactory.  Denne klasser tar Enum enemyType og ønsket antall som input, og produserer instanser av Enemy-objekter på et random sted utenfor skjermen. I tillegg kan den produsere Swarms; hærer av enemies som beveger seg som en unit.

Det lagget litt når Swarmene spawner. Det ble blant annet diskutert hvorvidt ulike måter å forhindre lag, feks å redusere antall mulige spawn points.Ingmar poengterte at det er mye som kan forbedres, og at dette bare er en skisse.

Andreas:
Viste fram et forslag til en meny. Det var bred enighet om at menyen var estetisk vakker og passer bra med spillets tema.

Eilif:
Eilif hadde satt seg inn i config, men teamet ble enige om at det er for tidlig å sette seg inn i og bruke tid på dette på et så tidlig stadium av prosjektet. Eilif fikk da i oppgave å starte på en Playerklasse som har en form angrepsfunksjon.

Hallvard:
Viste fram forslag til spritedesign for Player og diverse enemies. Han hadde utviklet en GIF-dekoder så man kan representere spritesheets som GIFs. Teamet var enige om å  fortsette å benytte seg av denne løsningen.

Hjemmelekse til neste møte: fortsette på de respektive klassene.

Plan for neste møte: merge branchene til en felles branch, og sette sammen et MVP.


### tirsdag 05.03
Tilstede: Ingmar, Hallvard, Haakon, Eilif

Under møtet:
Carl har meldt seg av studiet, og gruppen er i stor sorg. Det ble imidlertid nevnt at siden vi er i såpass tidlig i prosjektfasen medfører ikke dette særlig til vanskeligheter, og at det til og med kan bli lettere å fordele arbeidsoppgaver med 1 stk færre i teamet.

Planen om å merge branches til en felles branch i et MVP ble ikke overholdt. Grunnen er at flere medlemmer ikke hadde gjort seg ferdige med sine respektive arbeidsoppgaver, og at vi trengte litt mer tid. Det ble imidlertid snakket om at de ulike klassene ikke trenger å være optimale, men at det viktigste nå er at alle får bidratt med noe.

Hjemmelekse til neste gang: gjøre klare sine respektive klasser slik at de er klare til merging. Haakon ønsket å utvikle collision handling, både mellom player og enemy og mellom enemies.

Plan for neste gang: sette sammen et MVP bestående av en player som kan bevege seg og  angripe/skyte og enemies som beveger seg mot player.

### tirsdag 07.03
Tilstede: Ingmar, Eilif, Haakon

Under møtet:
Merger Ingmar sin branch i Haakon sitt rammeverk. Justert klassene deretter. Endte opp med en en MVP der man har en player som kan beveges, og enemies som spawner gjennom en enemyfactory. Vi sørget for å få et MVP som kjører i main uten feil. Samarbeidet litt med det skriftlige arbeidet som skal inn til oblig2.


# ProsjektRapport

### Hvordan fungerer rollene?

Rollene fungerer ganske bra til nå. Selv om noen medlemmer har spesifikke ansvarsområder, er det inneforstått at de også bidrar til prosjektet i en mer generell utviklerrolle.

Hva rollene betyr for oss i dette prosjektet

Team Lead:

Team lead styrer den generelle kursen for prosjektet. Tar avgjørende beslutninger dersom det er uenigheter, og har et overordnet øye over hva som blir gjort.

Tech Lead:

Tech lead er gjerne den som har mest kunnskap/erfaring med programmering og rammeverket (libGDX). Han kan også stille som support dersom teammedlemmene trenger hjelp med kode. I tillegg har han en ekstra vektet stemme når det kommer til spillets logikk og struktur.

UX Designer:
Et ekstra ansvar for UI og at “user experiencen” faktisk tar hensyn til brukeren, og ikke bare utviklerne.

Graphics Design:
Sikrer kvaliteten på det visuelle, og et ekstra ansvar for spillets design.

Full stack Devs:
En allsidig rolle der man kan jobbe med de fleste aspekter av spillet.



### Kommunikasjon og gruppedynamikk

Til nå foregår kommunikasjon både via Discord  og fysiske møter. I Discord planlegger vi hvor og når møtene skal finne sted, og i de fysiske møtene diskuterer vi hva som har blitt gjort siden sist, og hva som bør gjøres til neste gang.  Kjemien innad i gruppen er bra, og det virker som om alle er villig til å legge inn nødvendig innsats for å levere et solid produkt. Inntrykket er at alle får komme til ordet og blir hørt.

### Retrospekt

Hva vi har klart

Til nå har teamet klart å dele seg inn i roller, og det er ganske klart for medlemmene hva de rollene innebærer. Vi har klart å opprettholde en rytme med minst ett møte i uka, og har god kommunikasjonsflyt på Discord. Vi har også blitt litt kjent med hverandre og opparbeidet god kjemi, og medlemmene har fått bidratt til prosjektets gang på hver sin måte. Vi holder på å utvikle et solid rammeverk for fysikkhåndtering, og har levert et MVP.

Ting som må forbedres

Et fåtall av oss har prosjekterfaring på denne skalaen tidligere, og det er utfordrende å sørge for at alle bidrar noenlunde jevnt. I tillegg er det ikke lett å skrive kode der du henviser til objekter som andre har implementert eller som kanskje ikke finnes enda. Her er tre forbedringspunkter for å hjelpe oss med det:


- Tydeligere oppgavefordeling. I starten var oppgavefordelingen noe vag og uklar, noe som har ført til litt mangel på produktivitet og visjon i kodingen. Framover må vi bli flinkere på å fordele mer spesifikke hjemmelekser, og ikke minst at alle medlemmene får sjansen til å si sin mening om funksjonalitet og struktur for de utdelte oppgavene.
- Testing. I startfasen er det mye eksperimentering på ulike implementasjoner, og da er det lett å slurve med testing.
- Javadoc. For at alle medlemmene skal skjønne andre sin kode, må vi bli flinkere på å forklare kodens funksjon.


# Krav og Spesifikasjoner

Hva har vi prioritiert?

Vi har fulgt prioriteringslisten vi lagde til del A om MVP, og gjennomført punkt 1-3. Prioriteringene  har da vært å først vise et spillebrett og en spiller. Deretter har vi implementert bevegelse og litt kollisjons fysikk. Det tredje steget vi gjorde var å lage fiender og utforsket/diskutert hvordan vi skal implementere forskjellige typer fiender i fremtiden. Vi har imidlertid prioritert å lage Sprites og UI ganske høyt fordi det både er gøy og gjør det lettere å forestille seg hvordan spillet kommer til å se ut.


Ble resultatet det vi hadde forventet da vi starta?

Både resultatene av disse implementeringene og prioritetslisten har gått ca som forventet. Vi har en spiller som kan bevege seg og fiender som spawner.  Vi har brukt mye tid på å lage et solid rammeverk. Vi hadde nok ikke forventet å prioritere UI og sprites på et så tidlig tidspunkt. Dette har vi gjort for å utføre arbeidsfordelingen på en god måte mtp at vi er ganske mange på teamet og at alle må ha reelle oppgaver, og dette gir mulighet til å bli kjent med rammeverket.

Bugs?
Vi har ikke stått ovenfor veldig mange bugs så tidlig i projektet vårt enda. For å unngå dette fremover derimot er vi klar over at det er viktig med gode tester og oversiktlig kode for å gjøre det lettere å debugge.


