ButtonCodeChallenge
-------------------


Language
--------
Kotlin

Architecture 
------------
MVVM

Libraries
---------
RxJava 2,
Dagger 2,
Retrofit,
Android Debug Database

Android Architecture Components
-------------------------------
Data Binding,
Live Data,
Room,
Lifecycle,

Notes
-----
Facilitated communication between HomeActivity and CreateUserFragment with a shared ViewModel. All data is downloaded via Retrofit and stored into Room before being distributed to the views from the application’s repository. 

LiveData is used to broadcast Network / Loading status to indicate when the app is offline or encounters an error. RecyclerViews are wrapped in SwipeRefreshLayouts to refresh repository data.
