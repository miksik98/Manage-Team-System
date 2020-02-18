# Zespoły projektowe

System do zarządzania zespołami projektowymi, wykonany w ramach przedmiotu Technologie Obiektowe 2.

## Ogólne założenia
- System do zarządzania zespołami projektowymi 
- Wprowadzanie osób, tematów projektów 
- Tworzenie zespołów projektowych 
- Zespołom wystawiane są punkty 
- System wysyła maile z powiadomieniami  

## 1 Milestone

Widoki i kontrolery związane z projektami, 
widok główny i kontroler do niego, 
założenie projektu i konfiguracja gradle, 
kontroler i widoki związane osobami, 
integracja JavaFX ze Springiem,
integracja bazy danych, model bazy danych(dao + repository), 
wzorzec Command -> rejestr komend, komendy związane z dodawaniem, usuwaniem rekordów, operacje undo i redo,
oraz odpowiednia związana z tym aktualizacja widoków. 

## 2 Milestone

Widok zespołów, dodawanie użytkowników do zespołu, podgląd zespołów z widoku projektów,
przydzielanie zespołowi projektu, podgląd zespołów z widoku osób, ustawianie lidera, dodanie obsługi wielu modali, 
integracja bazy danych, model bazy danych, łączenie relacji oraz API do poszczególnych metod(DAO),
ocena zespołu, widok ocen dla zespołu, usuwanie ocen.

## 3 Milestone

Wysyłanie maili z powiadomieniami (subskrybcja), 
dodanie generowania rankingów, ocenianie grupowe, tworzenie raportów,
import danych z pliku (JSON), dodanie tasków dla konkretnej osoby w obrębie danego projektu.

## Jak uruchomić?

1. Wymagana jest Java w wersji co najmniej 11.
2. Zmienna systemowa JAVA_HOME musi wskazywać na jdk11.
3. Z poziomu katalogu głównego projektu:
- MacOS/Linux: ```./gradlew.run```
- Windows: ```gradlew.bat run```

## Autorzy

Damian Wasilenko, Dawid Majchrowski, Jolanta Gluza, Mikołaj Sikora