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
%   
%   
%   
%   

%   ESEMPI DI ALCUNI DATI DI INPUT DI UNA SITUAZIONE IN GAME PER IL TEAM RED


















allyStuff(X,Y) :- allyMine(X,Y).
allyStuff(X,Y) :- allyRadar(X,Y).

%   Scegliere un obiettivo
{
    objectiveIs(ID, X, Y, goReceiveItem) : cell(X,Y), robot(ID, _, _, _, none), Y=0;
    objectiveIs(ID, X, Y, pickGem) : robot(ID, _, _, _, none), presentGem(X,Y,_);
    objectiveIs(ID, X, Y, digEnemyRadar) : robot(ID, _, _, _, none), enemyRadar(X,Y);

    objectiveIs(ID, X, Y, placeMine) : cell(X,Y), robot(ID, _, _, _, mine), Y!=0;
    objectiveIs(ID, X, Y, placeRadar) : goodRadarPlace(X,Y,_), robot(ID, _, _, _, radar), Y!=0;
    objectiveIs(ID, X, Y, deliverGem) : cell(X,Y), robot(ID, _, _, _, gem), Y=0


} = 1 :- robot(ID, _, _, _, _).


:- objectiveIs(ID, X, Y, placeMine), allyStuff(X,Y).
:- objectiveIs(ID, X, Y, placeRadar), allyStuff(X,Y).


% TODO incrementare i costi adeguatamente per migliorare l'euristica
:~ objectiveIs(ID, Xo, _, _), robot(ID, _, Xr, _, _), X=Xo-Xr, &abs(X;Cost). [Cost@2, ID]
:~ objectiveIs(ID, _, Yo, _), robot(ID, _, _, Yr, _), Y=Yo-Yr, &abs(Y;Cost). [Cost@2, ID]



visibilita(X,Y) :- cell(X,Y), Y=0.
visibilita(X,Y) :- emptyGem(X,Y).
visibilita(X,Y) :- presentGem(X,Y,_).


% FORSE
% Numero di celle visibili
numVisibilita(N) :- N = #count { X, Y : visibilita(X, Y) }.

% Vincolo: se ci sono celle visibili, i robot devono muoversi solo verso celle visibili
:~ numVisibilita(N), N > 15, objectiveIs(_, X, Y, _), not visibilita(X, Y). [N@3, X, Y]



:~ objectiveIs(ID, X, Y, goReceiveItem). [3@3, ID]
:~ objectiveIs(ID, X, Y, pickGem). [2@3, ID]
:~ objectiveIs(ID, X, Y, digEnemyRadar). [1@3, ID]

:~ objectiveIs(ID, X, Y, placeMine). [2@3, ID]
:~ objectiveIs(ID, X, Y, placeRadar). [1@3, ID] 
:~ objectiveIs(ID, X, Y, deliverGem). [1@3, ID]


:~ objectiveIs(ID, X, Y, placeRadar), goodRadarPlace(X,Y,Cost) . [Cost@3, ID]




#show robot/5.
#show objectiveIs/4.