1. lets add button do add or delete all expenses
   2. this is for testing purpose
   3. lets add a view model for this 
      4. it will have to use cases - add dummy expenses, delete all expenses
      5. also will be two data source methods, and two repository methods

2. add a new screen, called DevScreen
   3. inside, add buttons to add dummy expenses, and delete all expenses
   4. will need dummyScreenViewModel
      5. which uses these use cases
--------------



3. figure out how to
   2. get actual category together with expense
      3. do we manually have to join, or room will join automatically when foreign key is set
2. forget all of these converters and shit - lets just convert in ui for now
2. ------
2. 
3. 
4. we actually need to pass all expenses to all expeses tab
2. and we need to calcluate today and this week expenses, and pass them to current expenses tab
- view model should calculate that i guess
3. add dummy use case to submit new expenses
- need some value class for that 
2. add database
- first add dummy DI
- then create view models and some logic to add and retrieve expenses, very simple way
2. add hilt
- use hilt for database and rest

# thoughts
maybe costs is not a greate name for feature layer? maybe expenses is better?



3. resources for join db
   4. https://medium.com/android-news/android-architecture-components-room-relationships-bf473510c14a
   5. https://proandroiddev.com/room-database-lessons-learnt-from-working-with-multiple-tables-d499c9be94ce
   6. https://medium.com/@manishkumar_75473/android-room-database-series-part-3-relationship-a-note-taking-example-c96fc25284e9