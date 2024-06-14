

%   riga(0..14). 
%   colonna(0..29). 
%   cella(X,Y) :- riga(X), colonna(Y).
%   action(dig). action(place). action(move). action(deliver). action(none).
%   item(mine). item(radar). item(gem). item(none).

%   coords(IdRobot,X,Y). 
%   ATTENZIONE! 
%   coords(IdRobot,X,Y). VIENE PASSATO DA JAVA ED È ESCLUSIVO PER IL TEAM BOGO, quello scemo
%   I TEAM NORMALI DEVONO IMPLEMENTARE LA LORO LOGICA ASP DI MOVIMENTO

%   X va da 0 a 14
%   Y va da 0 a 29

%   IdRobot va da 0 a 14
%   IdItem è un numero intero incrementale da 0 a +inifinito
%   Color può essere blue o red

%   Item può essere:
%                   mine
%                   radar
%                   gem
%                   none

%   ESEMPI DI ALCUNI DATI DI INPUT DI UNA SITUAZIONE IN GAME PER IL TEAM RED


% guess delle mosse
{mossa(ID, X, Y, A, I) : coords(ID, X, Y), action(A), item(I)} = 1 :- robot(ID, _, _, _, _).



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


#show mossa/5.