Change-Log
===============
> Regular configuration update: _01.06.2018_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### 1.0.4 ###
> upcoming

- Small updates and improvements.

### [1.0.3](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.3) ###
> 28.06.2018

- Small updates.

### [1.0.2](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.2) ###
> 15.03.2018

- `ItemDividerDecoration` now accepts **offsets** for its divider either via `setDividerOffset(int, int)`
  or via _Xml_ attributes: `recyclerDividerOffsetStart`, `recyclerDividerOffsetEnd`.
- Added ability to specify a `Precondition` for a desired `RecyclerViewItemDecoration` which may be
  useful for more item specific decorating.

### [1.0.1](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.1) ###
> 21.05.2017

- `ItemSwipeHelper.OnSwipeListener.onSwipeCanceled(...)` is now not being called if the associated
  `RecyclerView` is **computing layout** a the time when `ItemSwipeHelper.Interactor.clearView(...)`
  is requested.

### [1.0.0](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.0) ###
> 29.04.2017

- First production release.