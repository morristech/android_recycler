Change-Log
===============

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### [1.0.1](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.1) ###
> 21.05.2017

- `ItemSwipeHelper.OnSwipeListener.onSwipeCanceled(...)` is now not being called if the associated
  `RecyclerView` is **computing layout** a the time when `ItemSwipeHelper.Interactor.clearView(...)`
  is requested.

### [1.0.0](https://github.com/universum-studios/android_recycler/releases/tag/v1.0.0) ###
> 29.04.2017

- First production release.