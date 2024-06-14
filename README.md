# ISTRUZIONI

## Create il vostro package e file in [questa cartella](/Crystal-Rush-ASP/src/main/java/it/unical/ai) analogamente a BOGO con i seguenti nomi

### - XFORZA/TeamLogicXFORZA.java

### - PIO/TeamLogicPIO.java

### - GRUPPO_BELLO/TeamLogicPIO.java

### - CIGNI_HI_TECH/TeamLogicCIGNI_HI_TECH.java

---

### Predicato da restituire per ciascun robot

 > #### mossa(ID,X,Y,A,I)

### Le mosse illegali sono

1. ### Provare a muoversi fuori dalla mappa

2. ### Provare a muoversi di più di una singola casella

3. ### Provare a compiere azioni in caselle diverse da quella in cui ci si trova

4. ### Provare a compiere due azioni contemporaneamente (muoversi e piazzare / muoversi e scavare / ecc.)

5. ### Provare a scavare o piazzare oggetti in casa base

6. ### Provare a piazzare senza avere l'oggetto adeguato per piazzare (mina o radar)

7. ### Provare a consegnare al di fuori dalla casa base

8. ### Provare a consegnare senza avere una gemma

9. ### Provare a scavare un radar nemico per distruggerlo avendo con se oggetti

10. ### La mappa **non è** toroidale

---

## Crystal Rush

**Crystal Rush** è un gioco semplice sviluppato in Java Swing, progettato come progetto universitario. Il gioco si concentra sull'uso dell'intelligenza artificiale per controllare gli agenti nel gioco, utilizzando la programmazione in Answer Set Programming (ASP).

## Descrizione

In **Crystal Rush**, il giocatore deve raccogliere cristalli sparsi in un campo di gioco mentre evita o interagisce con vari agenti controllati dall'intelligenza artificiale. Gli agenti utilizzano strategie basate su ASP per decidere dove andare e che azione compiere (Scavare, Posizionare una mina, Posizionare un radar)
L'ambiente è ignoto allo start e gli agenti ne acquisiscono conoscenza visitando le celle e posizionando i radar.

### Obiettivo del gioco

- Raccogliere più cristalli del team avversario senza farsi annientare dalle mine nemiche

## Funzionalità

- **Interfaccia Grafica in Swing**: Una semplice interfaccia utente che permette di creare "tornei" permettendo di far sfidare tra loro a turno encoding ASP differenti
- **Agenti AI**: Agenti che operano basandosi su logiche definite tramite ASP per simulare l'intelligenza.

## Tecnologie Utilizzate

- **Java Swing**: per la creazione dell'interfaccia grafica.
- **Answer Set Programming**: per lo sviluppo dell'intelligenza artificiale degli agenti.
