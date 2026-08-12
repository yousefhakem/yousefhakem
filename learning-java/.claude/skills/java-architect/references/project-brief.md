# The project: Tally — a shared-expense tracker

One project, built over the whole journey. Not a todo app, not a blog. This one was chosen
because it forces every architectural decision worth learning, and because its rules are
genuinely ambiguous — which is where design skill actually comes from.

## What it does

A group of people (housemates, a trip, a couple) records shared expenses. Anyone can add an
expense, say who paid and how it's split. At any moment the app can answer "who owes whom,
and what's the smallest set of payments that settles everything?"

## Core requirements

1. A user signs up and logs in.
2. A user creates a **group** and invites others by email.
3. A member records an **expense**: amount, currency, description, who paid, and how it is
   split (equally / by exact amounts / by shares).
4. The app shows each member's **balance** within a group.
5. The app computes a **simplified settlement**: the minimum set of transfers that clears
   all debts.
6. A member records a **payment** between two members, which updates balances.
7. Expenses in a foreign currency are converted using the rate **on the date of the
   expense**, fetched from an external API.
8. A weekly summary email goes to each group.
9. A member can attach a receipt image to an expense.
10. Expense history is paginated, filterable, and searchable.

## Why each requirement is there (don't skip any)

| Requirement | What it teaches |
|---|---|
| Auth + groups | Identity model, authorization, tenant boundaries — the classic one-way door |
| Splits | Domain modelling with a real invariant: splits must sum to the total, exactly |
| Money | Why `double` is wrong, rounding rules, where the leftover cent goes |
| Balances | Derived vs stored state — the most under-taught design decision there is |
| Settlement | A genuinely hard algorithm that belongs in a deep, framework-free module |
| Currency API | Integration, failure handling, caching, ports and adapters |
| Weekly email | Background jobs, scheduling, idempotency, side effects |
| Receipts | File storage, streaming, the "don't put blobs in the database" lesson |
| History | Pagination, indexes, N+1 queries, query performance |

There are deliberate ambiguities in the requirements above (what happens to an expense when
a member leaves? can an expense be edited after a settlement? is a balance a number you
store or a number you compute?). **Do not resolve them for the user.** Surfacing and
deciding them is the exercise.

## Stack

Decided once, up front, and not revisited without an ADR:

- **Java 21** (LTS, modern syntax: records, sealed types, pattern matching, virtual threads)
- **Maven** — more verbose than Gradle, but the error messages and the docs are better for
  a beginner, and every Java tutorial on earth assumes it
- **Spring Boot 3** — the default for the job market and the ecosystem
- **PostgreSQL** in Docker; **Flyway** for migrations from day one
- **Spring Data JPA** — with the explicit lesson that JPA hides SQL, and that hiding SQL is
  a debt you eventually pay
- **Thymeleaf** server-rendered pages first. No JavaScript framework until the backend is
  finished. This is deliberate: a front end is a second application, and adding it early
  doubles the surface area while teaching nothing about Java.
- **JUnit 5**, plain unit tests for the domain, **Testcontainers** for the database tests
- **Docker** for local infra, then deploy to a single small host (Fly.io, Railway, or a VPS)

Explicitly *not* in the stack, and the user should be able to say why: microservices,
Kafka, Redis, GraphQL, Kubernetes, a React front end, hexagonal-architecture scaffolding
generators, Lombok (hides what Java is actually doing while they're still learning it).
