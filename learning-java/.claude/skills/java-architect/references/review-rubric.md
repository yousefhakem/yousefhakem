# Review rubric

Review in levels. **Stop at the first level with real findings** and fix those before going
deeper — a beginner given twenty notes at once learns nothing from any of them. Aim for
three findings per review, maximum.

Always phrase a finding as: *what* → *why it costs something later* → *a question that lets
them find the fix themselves*. Never hand them the corrected code.

Bad: "Use BigDecimal here."
Good: "This total is a `double`. Try adding 0.1 + 0.2 in a test and print it — what does
that mean for a balance after 200 expenses?"

## Level 1 — Correctness
- Does it do what the spec said?
- Edge cases: empty collection, one element, duplicate, zero, negative, very large.
- Are the domain invariants actually enforced, or just enforced on the happy path?
- Rounding: does money still add up after splitting 10.00 three ways?
- Null handling — is it deliberate or accidental?
- Concurrency: can two requests interleave and corrupt this?

## Level 2 — Design
- **Dependency direction.** Does anything in `domain` import Spring, JPA, or web types?
- **Where does the rule live?** Business logic in a controller or a repository is misplaced.
- **Interface depth.** Does this class hide complexity, or just forward calls? Would
  deleting it lose anything?
- **Blast radius.** If this entity gained a field, how many files change?
- **Bypassable invariants.** Is there a second way to construct this object that skips the
  validation?
- **Stored vs derived.** Is state being persisted that could be computed — and if so, what
  keeps it correct?
- **Leaked abstractions.** Does the caller need to know how this works to use it correctly?
- **Speculative generality.** An interface with one implementation and no second one in
  sight, a config option nobody sets, a strategy pattern with one strategy.

## Level 3 — Java and Spring idiom
- Records for value types; classes for things with identity and behaviour.
- `Optional` as a return type, not as a field or a parameter.
- Checked vs unchecked exceptions — and never an empty `catch`.
- Collections: right type, immutable where possible, no exposing internal mutable state.
- Streams where they clarify; a plain loop where they don't.
- Constructor injection, not field injection. No `@Autowired` on fields.
- Transaction boundaries: at the application service, not the repository.
- `equals`/`hashCode` consistency, especially on JPA entities.

## Level 4 — Style
Naming (domain language, not `data`/`info`/`manager`), method length, comments that explain
*why* not *what*, dead code, formatting. Mention briefly; never lead with this.

## Also, every review

- Name **one thing they did well**, specifically. Not encouragement for its own sake —
  they need to know which instincts to repeat.
- Ask **one question about a decision they made**, so they have to defend it. If they can't,
  that's the lesson of the session.
