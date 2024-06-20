Języki programowania urządzeń mobilnych LAB

Projekt Dziennik
Aplikacja mobilna napisana w języku Kotlin z wykorzystaniem architektury MVVM (Model-View-ViewModel)
Program umożliwia tworzenie, edytowanie i usuwanie wpisów dziennka oraz przeglądanie kalendarza.

Autorzy:
Aleksandra Łacińska 40361
Karol Muziński 39956

Funkcjonalności:
1) dodawanie nowych wpisów do dziennika
2) edytowanie istniejących wpisów
3) usuwanie wpisów
4) przeglądanie kalendarza i odnajdywanie dni, w które mamy wpisy w dzienniku
5) uwzględnianie lokalizacji urządzenia podczas dodawania nowego wpisu
6) powiadomienia push o dodaniu nowego, edycji istniejącego lub usunięciu wpisu

Technologie:
1) Kotlin
2) Android SDK
3) Room (biblioteka do obsługi bazy danych SQLite)
4) AndroidX (w tym ViewModel, LiveData, RecyclerView)
5) Material Components (do elementów UI)

Uruchamianie projektu:
(należy mieć zainstalowane Android Studio (Jellyfish lub nowszy), Android SDK, Gradle

1) sklonuj repozytorium na sowje urządzenie
2) uruchom projekt w Android Studio
3) Zsynchronizuj projekt z Gradle (ikona słonika ze strzałką w lewym górnym rogu ekranu)
4) Skonfiguruj emulator (np. Pixel 7 Pro) lub podłącz urządzenie fizyczne
5) Uruchom aplikację za pomocą przycisku RUN

UWAGA:
*Podczas dodawania pierwszego wpisu, aplikacja poprosi użytkownika o udostępnienie lokalizacji urządzenia.
*Wyświetlanie lokalizacji działa bez zarzutu, jednak na niektórych urządzeniach (w tym na emulatorze) trzeba umożliwić dostęp, cofnąć do ekranu głównego i ponownie rozpocząć nowy wpis, aby pojawiła się miejscowość.

**Powiadomienia push wyświetlane są pojedynczo. Aby odczytać powiadomienie, przesuń palcem z góry ekranu na dół i rozwiń listę powiadomień urządzenia.

