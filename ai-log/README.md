# Dziennik użycia AI

## Wpis 1

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-18                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Szukanie błędów, wyjaśnianie pojęć i API |
| Link / eksport rozmowy | Wpis1.txt                                 |

**Cel:** sprawdzenie, czy encja główna `Author` spełnia wymaganie R1.1.

**Co zrobiło AI:** przeanalizowało `Author.java`, `compose.yaml` i `application.yml`
i wskazało błąd: kolumna `SEX` miała `columnDefinition = "sex"` z `@JdbcTypeCode(SqlTypes.NAMED_ENUM)`,
czyli typ PostgreSQL, którego Hibernate nie tworzy. Przez to tabela `author` nie powstałaby na czystej bazie.
Zaproponowało zostawienie samego `@Enumerated(EnumType.STRING)`.

**Co przyjęłam:** poprawkę wprowadziłam samodzielnie w `Author.java` oraz analogicznie w `Book.java`.
AI nie zmieniało kodu w repozytorium.

## Wpis 2

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-18                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Szukanie błędów, wyjaśnianie pojęć i API |
| Link / eksport rozmowy | Wpis2.txt                                 |

**Cel:** sprawdzenie, czy aplikacja spełnia akapit „Aplikacja” z wymagań oraz wymaganie R1.2.

**Co zrobiło AI:** porównało wymagania z `build.gradle`, `compose.yaml`, `application.yml`,
encjami, DTO, kontrolerami, serwisem i `GlobalExceptionHandler`. Wskazało braki: brak Thymeleafa,
pole daty zamiast pola liczbowego z zakresem (`@Min`/`@Max` na `LocalDate`), brak zależności
`spring-boot-starter-validation`, adres `https://localhost:8443` w `OpenApiConfig`.
Po moich poprawkach wskazało, że `GenerationType.UUID` przy `Long id` uniemożliwi start aplikacji
oraz że stara kolumna `DATE_OF_BIRTH` w bazie zablokuje zapis.

**Co przyjęłam:** samodzielnie zmieniłam pole daty na `yearOfBirth` (Integer), enum na `AuthorStatus`
(ALIVE, RETIRED, DEAD), usunęłam `OpenApiConfig` i katalog `build/`. Regułę biznesową zmieniłam
na: autora ze statusem DEAD nie można zmienić na ALIVE. AI nie zmieniało kodu w repozytorium.

## Wpis 3

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-18                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Szukanie błędów, wyjaśnianie pojęć i API |
| Link / eksport rozmowy | Wpis3.txt                                 |

**Cel:** sprawdzenie, czy wymaganie R1.3 (POST 201 z `Location`, PUT 200, DELETE 204,
logika w serwisie) jest poprawnie zaimplementowane.

**Co zrobiło AI:** przeanalizowało `AuthorController`, `AuthorService`, `AuthorMapperMapStruct`,
`AuthorDTO` i `Author` i wskazało trzy błędy: nagłówek `Location` wskazywał nieistniejący adres
`/api/authors/add/{id}` (przez ścieżkę `/add` w `@PostMapping`), PUT używał `@RequestParam`
zamiast `@PathVariable` (więc `PUT /api/authors/{id}` dawał 400), a mapper kopiował `id` z DTO
do encji, przez co PUT bez `id` w body tworzył nowy rekord zamiast zmienić istniejący.
Zaproponowało poprawki i zwróciło uwagę na drobne niespójności (mapowanie raz w kontrolerze,
raz w serwisie; brak sprawdzania reguły DEAD → ALIVE, która należy do R1.5).

**Co przyjęłam:** wszystkie trzy poprawki wprowadziłam samodzielnie: `@PostMapping()` bez `/add`,
`@PathVariable` w PUT oraz `@Mapping(target = "id", ignore = true)` i
`@Mapping(target = "books", ignore = true)` w `mapDtoToAuthorInMemory`.
AI nie zmieniało kodu w repozytorium.

## Wpis 4

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-18                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Dokumentacja: README, komentarze |
| Link / eksport rozmowy | Wpis4.txt                                 |

**Cel:** przygotowanie pliku `README.md` zgodnie z częścią „Oddanie” w `wymagania_projektu.pdf`.

