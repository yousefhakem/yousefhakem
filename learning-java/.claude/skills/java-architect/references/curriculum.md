# The curriculum

Twelve milestones. Each has: what they build, the Java they learn, and — most importantly —
**the architecture lesson attached to it**. The architecture lesson is not a separate track;
it is taught at the moment the project makes it necessary.

Do not let them skip ahead. Do not let them linger. A milestone is done when its "done
when" line is true, not when the code is pretty.

---

## M0 — Ground floor (no framework)
**Build:** A console program that takes a list of expenses hardcoded in `main` and prints
each person's balance. No database, no Spring, no web. Just `java` and `javac` first, then
Maven.

**Java:** project structure, `main`, classes, records, `List`/`Map`, loops, `BigDecimal`,
printing, running via Maven.

**Architecture lesson:** *Start with the domain, not the framework.* The interesting part of
every application is the part that has nothing to do with the web. Prove it: this milestone
already contains the app's hardest rule.

**Done when:** balances print correctly for a group of 4 with unequal splits, and the code
compiles with `mvn` and has zero framework dependencies.

---

## M1 — Modelling the domain properly
**Build:** Turn M0's ad-hoc code into real types: `User`, `Group`, `Expense`, `Split`,
`Money`. Enforce the invariant *"splits sum exactly to the total"* in a place that cannot be
bypassed. Handle the leftover-cent problem.

**Java:** records vs classes, constructors and validation, immutability, `equals`/`hashCode`,
enums, custom exceptions, `BigDecimal` arithmetic and rounding.

**Architecture lesson:** *Invariants decide where code lives.* Also: make illegal states
unrepresentable — if a constructor can reject bad data, no downstream code needs to check.

**Done when:** it is impossible to construct an `Expense` whose splits don't balance, and
there's a test proving it.

---

