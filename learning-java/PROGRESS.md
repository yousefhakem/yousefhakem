# Progress

Maintained by the `java-architect` skill. Update at the end of every session.

- **Current milestone:** M1 — Modelling the domain properly
- **Started:** 2026-08-12
- **Last session:** 2026-08-12 — M0 completed

## Status

| Milestone | State |
|---|---|
| M0 Ground floor | done |
| M1 Domain model | not started |
| M2 Tests as design feedback | not started |
| M3 Deep module (settlement) | not started |
| M4 Persistence | not started |
| M5 First HTTP | not started |
| M6 Layers | not started |
| M7 Identity & authorization | not started |
| M8 External dependency | not started |
| M9 Background work | not started |
| M10 Data at scale | not started |
| M11 Shipping | not started |
| M12 Change it | not started |

## Open questions to resolve later

- Is a balance stored or derived? (decide at M4 — M0 built it as derived-on-demand via `BalanceCalculator`, no persistence involved yet, so this isn't a real answer to the M4 question, just a data point.)
- What happens to an expense when a member leaves a group?
- Can an expense be edited after a settlement has been recorded?

## Session log

2026-08-12 — Scaffolded a minimal Java main class in src/Main.java and confirmed it compiles and runs.
2026-08-12 — Completed M0. Built `Person`, `Expense` (paidBy, total amount, explicit per-person dollar shares), and `BalanceCalculator` (derives a `Map<Person, BigDecimal>` from a `List<Expense>`, never stored on `Person`). Set up the Maven project (`my-app/`) via archetype, wired `exec-maven-plugin` with a fixed `mainClass` so `mvn exec:java` runs it directly. Hit and fixed real bugs along the way: multiple public classes in one file, mismatched method return types after a refactor, an unbalanced hardcoded expense (shares not summing to total), and a payer who wasn't a participant in their own expense's shares map (silently dropped from output) — fixed by seeding the balance map with `paidBy → amount` before the shares loop, letting the loop overwrite it when the payer is also a participant. Verified correctness by hand: all 4 people's balances sum to zero. Chose explicit per-person dollar shares over percentages specifically to dodge rounding drift (found the classic $100÷3 = $99.99 problem themselves). Applied the ADR reversal-cost filter to both decisions from this session and concluded neither warrants a formal ADR yet — explicit shares vs. percentages is cheap (M1 rebuilds the domain model anyway), and derived-vs-stored balance is already an open question owned by M4.

<!-- one line per session: date — what was built, what was learned, what tripped them up -->
