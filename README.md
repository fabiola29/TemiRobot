# TemiRobot
Applicazione per temi robot in Android Studio su "personalizzaione dei comportamenti robotici in base allo scheduling delle attività quotidiane degli utenti".

Casi d'uso del sistema :

   1. Registrazione e accesso al sistema
   2. Cambio password e logout
   3. Creazione percorsi
   4. Lista personalizzata di percorsi
   5. Inserimento/cancellazione/modifica locazioni o punti d'interesse
   6. Inserimento attività
   7. Lista personalizzata di attività
   8. Esecuzione delle attività con priorità di orario

Implementato facendo uso di :

   - in android studio facendo uso di linguaggio Java per il back-end e XML per il front-end
   - Python 3 per registrazione e riconoscimento volto utente facendo uso della libreria fece recognition
   - Room database per la persistenza dei dati
   - sdk Temi per include funzionalità interne del robot nell'applicazione

Utilizzando inoltre per l'architettura esterna l'approccio a tre livelli in cui abbiamo:  

  - livello presentazione che si occupa dell'interazione con l'utente
  - livello di logica che si occupa della logica di business
  - livello dati che si occupa della persistenza dei dati
  
mentre per l'architettura interna un design pattern architetturale chiamato MVVM (Model View ViewModel) attraverso cui , è possibile trarre vantaggio in termini di prestazioni, leggibilità e modularità del codice.