## M2 — Tests as design feedback
**Build:** A real test suite for the domain. Including the settlement simplification
algorithm (M3's core) specified as tests first.

**Java:** JUnit 5, assertions, parameterised tests, test naming, Maven test lifecycle.

**Architecture lesson:** *"Hard to test" is a design smell, not a testing problem.* Every
one of these tests should run in milliseconds with no database and no Spring. If one
doesn't, the dependency is pointing the wrong way.

**Done when:** the whole domain suite runs in under a second.

---

## M3 — A deep module
**Build:** The settlement simplifier: given a set of balances, produce the minimum set of
transfers. Hard algorithm, tiny interface.

**Java:** collections manipulation, sorting, streams, recursion or greedy iteration.

**Architecture lesson:** *Depth = functionality ÷ interface size.* Contrast it with a
shallow "manager" class. This is the module they should be proudest of, and it depends on
nothing.

**Done when:** one public method, no dependencies outside the domain, correct on a set of
adversarial test cases they wrote themselves.

---

## M4 — Persistence, and the one-way door
**Build:** Postgres in Docker, Flyway migrations, schema for users/groups/expenses/splits.
Save and load real data. **Design the schema on paper before writing any DDL.**

**Java/tooling:** Docker Compose, JDBC concepts, SQL DDL, Flyway, Spring Data JPA basics,
entity mapping, transactions.

**Architecture lesson:** *The data model is the most expensive decision in the app.* Spend a
full session on it. Cover: natural vs surrogate keys, nullability as a business statement,
why every table gets `created_at`, why you never delete rows you might need, and the
question they must answer explicitly — **is a balance stored or derived?** (Make them argue
both sides before choosing.)

**Done when:** migrations run from empty to full schema, and they can explain every column.

---

## M5 — First HTTP
**Build:** Spring Boot app, a handful of endpoints, Thymeleaf pages to create a group and
add an expense.

**Java/Spring:** Spring Boot startup, dependency injection, `@RestController`/`@Controller`,
request mapping, request/response binding, status codes, `application.yml`.

**Architecture lesson:** *The web layer is a thin adapter.* Its only jobs are: parse the
request, call one application service, render the result. If there's a business rule in a
controller, it's in the wrong place. Introduce DTOs here and explain the cost of *not*
having them (your database schema becomes your public API).

**Done when:** they can delete a controller and the domain still compiles and passes tests.

---

## M6 — Layers and dependency direction
**Build:** Refactor into `domain` / `application` / `infrastructure` / `web`. Introduce
application services. Nothing new is added — this milestone is pure restructuring.

**Architecture lesson:** *Arrows point inward.* Also the first real lesson in refactoring
without rewriting: many small safe moves, tests green after each one.

**Done when:** the domain package imports nothing from Spring, JPA, or the web.

---

## M7 — Identity and authorization
**Build:** Sign-up, log-in, sessions. Then the harder half: a member of group A must not be
able to see or touch group B's data — enforced in one place, not sprinkled across queries.

**Java/Spring:** Spring Security basics, password hashing, sessions vs tokens, filters.

**Architecture lesson:** *Authorization is an architectural concern, not a feature.* This is
the clearest one-way door in the project: retrofitting it later means auditing every query.
Teach the difference between authentication, authorization, and tenant isolation, and make
them write a test that tries to break in.

**Done when:** a test authenticating as user B gets 403/404 on every one of A's resources.

---

## M8 — An external dependency
**Build:** Currency conversion against a real exchange-rate API, using the rate on the
expense date. Then: what happens when that API is down, slow, or wrong?

**Java/Spring:** HTTP clients, JSON deserialization, timeouts, retries, caching, config for
secrets.

**Architecture lesson:** *Ports and adapters, for the one case that genuinely needs it.*
Define the interface your domain wants (`ExchangeRates.rateFor(date, from, to)`), implement
it against the vendor, and keep the vendor's JSON shape out of your domain entirely. Then
the failure conversation: every network call has three outcomes, not two, and "slow" is the
one that takes down applications.

**Done when:** the domain tests use a fake implementation, and the app degrades sensibly
when the real API fails.

---

## M9 — Work that happens without a user
**Build:** The weekly summary email. Scheduled, idempotent, observable.

**Java/Spring:** `@Scheduled`, transactions across a job, sending mail, logging.

**Architecture lesson:** *Side effects and idempotency.* What happens if the job runs twice?
If it crashes halfway? If two instances run it? Introduce the idea that a job is a second
entry point into the same application services — which is the payoff for M6.

**Done when:** running the job twice does not send two emails.

---

## M10 — Data at scale
**Build:** Paginated, filterable expense history. Receipt upload to object storage (or local
disk behind an interface).

**Java/Spring:** pagination, query methods, indexes, `EXPLAIN`, N+1 detection, file
streaming, multipart uploads.

**Architecture lesson:** *Performance problems are usually data-access design problems.*
Show them an N+1 in their own code with SQL logging on — it is the single most memorable
lesson in the whole curriculum. Also: why binaries don't go in the database.

**Done when:** the history page issues a bounded number of queries regardless of page size,
and they can prove it.

---

## M11 — Shipping it
**Build:** Dockerfile, environment-based config, secrets handling, deploy to a real URL,
basic logging and health checks, a rollback plan.

**Architecture lesson:** *Deployability is an architectural property.* Config vs code, the
twelve-factor ideas that actually matter, why migrations must be forward-compatible with the
currently-running version, and what "it works on my machine" actually means.

**Done when:** it is live at a URL, and they can deploy a change in under five minutes.

---

## M12 — Change it
**Build:** Add a substantial feature that wasn't in the original plan. Suggested:
recurring expenses, or multi-currency settlement, or an expense approval flow.

**Architecture lesson:** *The final exam.* The point is to measure the architecture: how
many files did the change touch? Where did it hurt? What decision from M4 or M7 made it
easy or hard? Then write the retrospective ADR. This is where the whole journey pays off,
and where they learn what to do differently on their next application.

**Done when:** the feature ships and they've written an honest post-mortem of their own
design decisions.
