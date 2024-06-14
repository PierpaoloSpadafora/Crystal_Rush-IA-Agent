                                                    %%%%%%%%%%%%%%%%%%%%%%%%%
                                                     %GEN MOSSE % IL SANTO GRAL
                                                     %%%%%%%%%%%%%%%%%%%%%%%%%


 {mossa(ID, X, Y, A, I) :mossaPossibile(X1,Y1,X,Y),action(A),item(I)} = 1 :- robot(ID, _, X1, Y1, _). %GENERA TUTTE LE POSSIBILI MOSSE, SPOSTAMENTO MAX 1

 mossaPossibile (X1,Y1,X,Y) :- robot(ID, _, X1, Y1, _),row(X),col(Y),X1-X<=1,Y1-Y<=1,X-X1<=1,Y-Y1<=1. %LE MOSSE POSSIBILI SONO SOLO QUELLE A DISTANZA 1


 %---------------------------------------------------------------------

                                                     %%%%%%%%%%%%%%%%%%%%%%%%%
                                                     % CALCOLO SINGOLI ROBOT. %
                                                     %%%%%%%%%%%%%%%%%%%%%%%%%

 idMaxRobot(Id) :- Id = #max { ID : robot(ID,_,_,_,_) }. % CALCOLA ROBOT MAX
 idMinRobot(Id) :- Id = #min { ID : robot(ID,_,_,_,_) }. %CALCOLA ROBOT MIN
 idMidRobot(ID) :- robot (ID,_,_,_,_), idMaxRobot(ID1),idMinRobot(ID2), ID!=ID1, ID!=ID2. %CALCOLA ROBOT MID


 %---------------------------------------------------------------------
                                         %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                                             %MOSSE BANDITE     MOSSE BANDITE %
                                         %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %==================     BASE        ==========================%

 :-mossa(ID,_, 0, dig,_).  %NON SI PUÒ SCAVARE IN CASA BASE

 :-mossa(ID,_, 0, place,_).  %NON SI PUÒ PIAZZARE IN CASA BASE

 :-mossa(ID,_, _,_, I),robot(ID,_,_,Y,I1), I!=I1,Y!=0. % NON È POSSIBILE CAMBIARE ITEM SE NON IN BASE.

 %=================      DELIVER   =======================%

 :-mossa(ID,_, Y, deliver,_), Y!=0.  %NON SI PUO FARE DELIVER FUORI DALLA CASA BASE

 :- mossa(ID,_,_,A,_),robot(ID,_,_,0,gem), A!=deliver. %NON PUOI NON FARE DELIVER SE TI TROVI IN CASA BASE CON UNA GEMMA

 :-mossa(_,_,_,deliver,I), I!= gem. %NON PUOI FARE DELIVER SE NON HAI LA GEMMA
 %=================      MOVEMENT       =======================%

 :- mossa(ID, X, _,_ ,_ ), not row(X). % NON PUOI ANDARE FUORI MAPPA

 :- mossa(ID, _, Y,_ ,_ ), not col(Y). % NON PUOI ANDARE FUORI MAPPA

 :-mossa(ID, _, Y, A, _),robot(ID,_,_,Y2,_), Y != Y2, A!=move. %NON PUOI FARE UN'ACTION DIVERSA DA MOVE SE LA MOSSA HA COME PARAMENTRI X/Y MOVE.

 :-mossa(ID, X, _, A, _),robot(ID,_,X2,_,_), X != X2, A!=move. %NON PUOI FARE UN'ACTION DIVERSA DA MOVE SE LA MOSSA HA COME PARAMENTRI X/Y MOVE.

 :-mossa(ID, X, Y, move, _),robot(ID,_,X,Y,_). %NON PUOI FARE UN'ACTION MOVE SE NON TI SPOSTI

 %=================      DIG         ==============================%

 :-mossa(_,_,_,dig,I), I!=none. %NON PUOI SCAVARE SE HAI UN ITEM

 :-mossa(ID,Y,X,dig,_), robot (ID,_,X1,Y1,_,_), X1!=X,Y1!=Y. %NON PUOI SCAVARE FUORI DALLA TUA CELLA OCCUPATA

 %=================      PLACE      ==============================%

 :-mossa(ID,_,_,place,I),I!=radar, I!=mine. %NON PUOI PIAZZARE SE NON HAI GLI ITEM GIUSTI

 %====================   GAMESENSE       ============================%

 :-mossa(_,_,Y,A,_),A=dig, Y<6. %DATA LA STRUTTURA DEL GIOCO NON SCAVARE PRIMA DEL 5 %%%%DA CAMBIARE CON 6
 :-mossa(_,_,Y,A,_),A=place, Y<6. %DATA LA STRUTTURA DEL GIOCO NON PIAZZARE PRIMA DEL 5 %%%%%%DA CAMBIARE CON 6


 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

                                                         %%%%%%%%%%%%%%%%%%%%%%%%%
                                                         %STRATEGIA MAX E MIN %
                                                         %%%%%%%%%%%%%%%%%%%%%%%%%

                                                         %%%%%%%%%%%%%%%%%%%%%%%%%
                                                             %      MAX     %
                                                         %%%%%%%%%%%%%%%%%%%%%%%%%

 %=============== INIZO Y=0 ====================%

 :~mossa(ID, _,_ , A, I),robot(ID,_,_,Y,_),idMaxRobot(ID), I!=none,Y=0. [1@3] %NON È POSSIBILE CHE IDMAX IN Y=0 NON HA NONE

 %================= CALCOLO DISTANZE RADAR E MINERALI ============================%

 distanzaMossaMAXCellaRadar(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMaxRobot(ID),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA DI MAX FACENDO LA MOSSA ALLA CELLA RADAR
 distanzaAttualeMAXCellaRadar(ID,D):- robot(ID,_,X,Y,_),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA ATTUALE DI MAX ALLA CELLA RADAR


 distanzaMAXMinerale(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMaxRobot(ID),cellaMinerale(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA DI MAX FACENDO LA MOSSA ALLA CELLA MINERALE
 distanzaAttualeMAXCellaMinerale(ID,D):- robot(ID,_,X,Y,_),idMaxRobot(ID),cellaMinerale(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA ATTUALE DI MAX ALLA CELLA MINERALE

 %================   WEAK PER AVVICINARTI ALLE CELLE RADAR        ==================%

 :~ mossa (ID,X,Y,_,none), distanzaMossaMAXCellaRadar(ID,D,X,Y). [D@1,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLARADAR

 :~ mossa (ID,_,_,A,none), distanzaAttualeMAXCellaRadar(ID,0), A!= none. [1@2] %STAI FERMO UNA VOLTA ARRIVATO

 %================   WEAK PER RECUPERARE I MINERALI        ==================

 :~ mossa (ID,X,Y,_,none), distanzaMAXMinerale(ID,D,X,Y). [D@2,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLAMINERALE

 :~ mossa (ID,_,_,A,none), distanzaAttualeMAXCellaMinerale(ID,0), A!= dig. [1@3] %SCAVA MINERALE


 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

                                                         %%%%%%%%%%%%%%%%%%%%%%%%%
                                                             %      MIN     %
                                                         %%%%%%%%%%%%%%%%%%%%%%%%%

 %=============== INIZO Y=0 ====================%
  scava(Id) :- Id = #count { ID : distanzaMinMinerale(ID,D,X,Y) }. % CONTROLLA SE PUOI SCAVARE MINERALI

 :~ mossa(ID, _,_ , A, I),robot(ID,_,_,Y,_),idMinRobot(ID), I!=radar,Y=0, scava(0). [1@3] % È PREFERIBILE CHE IDMIN IN Y=0  ABBIA UN RADAR
 :~ mossa(ID, _,_ , A, I),robot(ID,_,_,Y,_),idMinRobot(ID), I!=none,Y=0,scava(S), S>0.[1@3] %SE CI SONO MINERALI ESCI DALLA BASE CON ITEM NONE

 %================= CALCOLO DISTANZE RADAR E MINERALI ============================%
 distanzaMossaMinCellaRadar(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMinRobot(ID),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA DI MIN FACENDO LA MOSSA ALLA CELLA RADAR
 distanzaAttualeMinCellaRadar(ID,D):- robot(ID,_,X,Y,_),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA ATTUALE DI MIN ALLA CELLA RADAR

 distanzaMinMinerale(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMinRobot(ID),cellaMinerale(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA DI MIN FACENDO LA MOSSA ALLA CELLA MINERALE
 distanzaAttualeMinCellaMinerale(ID,D):- robot(ID,_,X,Y,_),cellaMinerale(X1,Y1),idMinRobot(ID),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA ATTUALE DI MIN ALLA CELLA MINERALE

 %================   WEAK PER PIAZZARE I RADAR        ==================%

 :~ mossa (ID,X,Y,_,radar), distanzaMossaMinCellaRadar(ID,D,X,Y). [D@1,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLARADAR
 :~ mossa (ID,_,_,A,radar), distanzaAttualeMinCellaRadar(ID,0), A!= place. [1@2] %PIAZZA RADAR UNA VOLTA ARRIVATO

 %================   WEAK PER RECUPERARE I MINERALI        ==================

 :~ mossa (ID,X,Y,_,none), distanzaMinMinerale(ID,D,X,Y). [D@2,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLAMINERALE
 :~ mossa (ID,_,_,A,none), distanzaAttualeMinCellaMinerale(ID,0), A!= dig. [1@3] %SCAVA MINERALE

 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

                                                         %%%%%%%%%%%%%%%%%%%%%%%%%
                                                             %      MID    %
                                                         %%%%%%%%%%%%%%%%%%%%%%%%%
  %=============== INIZO Y=0 ====================%
 :~ mossa(ID, _,_ , A, I),robot(ID,_,_,Y,_),idMidRobot(ID), I!=mine,Y=0 . [1@3] %È PREFERIBILE CHE IDMID IN Y=0  ABBIA UNA MINA
 :~ mossa(ID, _,_ , A, I),robot(ID,_,_,Y,_),idMidRobot(ID), I!=none,Y=0,mineFinite(1) . [2@3] %SE CI SONO MINERALI ESCI DALLA BASE CON ITEM NONE
  %================= CALCOLO DISTANZE MINE E MINERALI ============================%

  distanzaMossaMidCellaMine(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMidRobot(ID),cellaMine(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA DI MID FACENDO LA MOSSA ALLA CELLA MINE
  distanzaAttualeMidCellaMine(ID,D):- robot(ID,_,X,Y,_),cellaMine(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2. %CALCOLA LA DISTANZA ATTUALE DI MID ALLA CELLA MINE

 distanzaMidMinerale(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMidRobot(ID),cellaMinerale(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2,mineFinite(1). %CALCOLA LA DISTANZA DI MID FACENDO LA MOSSA ALLA CELLA MINERALE
 distanzaAttualeMidCellaMinerale(ID,D):- robot(ID,_,X,Y,_),cellaMinerale(X1,Y1),idMidRobot(ID),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2,mineFinite(1). %CALCOLA LA DISTANZA ATTUALE DI MID ALLA CELLA MINERALE

  distanzaMossaMidCellaRadar(ID,D,X,Y):- mossa(ID, X, Y, _, _), idMidRobot(ID),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2,mineFinite(1). %CALCOLA LA DISTANZA DI MAX FACENDO LA MOSSA ALLA CELLA RADAR
  distanzaAttualeMidCellaRadar(ID,D):- robot(ID,_,X,Y,_),idMidRobot(ID),cellaRadar(X1,Y1),&abs(X-X1;D1), &abs(Y-Y1;D2),D= D1+D2,mineFinite(1). %CALCOLA LA DISTANZA ATTUALE DI MAX ALLA CELLA RADAR

  %================   WEAK PER PIAZZARE LE MINE        ==================%

  :~ mossa (ID,X,Y,_,mine), distanzaMossaMidCellaMine(ID,D,X,Y). [D@1,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLAMINE
  :~ mossa (ID,_,_,A,mine), distanzaAttualeMidCellaMine(ID,0), A!= place. [1@2] %PIAZZA MINE UNA VOLTA ARRIVATO

  %================   WEAK PER AVVICINARTI AI RADAR       ==================



   :~ mossa (ID,X,Y,_,none), distanzaMossaMidCellaRadar(ID,D,X,Y). [D@1,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLARADAR

   :~ mossa (ID,_,_,A,none), distanzaAttualeMidCellaRadar(ID,0), A!= none. [1@2] %STAI FERMO UNA VOLTA ARRIVATO


  %================   WEAK PER RECUPERARE I MINERALI        ==================

  :~ mossa (ID,X,Y,_,none), distanzaMidMinerale(ID,D,X,Y). [D@2,X,Y,ID] %AVVICINATI SEMPRE DI PIÙ ALLA CELLAMINERALE

  :~ mossa (ID,_,_,A,none), distanzaAttualeMidCellaMinerale(ID,0), A!= dig. [1@3] %SCAVA MINERALE

 %---------------------------------------------------------------------

                                                 %%%%%%%%%%%%%%%%%%%%%%%%%
                                                   %WEAK CONSTR.  GLOBALI
                                                 %%%%%%%%%%%%%%%%%%%%%%%%%



 :~ mossa (ID,X,Y,_,gem). [Y@4,ID,X,Y] %SE HAI GEMMA CORRI IN BASE

 :~mossa(ID,X, Y,_, I),robot(ID,_,_,_,I1), Y=0,I!=I1. [1@2,ID,X,Y,I] % SE CAMBI ITEM PAGHI

 :~mossa(ID,X, Y,A, gem),A!=deliver,Y!=0. [1@4,ID,X,Y] %SE NON CONSEGNI PAGA

 %---------------------------------------------------------------------
