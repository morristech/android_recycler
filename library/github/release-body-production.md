### Overview ###

Release patch.

### Changes ###

- `ItemDividerDecoration` now accepts **offsets** for its divider either via `setDividerOffset(int, int)`
  or via _Xml_ attributes: `recyclerDividerOffsetStart`, `recyclerDividerOffsetEnd`.
- Added ability to specify a `Precondition` for a desired `RecyclerViewItemDecoration` which may be
  useful for more item specific decorating.