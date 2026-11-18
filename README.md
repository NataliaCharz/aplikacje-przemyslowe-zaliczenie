# LibraryApp – biblioteka autorów i książek

Projekt zaliczeniowy z przedmiotu **Aplikacje przemysłowe**.
Aplikacja w Spring Boot 4.1 (Java 21, Gradle z wrapperem, `build.gradle` w Groovy DSL) z bazą PostgreSQL.
Udostępnia REST API oraz panel w Thymeleaf dla encji głównej **Author** i encji podrzędnej **Book**
(relacja jeden-do-wielu: autor → książki).

## Uruchomienie

Wymagania: Java 21+, Docker.

```bash
# 1. Baza danych PostgreSQL (compose.yaml w katalogu głównym)
docker compose up -d

# 2. Aplikacja
./gradlew bootRun

# 3. Budowanie i testy
./gradlew build
./gradlew test
```

| Adres | Opis |
|-------|------|
| http://localhost:8080/swagger-ui.html | dokumentacja OpenAPI (springdoc) |
| http://localhost:8080/api/authors | REST API autorów |
| http://localhost:8080/api/books | REST API książek |

Dane połączenia z bazą (z `compose.yaml`): baza `library`, użytkownik `admin`, hasło `admin`, port `5432`.
Tabele tworzy Hibernate (`spring.jpa.hibernate.ddl-auto=update`), dane zostają po restarcie (wolumen `postgres_data`).

## Encje i reguły walidacji

### Author (encja główna)

| Pole | Typ | Reguła |
|------|-----|--------|
| `id` | `Long` | generowane przez bazę |
| `surname` | tekst | wymagane, długość 2–100 znaków |
| `yearOfBirth` | liczba (`Integer`) | wymagane, zakres od -3000 do 2010 |
| `status` | wyliczenie `AuthorStatus` | wymagane, jedna z wartości: `ALIVE`, `RETIRED`, `DEAD` |

Błędne dane przy dodaniu (`POST`) i zmianie (`PUT`) dają **400** z listą błędnych pól i niczego nie zapisują.

### Book (encja podrzędna)

| Pole | Typ | Reguła |
|------|-----|--------|
| `id` | `Long` | generowane przez bazę |
| `title` | tekst | wymagane, długość 2–500 znaków |
| `pages` | liczba (`Integer`) | wymagane, co najmniej 10 |
| `author` | `@ManyToOne` | książka należy do dokładnie jednego autora |

## Reguła biznesowa (R1.5) – 409

Autora o statusie `DEAD` nie można zmienić z powrotem na status `ALIVE`, czy `RETIRED`.
Próba takiej zmiany daje odpowiedź **409 Conflict** w formacie Problem Details (`application/problem+json`).
Reguła jest sprawdzana w warstwie serwisu.

## Endpointy REST

### Autorzy – `/api/authors`

| Metoda | Ścieżka | Opis | Odpowiedź |
|--------|---------|------|-----------|
| GET | `/api/authors` | lista autorów | 200 |
| GET | `/api/authors/{id}` | jeden autor | 200 / 404 |
| POST | `/api/authors` | dodanie autora | 201 + nagłówek `Location` / 400 |
| PUT | `/api/authors/{id}` | zmiana autora | 200 / 400 / 404 / 409 |
| DELETE | `/api/authors/{id}` | usunięcie autora | 204 / 404 |

### Książki – `/api/books`

| Metoda | Ścieżka | Opis | Odpowiedź |
|--------|---------|------|-----------|
| GET | `/api/books` | lista książek | 200 |
| GET | `/api/books/{id}` | jedna książka | 200 / 404 |
| POST | `/api/books` | dodanie książki | 201 + nagłówek `Location` / 400 / 404 |
| PUT | `/api/books/{id}` | zmiana książki | 200 / 400 / 404 |
| DELETE | `/api/books/{id}` | usunięcie książki | 204 / 404 |

Wszystkie błędy API (400, 404, 409) zwracane są jako Problem Details z jednego miejsca w kodzie
(`GlobalExceptionHandler`).

## Operacja transakcyjna (R3.1, R3.3)

_Do uzupełnienia na poziomie 3._ Opis operacji zmieniającej dwie tabele w jednej transakcji
oraz wpisu do dziennika zapisywanego w osobnej transakcji (`REQUIRES_NEW`).

## Pliki (R4.1)

_Do uzupełnienia na poziomie 4._ Dozwolone typy plików (Content-Type) i maksymalny rozmiar załącznika.

## Role i uprawnienia (R5.4)

_Do uzupełnienia na poziomie 5._ Co najmniej dwie role i ich uprawnienia w API i w panelu.

## Spełnione wymagania

| Kod | Wymaganie | Wartość | Status |
|-----|-----------|---------|--------|
| R1.1 | Encja JPA w PostgreSQL z `compose.yaml`, `ddl-auto=update` | 10% | ✅ |
| R1.2 | Lista i element po id, 404, DTO, OpenAPI | 10% | ✅ |
| R1.3 | POST 201 + Location, PUT 200, DELETE 204, logika w serwisie | 10% | ✅ |
| R1.4 | Bean Validation trzech pól, 400 z listą pól | 10% | ✅ |
| R1.5 | Problem Details z jednego miejsca, reguła biznesowa 409 | 10% | ⬜ |
| R1.6 | Panel Thymeleaf: lista, formularz, PRG | 10% | ⬜ |
| R2.1 | Encja podrzędna, lista i dodanie dla autora | 4% | ⬜ |
| R2.2 | Lista książek z nazwiskiem autora bez N+1 | 3% | ⬜ |
| R2.3 | Stronicowanie i filtr po statusie w bazie | 3% | ⬜ |
| R3.1 | Operacja na dwóch tabelach w jednej transakcji | 4% | ⬜ |
| R3.2 | `@Version`, nieaktualna wersja → 409 | 3% | ⬜ |
| R3.3 | Dziennik w `REQUIRES_NEW` | 3% | ⬜ |
| R4.1 | Załącznik multipart, pobieranie, walidacja typu i rozmiaru | 4% | ⬜ |
| R4.2 | Eksport CSV | 3% | ⬜ |
| R4.3 | Pliki w panelu | 3% | ⬜ |
| R5.1 | Testy `@WebMvcTest` | 3% | ⬜ |
| R5.2 | Test w Testcontainers | 2% | ⬜ |
| R5.3 | Spring Security, BCrypt, formularz + HTTP Basic, 401 | 3% | ⬜ |
| R5.4 | Dwie role, 403 | 2% | ⬜ |

**Deklarowana suma:** 40% (R1.1, R1.2, R1.3, R1.4).

## Deklaracja AI

Dziennik rozmów z narzędziami AI: [`ai-log/README.md`](ai-log/README.md).
Deklaracja: `DEKLARACJA_AI.md` (zostanie dodana przy oddaniu projektu).
