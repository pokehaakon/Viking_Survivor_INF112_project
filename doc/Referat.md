Referat - Ingmar

torsdag 15.2
Tilstede: Andreas, Carl, Ingmar, Hallvard, Haakon, Eilif

Under møtet:
Teamet hadde landet på en variant av Vampire Survivors. Grunnen til dette var  at spillfysikken er lett å skjønne og relativt overkommelig å implementere, og det er forholdsvis lett å eskalere/nedskalere ved å legge til eller fjerne funksjonalitet.

Det ble foreslått vikingtema, og foreløpig tittel ble Viking Survivors.

Videre samarbeidet teamet med siste finpuss av del A. I denne prosessen ble det også diskutert GIT-funksjonalitet. Det ble bestemt at hvert medlem lager sin egen branch der de kan implementere egen kode, før vi merger til en felles branch når tiden er inne.

Det ble reflektert over utfordringen om å skape sine egne implementasjoner som skal samhandle gjennom et felles rammeverk og med deler av spillet som man ikke har kjennskap til enda. I tillegg snakket teamet om utfordringen rundt det å sørge for at alle bidrar noenlunde likt, når vi er såpass mange.

Det ble bestemt at spillet skulle utvikles i libGDX.

I tillegg ble det også fordelt følgende foreløpige roller:

Team Lead/Grafisk design: Hallvard
Team Lead ble valgt gjennom loddtrekning. Ingen hadde et stort ønske om være leder, men vi var enige om at dette likevel er nødvendig for å knytte teamet sammen.
Tech Lead: Haakon
Haakon har mest erfaring og kunnskap innen informatikk, derfor var dette naturlig.
UX design: Andreas
Andreas var interessert i å blant annet utvikle UI og menyer, derfor fikk han et spesielt ansvar på dette punktet.
Fullstack dev: Ingmar, Eilif og Carl
Ingmar, Eilif og Carl hadde ingen spesifikke ønsker eller preferanser, derfor er deres rolle en mer generell og allsidig utviklerrolle.

Hjemmelekse til neste møte: bli kjent med rammeverket i libGdx.
Plan for neste møte: starte på et klassediagram.

torsdag 22. 2
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



fredag 23.02
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





torsdag 29.02
Tilstede: Andreas, Carl, Ingmar, Hallvard, Eilif

Under møtet:
Medlemmene viste fram det de hadde jobbet på siden forrige gang.

Ingmar:

Viste fram en skisse til en EnemyFactory.  Denne klasser tar Enum enemyType og ønsket antall som input, og produserer instanser av Enemy-objekter på et random sted utenfor skjermen. I tillegg kan den produsere Swarms; hærer av enemies som beveger seg som en unit.

Det lagget litt når Swarmene spawner. Det ble blant annet diskutert hvorvidt det å blant annet redusere antall mulige spawn points kan gjøre at iterasjonen går raskere. Ingmar poengterte at det er mye som kan forbedres, og at dette bare er en skisse.

Andreas:
Viste fram et forslag til en meny. Det var bred enighet om at menyen var estetisk vakker og passer bra med spillets tema.

Eilif:
Eilif hadde satt seg inn i config, men teamet ble enige om at det er for tidlig å sette seg inn i og bruke tid på dette på et så tidlig stadium av prosjektet. Eilif fikk da i oppgave å starte på en Playerklasse som har en form angrepsfunksjon.

Hallvard:
Viste fram forslag til spritedesign for Player og diverse enemies. Han hadde utviklet en GIF-dekoder så man kan representere spritesheets som GIFs. Teamet var enige om å  fortsette å benytte seg av denne løsningen.

Hjemmelekse til neste møte: fortsette på de respektive klassene.

Plan for neste møte: merge branchene til en felles branch, og sette sammen et MVP.


tirsdag 05.03
Tilstede: Ingmar, Hallvard, Haakon, Eilif

Under møtet:
Carl har meldt seg av studiet, og gruppen er i stor sorg. Det ble imidlertid nevnt at siden vi er i såpass tidlig i prosjektfasen medfører ikke dette særlig til vanskeligheter, og at det til og med kan bli lettere å fordele arbeidsoppgaver med 1 stk færre i teamet.

Planen om å merge branches til en felles branch i et MVP ble ikke overholdt. Grunnen er at flere medlemmer ikke hadde gjort seg ferdige med sine respektive arbeidsoppgaver, og at vi trengte litt mer tid. Det ble imidlertid snakket om at de ulike klassene ikke trenger å være optimale, men at det viktigste nå er at alle får bidratt med noe.

Hjemmelekse til neste gang: gjøre klare sine respektive klasser slik at de er klare til merging. Haakon ønsket å utvikle collision handling, både mellom player og enemy og mellom enemies.

Plan for neste gang: sette sammen et MVP bestående av en player som kan bevege seg og  angripe/skyte og enemies som beveger seg mot player.