**Co zrobiło AI:** przeczytało wymagania oraz kod (`build.gradle`, `compose.yaml`, `application.yml`,
encje, DTO, kontrolery, serwisy, `GlobalExceptionHandler`) i napisało całą treść nowego `README.md`:
polecenia uruchomienia, encje `Author` i `Book` z regułami walidacji, regułę biznesową 409
(DEAD → ALIVE), tabele endpointów REST, puste sekcje do uzupełnienia (R3.1, R4.1, R5.4) i listę wymagań
z zaznaczonymi R1.1–R1.3. Zwróciło uwagę, że reguła 409 i Problem Details dla błędów walidacji
nie są jeszcze w kodzie. Na moją prośbę zapisało plik w repozytorium i zrobiło commit `d354d27`
ze stopką `AI-Assisted`.

**Co przyjęłam:** treść `README.md` przyjęłam bez zmian. Kod aplikacji nie był zmieniany.

## Wpis 5

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-19                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Szukanie błędów, wyjaśnianie pojęć i API |
| Link / eksport rozmowy | Wpis5.txt                                 |

**Cel:** sprawdzenie, czy wymaganie R1.4 (Bean Validation trzech pól według README, przy dodaniu
i zmianie, 400 z listą błędnych pól, brak zapisu błędnych danych) jest spełnione.

**Co zrobiło AI:** porównało reguły z `README.md` z adnotacjami w `AuthorDTO`, sprawdziło `@Valid`
w POST i PUT w `AuthorController`, `GlobalExceptionHandler` i zależność `spring-boot-starter-validation`,
a także uruchomiło `./gradlew compileJava`. Wskazało błąd kompilacji: brak `return problemDetail;`
w metodzie `handle400Enum`. Zasugerowało krótki opis `detail` zamiast `ex.getMessage()` oraz zwróciło
uwagę na niespójność reguły 409: kod blokował DEAD → ALIVE i DEAD → RETIRED, a README opisywało tylko DEAD → ALIVE.

**Co przyjęłam:** błąd znalazłam i poprawiłam samodzielnie (dodany `return`). Opis błędu w obu handlerach
400 zmieniłam na `"Validation failed"`, a regułę 409 w `README.md` uzupełniłam o status `RETIRED`.
AI nie zmieniało kodu w repozytorium.

## Wpis 6

| |                                          |
|---|------------------------------------------|
| Data | 2026-11-19                               |
| Narzędzie | Claude Code (model Claude Opus 5.5)      |
| Element projektu (wg deklaracji) | Szukanie błędów, wyjaśnianie pojęć i API |
| Link / eksport rozmowy | Wpis6.txt                                 |

**Cel:** sprawdzenie, czy wymaganie R1.5 (wszystkie błędy API 400, 404, 409 jako Problem Details
z jednego miejsca w kodzie, bez śladu stosu, reguła biznesowa z README dająca 409) jest spełnione.

**Co zrobiło AI:** porównało wymaganie z `GlobalExceptionHandler`, wyjątkami, `AuthorService`,
`BookService`, `SecurityConfig`, `application.yml` i opisem reguły w `README.md`. Wskazało, że błędy
zgłaszane przez sam Spring (np. 404 dla nieistniejącego adresu, 405, 415) nie wracają jako Problem Details,
że `detail` z `ex.getMessage()` w handlerze 400 ujawnia nazwy klas, a także nieużywany wyjątek
`InvalidDateOfBirthException` i zbędny średnik w `AuthorService`. Zaproponowało dziedziczenie po
`ResponseEntityExceptionHandler`, wyjaśniło działanie `@Order` dla klas `@ControllerAdvice`
(kolejność advice, priorytet `ProblemDetailsExceptionHandler` Boota) i uruchomiło `./gradlew compileJava`.

**Co przyjęłam:** samodzielnie wybrałam wariant z `@Order(Ordered.HIGHEST_PRECEDENCE)` na
`GlobalExceptionHandler` i włączeniem `spring.mvc.problem-details.enabled` w `application.yml`
oraz usunęłam `InvalidDateOfBirthException`. AI nie zmieniało kodu w repozytorium.
