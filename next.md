1. need to define state that will hold current reports
2. need to actually set this state when reports are fetched
3. need to create table for daily budget
- no idea how will daily budget look like. i guess we do daily budget for entire month. so we would store year, month, daily budget amount?
4. need to trigger collect on reports whenever daily budget is changed
- which means that we need to store daily budget in some kidn of flow in view model, and then subscrtibe ot it? maybe curret state flow does it already? not sure
5. does collecting reports every time daily budget changes make sense? maybe we should just collect reports once, and then whenever daily budget changes we just recalculate something?
- maybe. so maybe use case would actually return expenses for day, and set them in private state somewhere. then whenever daily budget changes we would recalculate remaining budget for day?
- ofc, we would still be subscribing to collecting expenses for day, so if expenses change we would also recalculate remaining budget for day. but we would only be fetching reports once day changes 
- the results is the same, but we would be doing less work when daily budget changes. we would just recalculate remaining budget instead of collecting reports again
6. also, question is, do we need cancel previous collecting when daily budget changes? or do we just let it run and update state whenever reports are fetched? i am not sure how it works with android jetpack compose. maybe it automatically cancels previous collecting when new collecting is started? not sure. i thing there was some cancelation of coroutine or scope - there is some kind of mecahnism for that

----------
1. lets create entity for daily budget
- maybe we can also store date added. so we can always 