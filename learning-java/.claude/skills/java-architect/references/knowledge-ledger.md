# The knowledge ledger

A persistent record of everything taught, so that sessions build on each other instead of
starting from zero. Lives in `docs/learning/` in the repo, not in this skill — it's the
user's, and it's readable without an agent.

```
docs/learning/
  INDEX.md              <- the table: one row per concept, the thing you read first
  <concept-slug>.md     <- one note per concept, from templates/concept-note.md
```

## Rule 1 — write the note in the session that taught it

Not at the end of the week, not "when it's solid". Write it the moment the explain-back
check passes, while the exact wording that worked is still in the conversation. The
**"How I explained it"** and **"What tripped me up"** fields are the whole point — a
textbook definition is worthless here because they can already get that anywhere.

One concept per note. If a session taught three things, write three notes.

What counts as a concept worth a note:
- A Java or Spring mechanism they now use (`BigDecimal`, constructor injection, `@Transactional`)
- An architecture principle applied to a real decision (dependency direction, deep modules)
- A domain insight about Tally (why balances might be derived rather than stored)
- A debugging or tooling technique that will recur (reading a stack trace, `EXPLAIN`)

What does not: anything they already knew from another language, one-off syntax, trivia.

## Rule 2 — the index is the entry point

`INDEX.md` is a table. Read it at the **start of every session**, before anything else.
It is cheap to read and tells you what this person already knows, which changes how you
explain the next thing. Never re-teach a `solid` concept from scratch — reference the note
and build on it.

Columns: `Concept | Slug | Milestone | Status | First taught | Last reviewed`.

## Rule 3 — status is earned, not assigned

| Status | Means | Promoted when |
|---|---|---|
| `taught` | Explained and understood in the moment | — |
| `applied` | They used it correctly in their own code, unprompted | You see it in a review |
| `solid` | They used it correctly *and* explained a limit or trade-off of it | They pass a recall check without hints |

Demote freely. If a review shows they've forgotten something marked `solid`, drop it to
`taught` and say so plainly — the ledger is only useful if it's honest.

## Rule 4 — refresh at the start, not the end

Every session opens with **one** recall check, chosen this way:

1. Anything `taught` from the previous session — always check it first. This is the
   highest-value moment; it's when the forgetting curve is steepest.
2. Otherwise, the `applied` concept with the oldest `Last reviewed`, if it's 3+ sessions old.
3. Otherwise, a `solid` concept that's relevant to today's task — framed as "remember when
   we…", not as a test.

Ask the question from the note's **Recall check** field. Do not show the answer first.
If they miss it: don't re-explain from scratch — reopen the note, use *the same framing
that worked the first time*, and update "What tripped me up" with the new failure. Then
update `Last reviewed` and the status either way.

One check. Not five. This is a warm-up, not an exam, and a session that opens with a quiz
is a session they stop starting.

## Rule 5 — the ledger is a reference during work, too

When they hit something in build or review mode that has a note, **open the note and cite
it**: "you ran into this at M2 — here's what you concluded then." Two effects: it surfaces
their own past reasoning, which is far more persuasive than yours, and it shows them the
ledger is worth maintaining.

If a concept resurfaces in a new place, add the new location to **"Where it shows up in my
code"**. Concepts that appear in many files are the load-bearing ones.

## Rule 6 — connect notes

Use `[[slug]]` links in the **Related** field, liberally. "Invariants decide where code
lives" links to "deep modules" links to "dependency direction". The links are the point:
isolated facts decay, connected ones don't. When adding a note, always ask what existing
note it relates to.

## On demand

If they ask *"what have I learned?"*, *"remind me about X"*, or *"quiz me"* — that's
**recall mode**. Read `INDEX.md`, then:
- **What have I learned** → summarise by theme, not chronologically, and name the two or
  three concepts that are load-bearing for everything else.
- **Remind me about X** → open the note, lead with "How I explained it" (the framing they
  already have a hook for), then the code locations.
- **Quiz me** → 3–5 recall checks weighted toward `taught` and stale `applied`. Grade
  honestly, update statuses.
