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
