# Android-MVP-Dagger2
This repository contains a detailed sample News application that uses MVP as its presentation layer pattern. **The app aims to be extremely flexible to creating variants for automated and manual testing.** Also, the project implements and follows the guidelines presented in Google Sample [MVP+dagger2+dagger-android](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/).

Essential dependencies are Dagger2 with Dagger-android, RxJava with RxAndroid, Room, Retrofit and Espresso. Other noteworthy dependencies would be Mockito, Picasso and Guava.
# App Demo
![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/scrolling.gif)
![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/archiving.gif)
![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/open_tab.gif)
## Presentation Layer
As you can see in the below diagram, Views are intended to be as dumb as possible. The Presenter handles most of the logic therefore cancelling any dependency between the View layer and the Model layer.

It is easy to spot the fact that the Model layer is completely isolated and centralized through the repository pattern.

![Presentation](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/presentation_layer_diagram.png)
## Dependency Injection
Dagger2 is used to externalize the creation of dependencies from the classes that use them. Android specific helpers are provided by `Dagger-Android` and the most significant advantage is that they generate a subcomponent for each `Activity` through a new code generator.
Such subcomponent is:
```java
@ActivityScoped
@ContributesAndroidInjector(modules = NewsModule.class)
abstract NewsActivity newsActivity(); 
```
The below diagram illustrates the most significant relations between components and modules. You can also get a quick glance on how annotations help us define custom Scopes in order to properly handle classes instantiation.

![Dependecy](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/dependecy_graph_diagram.png)
_Note: The above diagram might help you understand how Dagger-android works. Also, only essential components/modules/objects are included here, this is suggested by the "…"_
## Model Layer
As you might have noticed in the first diagram, the repository handles data interactions and transactions from two main data sources - local and remote:
- `NewsRemoteDataSource` defined by a REST API consumed with [Retrofit](http://square.github.io/retrofit)
- `NewsLocalDataSource` defined by a SQL database consumed with [Room](https://developer.android.com/topic/libraries/architecture/room)

When data is being retrieved (from any source), every response is propagated through callbacks all the way to the `NewsPresenter` that handles them accordingly.

The same way as the Presenter-View relationship depends entirely on interfaces defined in `NewsContract`, decoupling is reinforced within the Model layer (entirely consisted by `NewsRepository`). Therefore, lower level components (which are the data sources: `NewsRemoteDataSource` and `NewsLocalDatasourece`) are decoupled through `NewsDataSource` interface.

In this manner, the project respects the DIP (Dependency Inversion Principle) as both low and high level modules depend on abstractions.
### Reactive approach
It is extremely important to note that this project has a low level of reactiveness, it might barely dream to the possibilities of a effective reactive approach.
Nevertheless, the app was intended to have a flexible and efficient testing capability, rather than a fully reactive build.

Even in this case, you will be able to notice RxJava's benefits when data is being retrieved by `NewsRemoteDataSource` from the REST client ([News API](https://newsapi.org/)):
- threading is much easier, with no need for the dreaded `AsyncTasks` 
- error handling is now fun 
- any reactive process is immediately stopped in certain situations of the apps' life cycle with the help of `Disposable`
## Strong points
- possess high flexibility to create variants for automated and manual testing
- possess lightweight structure due to its presentation layer pattern
- is scalable - the app is easy to expand
## Weak points
- maintenance effort could be lower and scalability could be better - even though the app has a solid structure and complies to some of the SOLID principles, it cannot be considered as part of Clean Architecture, mainly because the Presenter contains most of the logic, therefore substituting Interactors (use-cases) 
- possess medium complexity - other approaches might lower complexity and increase efficiency

# Final notes:
- the app is not a polished ready-to-publish product, it acts merely as a starting point for android enthusiasts out there 
- using this project as your starting point and expanding it is also encouraged, as at this point it is very easy to add new modules
- forks are encouraged