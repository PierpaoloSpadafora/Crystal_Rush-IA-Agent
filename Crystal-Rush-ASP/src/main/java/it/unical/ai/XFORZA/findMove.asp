%   
%   row(0..14). col(0..29). 
%   cell(X,Y) :- row(X), col(Y). 
%   action(dig). action(place). action(move). action(deliver). action(none). 
%   item(none). item(gem). item(radar). item(mine). 
%   robot(0,blue, 4, 0, mine). robot(8,blue, 13, 1, none). robot(11,blue, 11, 7, none). 
%   teamColor(blue). 
%   itemAvailable(mine). itemAvailable(radar). itemAvailable(none).
%   
%   emptyGem(8,5). emptyGem(8,6). presentGem(8,7,1). 
%   emptyGem(8,8). emptyGem(8,9). emptyGem(9,5).
%   emptyGem(9,6). presentGem(9,7,2). presentGem(9,8,2). 
%   presentGem(9,9,2). emptyGem(10,5). presentGem(10,6,2). 
%   
%   allyRadar(10,7). allyMine(13,16).
%   enemyRadar(12,8). enemyMine(10,9)
%   
%   


%   ESEMPI DI ALCUNI DATI DI INPUT DI UNA SITUAZIONE IN GAME PER IL TEAM RED



















% guess delle mosse
numRow(Y) :- #count{X : row(X)} = Y.
numCol(Y) :- #count{X : col(X)} = Y.

itemRobot(ID,I) :- robot(ID,_,_,_,I), item(I), I!=none.
remainingRobots(ID, X, Y, A, I) :- robot(ID, X, Y, A, I), not itemRobot(ID,I).

{coords(ID, X, Y, Cost) : costOfNextMove(ID,X,Y,Cost)} = 1 :- robot(ID,_,_,_,_).



%   Vieta ad un robot di muoversi in una cella dove c'è una mina nemica
:- coords(_, X, Y, _), enemyMine(X,Y).

%   Sfavorisci i percorsi più lunghi
:~ coords(ID, X, Y, Cost). [Cost@5, ID, X, Y]



auxyPS(mine,placeMine). auxyPS(radar,placeRadar). auxyPS(gem,deliverGem).
{
    %   se il robot e l'obiettivo hanno coordinate diverse il robot si muove verso le coordinate obiettivo
    mossa(ID, X, Y, move, I) : coords(ID, X, Y, _), objectiveIs(ID,Xo,Yo,A), auxyPS(I,A), robot(ID,_,Xr,Yr,_), numCol(MY), R1=(Xr*MY)+Yr, R2=(Xo*MY)+Yo, R1!=R2; 

    %   Il robot piazza o consegna l'oggetto
    mossa(ID, X, Y, place, mine) : objectiveIs(ID,X,Y,placeMine), robot(ID,_,X,Y,mine);
    mossa(ID, X, Y, place, radar) : objectiveIs(ID,X,Y,placeRadar), robot(ID,_,X,Y,radar);
    mossa(ID, X, Y, deliver, gem) : objectiveIs(ID,X,Y,deliverGem),  robot(ID,_,X,Y,gem)
} = 1 :- itemRobot(ID,I).


auxySS(pickGem). auxySS(digEnemyRadar). auxySS(goReceiveItem).


{
    %   offre la possibilità al robot di non fare assolutamente nulla (molto svantaggiato dai weak constraint)
    mossa(ID, X, Y, none, none) : objectiveIs(ID,X,Y,goReceiveItem), robot(ID,_,X,Y,none);

    %   se il robot e l'obiettivo hanno coordinate diverse il robot si muove verso le coordinate obiettivo
    mossa(ID, X, Y, move, none) : coords(ID, X, Y, _), objectiveIs(ID,Xo,Yo,I), auxySS(I), robot(ID,_,Xr,Yr,none), numCol(MY), R1=(Xr*MY)+Yr, R2=(Xo*MY)+Yo, R1!=R2; 

    %   se l'obiettivo è quello di ricevere un item gli viene assegnata una mina o un radar, se disponibile
    mossa(ID, X, Y, none, radar) : objectiveIs(ID,X,Y,goReceiveItem), itemAvailable(radar), robot(ID,_,X,Y,none);
    mossa(ID, X, Y, none, mine) :  objectiveIs(ID,X,Y,goReceiveItem), itemAvailable(mine), robot(ID,_,X,Y,none);

    %   gli viene data la possibilità di prendere una gemma o distruggere un radar
    mossa(ID, X, Y, dig, none) : objectiveIs(ID,X,Y,pickGem), robot(ID,_,X,Y,none);
    mossa(ID, X, Y, dig, none) : objectiveIs(ID,X,Y,digEnemyRadar), robot(ID,_,X,Y,none)

} = 1 :- remainingRobots(ID, _, _, _, _).




:~ mossa(ID, X, Y, none, none). [20@5, ID, X, Y]  
:~ mossa(ID, X, Y, none, I). [10@5, ID, X, Y, I]  
:~ mossa(ID, X, Y, move, I), costOfNextMove(ID, X, Y, Cost). [Cost@5, ID, X, Y, I] 




