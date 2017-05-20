Change-Log
===============

### Release 1.0.1 ###
> --.05.2017

- `ItemSwipeHelper.OnSwipeListener.onSwipeCanceled(...)` is now not being called if the associated
  `RecyclerView` is **computing layout** a the time when `ItemSwipeHelper.Interactor.clearView(...)`
  is requested.

### [Release 1.0.0] (https://github.com/universum-studios/android_recycler/releases/tag/1.0.0) ###
> 29.04.2017

- Finalized `ItemDividerDecoration`.
- Finalized `ItemSpaceDecoration`.
- Finalized `ItemSwipeHelper`.
- Implemented `ItemDragHelper`.

### [Pre-release 0.1.0](https://github.com/universum-studios/android_recycler/releases/tag/0.1.0) ###
> 27.01.2017

- First pre-release.