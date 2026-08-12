# The craft of architecting an application

This is the reference for **design** mode. Teach from it; don't paste it.

## 1. What architecture actually is

Architecture is **the set of decisions that are expensive to reverse**. That's the whole
definition. It is not folder structure, not diagrams, not which framework you picked.

The practical consequence: your design effort should be proportional to the *cost of being
wrong*, not to how interesting the problem is. Most decisions in an application are cheap
to reverse and deserve a default and five minutes. A small number are expensive and
deserve an afternoon, a written record, and a deliberate choice.

## 2. The cost-of-reversal filter

Before any decision, ask: **if this turns out wrong in three months, what does it cost to
change?**

**Two-way doors (cheap — decide fast, move on):**
- Class and method names, package layout, file organisation
- Whether a method lives on the service or the entity
- Validation library, logging library, test assertion style
- HTML/CSS, page layout, most UI
- Which cloud host, most config values
- Internal implementation of anything behind an interface

**One-way doors (expensive — these are your architecture):**
- **The data model.** Once real users have real rows, changing what an entity *means* is
  a migration, a backfill, and a bug hunt. Getting `Expense`/`Split`/`Settlement` wrong
  costs weeks. This is the single highest-leverage thing to think about.
- **Module boundaries and dependency direction.** Which parts of the code are allowed to
  know about which other parts. Once a dependency exists, everything downstream assumes
  it, and untangling it touches every file.
- **The identity and permission model.** Who is a user, what can they see, what is a
  tenant boundary. Retrofitting authorization into code that assumed a single trusting
  user is a rewrite of every query.
- **Public contracts.** Any API shape, URL, or event format that something outside your
  control depends on. Once published, you can only add, never change.
- **Choice of persistence paradigm** (relational vs document vs event-sourced). Not
  "Postgres vs MySQL" — that's cheap. "Rows vs events" is not.

Everything else: pick the boring default and keep moving.

## 3. Why doing this *now* saves time (the actual mechanism)

The user asked why. Don't answer with "clean code is good". Answer with mechanics:

- **Rework cost compounds.** A wrong data model costs one hour to fix on day 1, one day in
  week 3, and a week once there's data, tests, and UI depending on it. The cost curve is
  roughly exponential in the number of things that depend on the decision. Architecture
  is just *deciding the high-dependency things first, while their dependency count is zero*.
- **Blast radius determines your speed.** How fast you can add a feature is determined by
  how many files you have to touch and hold in your head. Good boundaries mean a feature
  touches 3 files instead of 20. That's not aesthetics — that's the difference between a
  two-hour change and a two-day change, every single time, forever.
- **Debugging cost is proportional to how many things could be responsible.** Clear
  boundaries mean the bug is in one of three places, not one of thirty.
- **Testability is a consequence of architecture, not a separate activity.** Code that's
  hard to test is code with too many dependencies. If a test needs a running database and
  an HTTP server to check a business rule, the business rule is in the wrong place. Notice
  this: "this is annoying to test" is your best architectural smoke alarm.
- **AI agents amplify the effect.** An agent working in a codebase with clear seams can be
  handed one module and succeed. In a tangled codebase it must read everything, and it
  makes plausible-looking changes in the wrong layer. Architecture is now also a lever on
  how much help you can get.

The inverse is equally important and less often said: **over-architecting costs the same
way.** Abstractions built for requirements that never arrive are dependencies you pay for
on every change. The skill is not "design more", it's "design the expensive things and
nothing else".

## 4. How to design a feature (the loop to teach)

For every non-trivial feature, in this order:

1. **State the behaviour in one sentence, in domain language.** "A member can record an
   expense that is split among a subset of the group." If it takes a paragraph, it's more
   than one feature.
2. **Name the nouns and their rules.** What entities exist, what must always be true about
   them (invariants). *"The sum of the splits equals the total"* is an invariant — write it
   down, because it decides where the code lives.
3. **Decide where the invariant is enforced.** An invariant should be enforced in one
   place, and that place should be impossible to bypass. This single question resolves
   most "should this be in the service or the entity" arguments.
4. **Write the caller before the implementation.** Write the line of code you *wish* you
   could write. That's the interface. If it's awkward to call, the design is wrong, and
   you found out for free.
5. **Ask what breaks.** Concurrency, partial failure, bad input, a malicious member,
   money rounding. Pick the ones that are real for your app and handle them; explicitly
   ignore the rest, out loud.
6. **Only then, code.**

## 5. Deep modules and narrow interfaces

The most useful single heuristic in software design: **a good module hides a lot of
complexity behind a small interface.** Depth = functionality ÷ interface size.

- `SettlementCalculator.simplify(List<Debt>) -> List<Transfer>` is deep: one method, a
  genuinely hard algorithm hidden.
- A "manager" class with fifteen methods that each do one line of work is shallow — it
  costs more to learn than it saves.

Shallow modules are worse than no module. When they're tempted to add a layer, ask: **what
complexity does this hide?** If the answer is "none, it just forwards calls", delete it.

Corollary: **the interface should be simpler than the implementation.** If describing how
to use it is as hard as describing how it works, you haven't abstracted anything.

## 6. Dependency direction

The one structural rule worth being strict about: **dependencies point inward, toward the
domain.**

```
web/http  ->  application/services  ->  domain (entities, rules)
                     |
                     v
              infrastructure (db, email, external APIs)
```

- The domain knows nothing about HTTP, JSON, Spring, or SQL.
- The web layer knows about the application layer; nothing knows about the web layer.
- Infrastructure implements interfaces *defined by* the inner layers.

Why it pays: your business rules become testable in milliseconds with no framework, and
swapping the database or adding a second entry point (CLI, scheduled job, API) doesn't
touch them. Test the rule: **can you delete the web layer and still compile the domain?**

Don't teach this as a folder-naming exercise. Teach it as: "which way do the arrows point,
and what happens when one points the wrong way".

## 7. When to introduce a pattern

Never introduce a pattern because it's correct. Introduce it when a **specific pain** shows
up. Wait for the pain; the pain teaches the pattern better than any explanation.

| Pain they will actually feel | The response |
|---|---|
| The same query logic copy-pasted in three places | Extract a repository method |
| A test needs a real database to check arithmetic | Move the rule into the domain |
| A controller is 200 lines | Extract an application service |
| Changing an entity field breaks the JSON API | Introduce DTOs at the boundary |
| Two features both need "notify the user" | Extract a port + adapter |
| A change to email breaks expense tests | The dependency points the wrong way |

If they propose a pattern with no pain behind it, ask: "what will break if we don't?"

## 8. Recording decisions

Every one-way-door decision gets an ADR (`templates/adr.md`): the context, the options,
the choice, and the consequences. Two reasons this is worth the ten minutes:

1. In two months they will not remember why, and will either re-litigate it or break the
   reason.
2. Writing "the consequences" forces them to notice consequences they hadn't considered.
   Half the value is that the decision changes while writing it down.

Do not write ADRs for two-way doors. That's ceremony, and it teaches the wrong lesson.
