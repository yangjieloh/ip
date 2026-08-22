# UI Test Plan

Run each test case in a fresh instance of `Pixel`. Output comparisons are exact except that CRLF and LF line endings are considered equivalent. Each command below is followed by Enter.

## UI-00: Reject an empty ToDo and an unknown command

**Aim:** Verify that a ToDo without a description and an unrecognized command produce the required error messages without being added as tasks.

**Input commands:**

```text
todo
blah
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Oops! Please give me a description for the todo.
____________________________________________________________
____________________________________________________________
Sorry, I don't recognise that command.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-01: Add and list tasks

**Aim:** Verify that Pixel stores entered task descriptions, lists them in order as not done, and exits on `bye`.

**Input commands:**

```text
todo read book
todo return book
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-02: Mark and unmark a task

**Aim:** Verify that `mark` completes the selected task and `unmark` reverses its done status.

**Input commands:**

```text
todo read book
todo return book
mark 2
list
unmark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[T][X] return book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-03: Add and manage typed tasks

**Aim:** Verify that Pixel adds ToDos, Deadlines, and Events; treats date/time values as strings; lists each task with its type icon; and preserves typed-task formatting when marking and unmarking.

**Input commands:**

```text
todo borrow book
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
mark 2
list
unmark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][X] do homework (by: no idea :-p)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] do homework (by: no idea :-p)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-04: Reject malformed dated tasks

**Aim:** Verify that incomplete Deadline and Event commands show usage guidance without terminating Pixel or adding invalid tasks.

**Input commands:**

```text
deadline do homework
deadline /by Sunday
event project meeting /from Monday
event project meeting /from /to Friday
todo valid task
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Oops! Please specify the deadline using /by.
____________________________________________________________
____________________________________________________________
Oops! Please specify the deadline using /by.
____________________________________________________________
____________________________________________________________
Oops! Please specify the event using /from and /to.
____________________________________________________________
____________________________________________________________
Oops! The event description and times cannot be empty.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] valid task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] valid task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-05: Preserve task state across rejected creation commands

**Aim:** Verify that invalid Deadline, Event, and unknown commands interleaved with valid additions neither create tasks nor disturb task order.

**Input commands:**

```text
todo alpha
deadline missing deadline
deadline beta /by Sunday
event broken /from /to Friday
event gamma /from Monday /to Tuesday
blah
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Oops! Please specify the deadline using /by.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Oops! The event description and times cannot be empty.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: Monday to: Tuesday)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Sorry, I don't recognise that command.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] alpha
2.[D][ ] beta (by: Sunday)
3.[E][ ] gamma (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-06: Preserve completion state across invalid task numbers

**Aim:** Verify that invalid, out-of-range, zero, and nonnumeric mark/unmark commands do not modify the completion state of existing tasks.

**Input commands:**

```text
todo alpha
mark 1
mark 0
mark 2
mark abc
unmark 1
unmark 0
unmark abc
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] alpha
____________________________________________________________
____________________________________________________________
That task number does not exist.
____________________________________________________________
____________________________________________________________
That task number does not exist.
____________________________________________________________
____________________________________________________________
Please specify a valid task number after mark.
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] alpha
____________________________________________________________
____________________________________________________________
That task number does not exist.
____________________________________________________________
____________________________________________________________
Please specify a valid task number after unmark.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] alpha
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-07: Recover after missing task fields

**Aim:** Verify that empty descriptions and missing date/time fields are rejected and that a subsequent valid task can still be added and listed.

**Input commands:**

```text
todo
deadline
deadline homework /by
event
event meeting /from Monday
event meeting /from Monday /to
todo recovered
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Oops! Please give me a description for the todo.
____________________________________________________________
____________________________________________________________
Oops! Please give me a description and deadline.
____________________________________________________________
____________________________________________________________
Oops! Please specify the deadline using /by.
____________________________________________________________
____________________________________________________________
Oops! Please give me an event description and time.
____________________________________________________________
____________________________________________________________
Oops! Please specify the event using /from and /to.
____________________________________________________________
____________________________________________________________
Oops! The event description and times cannot be empty.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] recovered
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] recovered
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-08: Delete tasks and preserve list order

**Aim:** Verify that deleting a middle task removes the correct object, shifts later tasks forward, updates the count, and uses the new numbering for a subsequent deletion.

**Input commands:**

```text
todo alpha
deadline beta /by Sunday
event gamma /from Monday /to Tuesday
mark 2
delete 2
list
delete 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: Monday to: Tuesday)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] beta (by: Sunday)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][X] beta (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] alpha
2.[E][ ] gamma (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] gamma (from: Monday to: Tuesday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] alpha
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-09: Preserve state across invalid delete commands

**Aim:** Verify that zero, out-of-range, and nonnumeric delete commands are rejected without removing or modifying existing tasks.

**Input commands:**

```text
todo alpha
mark 1
delete 0
delete 2
delete abc
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] alpha
____________________________________________________________
____________________________________________________________
That task number does not exist.
____________________________________________________________
____________________________________________________________
That task number does not exist.
____________________________________________________________
____________________________________________________________
Please specify a valid task number after delete.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] alpha
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-10: Save task changes to disk

**Aim:** Verify that adding, marking, unmarking, and deleting typed tasks saves the resulting list to `data/pixel.txt` without changing console behavior.

**Input commands:**

```text
todo alpha
deadline beta /by Sunday
event gamma /from Monday /to Tuesday
mark 1
unmark 1
mark 2
delete 3
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: Monday to: Tuesday)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] alpha
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] alpha
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] beta (by: Sunday)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] gamma (from: Monday to: Tuesday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved file (`data/pixel.txt`):**

```text
T | 0 | alpha
D | 1 | beta | Sunday
```

## UI-11: Load tasks and continue saving changes

**Aim:** Verify that Pixel restores ToDos, Deadlines, Events, and their done statuses at startup, then correctly saves later changes to those loaded tasks.

**Initial saved file (`data/pixel.txt`):**

```text
T | 1 | alpha
D | 0 | beta | Sunday
E | 1 | gamma | Monday | Tuesday
```

**Input commands:**

```text
list
mark 2
unmark 3
delete 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] alpha
2.[D][ ] beta (by: Sunday)
3.[E][X] gamma (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] beta (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [E][ ] gamma (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] alpha
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][X] beta (by: Sunday)
2.[E][ ] gamma (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved file (`data/pixel.txt`):**

```text
D | 1 | beta | Sunday
E | 0 | gamma | Monday | Tuesday
```