% -------------- TROVARE SE ESISTE UN PATH VISIBILE DA DOVE SI TROVA IL ROBOT A DOVE SI TROVA IL SUO OBIETTIVO ----------------

% casVisib(IdCella, Xc, Yc) :- cell(Xc,Yc), Yc=0, IdCella = (Xc*MY)+Yc, numCol(MY).
% casVisib(IdCella, Xc, Yc) :- emptyGem(Xc,Yc), IdCella = (Xc*MY)+Yc, numCol(MY).
% casVisib(IdCella, Xc, Yc) :- presentGem(Xc,Yc, _), IdCella = (Xc*MY)+Yc, numCol(MY).
% connesso(IdCella1, IdCella2) :- casVisib(IdCella1, X1, Y1), casVisib(IdCella2, X2, Y2), DX = X1 - X2, &abs(DX;DXA), DY = Y1 - Y2, &abs(DY;DYA), DXA=DYA, DXA = 1.
% path(X, Y) :- connesso(X, Y).
% path(X, Y) :- connesso(X, Z), path(Z, Y).

% verrà prodotto un "raggiunge(id)" per ogni robot che riesce effettivamente a raggiungere il suo obiettivo passando solo per celle visibili
% raggiunge(IDR) :- objectiveIs(IDR,Xo,Yo,_,_), robot(IDR,_,Xr,Yr,_), path(Cas1, Cas2), casVisib(Cas1,Xo,Yo), casVisib(Cas2,Xr,Yr).

% -------------- la implementiamo dopo perchè ci metto troppo a debuggarla 😥 ----------------


visibilita(X,Y) :- cell(X,Y), Y=0.
visibilita(X,Y) :- emptyGem(X,Y).
visibilita(X,Y) :- presentGem(X,Y,_).

% Numero di celle visibili
numVisibilita(N) :- N = #count { X, Y : visibilita(X, Y) }.
% Vincolo: se ci sono celle visibili, i robot devono muoversi solo verso celle visibili
:~ numVisibilita(N), N > 15, mossa(ID, X, Y, _, _), not visibilita(X, Y). [N@6, ID, X, Y]







% -------------- VINCOLI PER ASSICURARSI DELLA LEGALITA' DELLE MOSSE ----------------

% Vincolo 0: Se un robot non si trova nel QG non gli si può assegnare una mossa con un oggetto diverso da quello che possiede
:- robot(ID, _, _, Y, AR), mossa(ID, _, _, _, AM), Y!=0, AR!=AM.

% Se un robot possiede già un oggetto diverso da none non gli si può dare una mossa con un oggetto diverso
:- robot(ID, _, _, _, AR), mossa(ID, _, _, _, AM), AR!=none, item(AR), AR!=AM.

% Vincolo 1: assicurati che ogni robot abbia esattamente una mossa
:- robot(ID, _, _, _, _), not 1 = #count {X,Y,A,I : mossa(ID,X,Y,A,I)}.

% Vincolo 2: Evita mosse fuori dalla mappa
:- mossa(_, X, _, _, _), not row(X).
:- mossa(_, _, Y, _, _), not col(Y).

% Vincolo 3: Non scavare o piazzare nella casa base
:- mossa(_, _, 0, place, _).
:- mossa(_, _, 0, dig, _).

% Vincolo 4: Non permettere di piazzare se l'item è 'none' o 'gem'
:- mossa(_, _, _,place,none).
:- mossa(_, _, _,place,gem).

% delivera solo se si trova nel QG
:- mossa(_, _, Y, A, _), action(A), A=deliver, Y!=0.

% Vincolo 5: Assicurati che le mine e i radar possano essere usati solo per piazzare, muoversi o non fare nulla
:- mossa(_, _, _,dig,radar).
:- mossa(_, _, _,deliver,radar).

:- mossa(_, _, _,dig,mine).
:- mossa(_, _, _,deliver,mine).

:- mossa(_, _, _,dig,gem).
:- mossa(_, _, _,deliver,none).

%   Vincolo 6: Forza il comando move se il robot si è spostato
:- mossa(ID,X,Y,A,_), robot(ID, _, X2, _, _), X!=X2, A!=move.
:- mossa(ID,X,Y,A,_), robot(ID, _, _, Y2, _), Y!=Y2, A!=move.

%   Vincolo 7: Non è possibile che se action move e item mine/radar/gem le coordinate siano uguali
:- mossa(ID, X, Y, move, _), robot(ID, _, Xr, Yr, _), X = Xr, Y = Yr.

%   Impedisci che vengano prese più di una mina o radar
:- #count{ ID, X, Y, A : mossa(ID, X, Y, A, I), I=mine, item(I) } > 1.
:- #count{ ID, X, Y, A : mossa(ID, X, Y, A, I), I=radar, item(I)} > 1.



#show robot/5.
#show objectiveIs/4.
#show mossa/5.







