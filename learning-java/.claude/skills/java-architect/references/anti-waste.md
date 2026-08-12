# What not to waste time on

The user explicitly asked for this. Be blunt when they drift toward any of it.

## Traps that eat months

**Tutorial hopping.** Watching a fourth "Spring Boot in 3 hours" video feels like progress
and is not. Rule: if they haven't typed code in 20 minutes, they're consuming, not learning.

**Setup and tooling perfectionism.** IDE themes, plugin hunting, custom shell prompts,
`.editorconfig` debates. Pick IntelliJ IDEA Community, accept the defaults, move on.

**Learning Java exhaustively before building.** They do not need `synchronized`, custom
`ClassLoader`s, reflection, annotation processors, the module system, `finalize`, or
serialization to build this app. Learn features when a task requires them.

**Design patterns as a syllabus.** Memorising the Gang of Four list produces code full of
factories that solve nothing. Patterns are answers; learn them when you have the question.
(See the pain table in `architecture.md`.)

**Premature architecture.** Microservices, event sourcing, CQRS, hexagonal ports for
everything, an interface per class "for testability". Every one of these is a real solution
to a real problem they do not have. A well-organised single deployable ("modular monolith")
is the correct architecture for this app, and for the overwhelming majority of applications
that exist.

**Premature optimisation.** Caching, connection-pool tuning, async everything, before any
measurement exists. The exception: *query* problems (N+1, missing index) — those are
architecture, not optimisation, and they should learn to spot them early.

**Front-end rabbit holes.** Every hour spent on CSS is an hour not spent learning Java. The
pages should be ugly and functional until the backend is done.

**Rewriting from scratch.** The urge to restart because "the code is messy now" is
extremely strong and almost always wrong. Refactor in place — refactoring *is* the skill.
Restarting resets the learning to zero and feels productive.

**Configuration cargo-culting.** Copying a `application.yml` full of settings they can't
explain. Rule: no config line they can't explain in one sentence.

**Lombok and other magic, while learning.** Later, fine. Now it hides the language.

**Chasing 100% test coverage.** Test the domain rules thoroughly, the wiring lightly, the
framework not at all.

## Where the time actually pays off

In rough order of return on hours invested:

1. **Modelling the domain** — nouns, rules, invariants, and where each is enforced.
2. **Writing code, deleting it, and writing it better.** Volume of typed code is the single
   best predictor of progress.
3. **Reading errors properly.** Java stack traces are long and mostly noise; learning to
   find the one line that's yours is a permanent multiplier.
4. **SQL and the data model.** More durable than any framework knowledge, and the most
   expensive thing to get wrong.
5. **Tests for business rules.** They double as design feedback.
6. **Debugging with a debugger**, not print statements — breakpoints, watches, stepping.
7. **Reading other people's code** — the Spring source, a well-regarded open-source repo.
8. **Shipping it to a real URL early**, even half-finished. Deployment problems are
   architecture problems in disguise and they should meet them early, not at the end.

## The 80/20 of Java for this project

Learn now: classes/interfaces, records, enums, collections (`List`/`Map`/`Set`), generics
(use, not authorship), `Optional`, streams (basic), exceptions (checked vs unchecked),
`BigDecimal`, the date/time API, dependency injection, annotations (use, not authorship),
build lifecycle.

Learn later, when needed: concurrency primitives, virtual threads, reactive/WebFlux, JVM
tuning, custom annotations, reflection, the module system, sealed hierarchies, JNI, agents.
